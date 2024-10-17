package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.BaseTest;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.service.UsersDbClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.utils.RandomDataUtils.randomName;
import static guru.qa.niffler.utils.RandomDataUtils.randomPassword;
import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

@WebTest
public class ProfileTest extends BaseTest {
    String userData = "kisa";

    @Test
    @DisplayName("После удачной авторизации показывается главная страница")
    void mainPageShouldBeDisplayedAfterSuccessLogin() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(userData, userData)
                .mainPageAfterLoginCheck();
    }

    @Test
    @DisplayName("После неудачной авторизации показывается ошибка")
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .auth(userData, "kiss")
                .loginErrorCheck("Bad credentials");
    }

    @DisplayName("Архивация категории")
    @User(username = "risa", categories = @Category(archived = false))
    @Test
    void archivingCategoryTest(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("risa", "risa")
                .openProfile()
                .clickArchiveButton()
                .confirmArchiving()
                .successArchiveTooltipCheck("Category " + category.name() + " is archived");
    }

    @DisplayName("Разархивация категории")
    @User(username = "misa", categories = @Category(archived = true))
    @Test
    void unArchivingCategoryTest(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("misa", "misa")
                .openProfile()
                .clickArchiveSwitcher()
                .categoryInListCheck(category.name())
                .clickUnArchiveButton()
                .confirmArchiving()
                .categoryNotInListCheck(category.name());
    }

    @DisplayName("Активная категория отображается в списке активных категорий")
    @User(username = "nisa", categories = @Category(archived = false))
    @Test
    void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("nisa", "nisa")
                .openProfile()
                .categoryInListCheck(category.name());
    }

    @User(username = "lisa", categories = @Category(archived = true))
    @Test
    @DisplayName("Архивная категория отображается в списке архивных категорий")
    void archivedCategoryShouldPresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("lisa", "lisa")
                .openProfile()
                .clickArchiveSwitcher()
                .categoryInListCheck(category.name());
    }

    @Test
    @DisplayName("Проверка сохранения имени пользователя")
    void setNameTest() {
        UsersDbClient usersDbClient = new UsersDbClient();
        String username = randomUsername();
        String password = randomPassword();
        String newName = randomName();

        usersDbClient.createUser(username, password);

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(username, password);

        new Header().toProfilePage()
                .setName(newName)
                .checkName(newName);
    }
}

