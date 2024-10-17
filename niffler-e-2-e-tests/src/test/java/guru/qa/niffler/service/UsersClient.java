package guru.qa.niffler.service;

import guru.qa.niffler.model.UserJson;

import javax.annotation.Nonnull;

public interface UsersClient {
    @Nonnull
    UserJson createUser(@Nonnull String username, @Nonnull String password);

    void addIncomeInvitation(@Nonnull UserJson targetUser, int count);

    void addOutcomeInvitation(@Nonnull UserJson targetUser, int count);

    void addFriend(@Nonnull UserJson targetUser, int count);
}

