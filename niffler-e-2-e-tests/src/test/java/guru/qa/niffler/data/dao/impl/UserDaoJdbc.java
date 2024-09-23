package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.UserDao;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.CurrencyValues;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.UUID;

public class UserDaoJdbc implements UserDao {
    private static final Config CFG = Config.getInstance();

    @Override
    public UserEntity createUser(UserEntity user) {
        try (Connection connection = Databases.connection(CFG.userdataJdbcUrl())) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO \"user\" (username, firstname, surname, full_name, currency, photo, photo_small) "
                            + "VALUES (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, user.getUsername());
                ps.setString(2, user.getFirstname());
                ps.setString(3, user.getSurname());
                ps.setString(4, user.getFullname());
                ps.setString(5, user.getCurrency().name());
                ps.setBytes(6, user.getPhoto());
                ps.setBytes(7, user.getPhotoSmall());

                ps.executeUpdate();

                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setId(generatedKeys.getObject(1, UUID.class));
                    } else {
                        throw new SQLException("Creating user failed, no ID obtained.");
                    }
                }

                return user;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        return findUserByQuery("SELECT * FROM \"user\" WHERE id = ?", ps -> ps.setObject(1, id));
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return findUserByQuery("SELECT * FROM \"user\" WHERE username = ?", ps -> ps.setString(1, username));
    }

    private Optional<UserEntity> findUserByQuery(String query, QueryParameterSetter setter) {
        try (Connection connection = Databases.connection(CFG.userdataJdbcUrl());
             PreparedStatement ps = connection.prepareStatement(query)) {

            setter.setParameters(ps);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUserEntity(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public void delete(UserEntity user) {
        deleteById(user.getId());
    }

    public void deleteById(UUID id) {
        try (Connection connection = Databases.connection(CFG.userdataJdbcUrl())) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "DELETE FROM \"user\" WHERE id = ?")) {

                ps.setObject(1, id);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private UserEntity mapResultSetToUserEntity(ResultSet rs) throws SQLException {
        UserEntity user = new UserEntity();
        user.setId(rs.getObject("id", UUID.class));
        user.setUsername(rs.getString("username"));
        user.setFirstname(rs.getString("firstname"));
        user.setSurname(rs.getString("surname"));
        user.setFullname(rs.getString("full_name"));
        user.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
        user.setPhoto(rs.getBytes("photo"));
        user.setPhotoSmall(rs.getBytes("photo_small"));
        return user;
    }

    @FunctionalInterface
    private interface QueryParameterSetter {
        void setParameters(PreparedStatement ps) throws SQLException;
    }
}