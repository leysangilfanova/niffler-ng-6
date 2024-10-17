package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.EditSpendingPage;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.PeoplePage;
import guru.qa.niffler.page.ProfilePage;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class Header {

    private final SelenideElement header = $("#root header");
    private final SelenideElement menu = $("[role='menu']");

    @Step("Перейти к странице друзей")
    public FriendsPage toFriendsPage() {
        header.$("[aria-label='Menu']").click();
        menu.$(byText("Friends")).click();
        return new FriendsPage();
    }

    @Step("Перейти к странице всех людей")
    public PeoplePage toAllPeoplesPage() {
        header.$("[aria-label='Menu']").click();
        menu.$(byText("All people")).click();
        return new PeoplePage();
    }

    @Step("Перейти к странице профиля")
    public ProfilePage toProfilePage() {
        header.$("[aria-label='Menu']").click();
        menu.$(byText("Profile")).click();
        return new ProfilePage();
    }

    @Step("Разлогиниться")
    public LoginPage signOut() {
        header.$("[aria-label='Menu']").click();
        menu.$(byText("Sign out")).click();
        return new LoginPage();
    }

    @Step("Добавить новую трату")
    public EditSpendingPage addSpendingPage() {
        header.$(byText("New spending")).click();
        return new EditSpendingPage();
    }

    @Step("Вернуться на главную страницу")
    public MainPage toMainPage() {
        header.$(".MuiToolbar-gutters").click();
        return new MainPage();
    }
}
