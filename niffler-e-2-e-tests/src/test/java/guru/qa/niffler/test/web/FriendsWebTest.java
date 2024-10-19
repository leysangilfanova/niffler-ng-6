package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.StaticUser;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.service.UsersDbClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.EMPTY;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.WITH_FRIEND;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.WITH_INCOME_REQUEST;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.WITH_OUTCOME_REQUEST;
import static guru.qa.niffler.page.BaseTest.CFG;
import static guru.qa.niffler.utils.RandomDataUtils.randomPassword;
import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

@WebTest
public class FriendsWebTest {

    private final UsersDbClient usersDbClient = new UsersDbClient();
    private final String username = randomUsername();
    private final String password = randomPassword();

    @Test
    @DisplayName("У пользователя есть друг")
    void friendShouldBePresentInFriendsTable(@UserType(WITH_FRIEND) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .openFriendsPage()
                .existingFriendsCheck(user.friend());
    }

    @Test
    @DisplayName("У нового пользователя нет друзей")
    void friendsTableShouldBeEmptyForNewUser(@UserType(EMPTY) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .openFriendsPage()
                .noExistingFriendsCheck();
    }

    @Test
    @DisplayName("У пользователя есть входящая заявка в друзья")
    void incomeInvitationBePresentInFriendsTable(@UserType(WITH_INCOME_REQUEST) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .openFriendsPage()
                .invitationsToFriendsCheck(user.income());
    }

    @Test
    @DisplayName("У пользователя есть отправленная заявка в друзья")
    void outcomeInvitationBePresentInAllPeoplesTable(@UserType(WITH_OUTCOME_REQUEST) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .openFriendsPage()
                .clickAllPeopleBtn()
                .invitationSentToUserCheck(user.outcome());
    }

    @Test
    @DisplayName("Прием заявки в друзья")
    void acceptInvitationToFriendsTest() {
        UserJson mainUser = usersDbClient.createUser(username, password);
        usersDbClient.addIncomeInvitation(mainUser, 1);

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(username, password);

        new FriendsPage()
                .getHeader()
                .toFriendsPage()
                .acceptFriend()
                .unfriendBtnIsVisibleCheck();
    }

    @Test
    @DisplayName("Отклонение заявки в друзья")
    void declineInvitationToFriendsTest() {
        UserJson mainUser = usersDbClient.createUser(username, password);
        usersDbClient.addIncomeInvitation(mainUser, 1);

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(username, password);

        new FriendsPage()
                .getHeader()
                .toFriendsPage()
                .declineFriend()
                .noExistingFriendsCheck();
    }
}
