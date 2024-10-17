package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.data.entity.userdata.UserEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserJson(
        @JsonProperty("id")
        @Nullable UUID id,
        @JsonProperty("username")
        @Nullable String username,
        @JsonProperty("firstname")
        @Nullable String firstname,
        @JsonProperty("surname")
        @Nullable String surname,
        @JsonProperty("fullname")
        @Nullable String fullname,
        @JsonProperty("currency")
        @Nullable CurrencyValues currency,
        @JsonProperty("photo")
        @Nullable String photo,
        @JsonProperty("photoSmall")
        @Nullable String photoSmall,
        @JsonProperty("friendState")
        @Nullable FriendState friendState,
        @JsonIgnore
        @Nullable TestData testData) {

    @Nonnull
    public static UserJson fromEntity(@Nonnull UserEntity entity, @Nullable FriendState friendState) {
        return new UserJson(
                entity.getId(),
                entity.getUsername(),
                entity.getFirstname(),
                entity.getSurname(),
                entity.getFullname(),
                entity.getCurrency(),
                entity.getPhoto() != null && entity.getPhoto().length > 0 ? new String(entity.getPhoto(), StandardCharsets.UTF_8) : null,
                entity.getPhotoSmall() != null && entity.getPhotoSmall().length > 0 ? new String(entity.getPhotoSmall(), StandardCharsets.UTF_8) : null,
                friendState,
                null
        );
    }

    @Nonnull
    public UserJson addTestData(@Nonnull TestData testData) {
        return new UserJson(
                id, username, firstname, surname, fullname, currency, photo, photoSmall, friendState, testData
        );
    }
}
