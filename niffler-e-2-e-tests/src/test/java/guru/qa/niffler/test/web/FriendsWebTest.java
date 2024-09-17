package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.StaticUser;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType;
import guru.qa.niffler.page.LoginPage;
import io.qameta.allure.Step;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.EMPTY;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.WITH_FRIEND;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.WITH_INCOME_REQUEST;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.WITH_OUTCOME_REQUEST;
import static guru.qa.niffler.page.Pages.friendsPage;
import static guru.qa.niffler.page.Pages.loginPage;
import static guru.qa.niffler.page.Pages.mainPage;
import static guru.qa.niffler.page.Pages.peoplePage;

@WebTest
public class FriendsWebTest {

    private static final Config CFG = Config.getInstance();

    @BeforeEach
    @Step("Открыть страницу авторизации")
    public void openLoginPage() {
        final Config CFG = Config.getInstance();
        open(CFG.frontUrl(), LoginPage.class);
    }

    @Test
    @DisplayName("У пользователя есть друг")
    void friendShouldBePresentInFriendsTable(@UserType(WITH_FRIEND) StaticUser user) {
        // Попробовала так писать тесты, мне не нравится этот способ :(
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .openFriendsPage()
                .existingFriendsCheck(user.friend());
    }

    @Test
    @DisplayName("У нового пользователя нет друзей")
    void friendsTableShouldBeEmptyForNewUser(@UserType(EMPTY) StaticUser user) {
        loginPage.login(user.username(), user.password());
        mainPage.openFriendsPage();
        friendsPage.noExistingFriendsCheck();
    }

    @Test
    @DisplayName("У пользователя есть заявка в друзья")
    void incomeInvitationBePresentInFriendsTable(@UserType(WITH_INCOME_REQUEST) StaticUser user) {
        loginPage.login(user.username(), user.password());
        mainPage.openFriendsPage();
        friendsPage.invitationsToFriendsCheck(user.income());
    }

    @Test
    void outcomeInvitationBePresentInAllPeoplesTable(@UserType(WITH_OUTCOME_REQUEST) StaticUser user) {
        loginPage.login(user.username(), user.password());
        mainPage.openFriendsPage();
        friendsPage.clickAllPeopleBtn();
        peoplePage.invitationSentToUserCheck(user.outcome());
    }
}
