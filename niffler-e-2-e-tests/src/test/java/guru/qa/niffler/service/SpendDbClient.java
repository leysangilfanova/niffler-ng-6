package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.CategoryDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoSpringJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.List;
import java.util.stream.Collectors;

import static guru.qa.niffler.data.Databases.dataSource;
import static guru.qa.niffler.data.Databases.transaction;

public class SpendDbClient {

    private static final Config CFG = Config.getInstance();

    public SpendJson createSpend(SpendJson spend) {
        return transaction(connection -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);
                    if (spendEntity.getCategory().getId() == null) {
                        CategoryEntity categoryEntity = new CategoryDaoJdbc(connection)
                                .create(spendEntity.getCategory());
                        spendEntity.setCategory(categoryEntity);
                    }
                    return SpendJson.fromEntity(
                            new SpendDaoJdbc(connection).create(spendEntity)
                    );
                },
                CFG.spendJdbcUrl()
        );
    }

    public List<SpendJson> findAllSpends() {
        return transaction(connection -> {
                    List<SpendEntity> spendEntities = new SpendDaoJdbc(connection).findAll();
                    return spendEntities.stream()
                            .map(SpendJson::fromEntity)
                            .collect(Collectors.toList());
                },
                CFG.spendJdbcUrl()
        );
    }

    public List<SpendJson> findAllSpendsSpring() {
        return transaction(connection -> {
                    List<SpendEntity> spendEntities = new SpendDaoSpringJdbc(dataSource(CFG.spendJdbcUrl())).findAll();
                    return spendEntities.stream()
                            .map(SpendJson::fromEntity)
                            .collect(Collectors.toList());
                },
                CFG.spendJdbcUrl()
        );
    }

    public List<CategoryJson> findAllCategories() {
        return transaction(connection -> {
                    List<CategoryEntity> categoryEntities = new CategoryDaoJdbc(connection).findAll();
                    return categoryEntities.stream()
                            .map(CategoryJson::fromEntity)
                            .collect(Collectors.toList());
                },
                CFG.spendJdbcUrl()
        );
    }

    public List<CategoryJson> findAllCategoriesSpring() {
        return transaction(connection -> {
                    List<CategoryEntity> categoryEntities = new CategoryDaoSpringJdbc(dataSource(CFG.spendJdbcUrl())).findAll();
                    return categoryEntities.stream()
                            .map(CategoryJson::fromEntity)
                            .collect(Collectors.toList());
                },
                CFG.spendJdbcUrl()
        );
    }
}
