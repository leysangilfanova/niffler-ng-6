package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.textsInAnyOrder;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class FriendsPage {

    private final ElementsCollection friendsList = $$("#friends tr").as("список друзей");
    private final ElementsCollection requestsList = $$("#requests tr").as("список заявок в друзья");
    private final SelenideElement allPeopleBtn = $(byText("All people")).as("кнопка 'All people'");
    private final SelenideElement searchInput =  $("input[type='text']").as("инпут поиска");
    private final SelenideElement acceptBtn = $(byText("Accept")).as("кнопка 'Accept'");
    private final SelenideElement declineBtn = $(byText("Decline")).as("кнопка 'Decline'");
    private final SelenideElement unfriendBtn = $(byText("Unfriend")).as("кнопка 'Unfriend'");
    private final SelenideElement declineBtnInActionMenu =
            $(".MuiDialogActions-spacing [type='button']:nth-child(2)").as("кнопка 'Decline' в диалоговом окне");

    @Step("Проверка того, что у пользователя есть добавленные друзья")
    public FriendsPage existingFriendsCheck(String username) {
        makeFriendSearch(username);
        friendsList.shouldHave(textsInAnyOrder(username));
        return this;
    }

    @Step("Проверка того, что у пользователя нет друзей")
    public FriendsPage noExistingFriendsCheck() {
        friendsList.shouldHave(size(0));
        return this;
    }

    @Step("Проверка того, что у пользователя есть заявка в друзья")
    public FriendsPage invitationsToFriendsCheck(String username) {
        makeFriendSearch(username);
        requestsList.shouldHave(textsInAnyOrder(username));
        return this;
    }

    @Step("Кликнуть по кнопке 'AllPeople'")
    public PeoplePage clickAllPeopleBtn() {
        allPeopleBtn.click();
        return new PeoplePage();
    }

    @Step("Осуществить поиск друга")
    public FriendsPage makeFriendSearch(String friendName) {
        searchInput.sendKeys(friendName);
        searchInput.sendKeys(Keys.ENTER);
        return new FriendsPage();
    }

    @Step("Принять заявку в друзья")
    public FriendsPage acceptFriend() {
        acceptBtn.click();
        return this;
    }

    @Step("Отклонить заявку в друзья")
    public FriendsPage declineFriend() {
        declineBtn.click();
        declineBtnInActionMenu.click();
        return this;
    }

    @Step("Кнопка 'Unfriend' отображается")
    public FriendsPage unfriendBtnIsVisibleCheck() {
        unfriendBtn.shouldBe(visible);
        return this;
    }
}
