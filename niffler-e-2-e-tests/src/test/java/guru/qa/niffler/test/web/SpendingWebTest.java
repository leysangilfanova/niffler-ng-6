package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.EditSpendingPage;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.component.SpendingTable;
import guru.qa.niffler.service.UsersDbClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.page.BaseTest.CFG;
import static guru.qa.niffler.utils.RandomDataUtils.randomPassword;
import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

@WebTest
public class SpendingWebTest {
    @User(
            username = "kisa",
            spendings = @Spending(
                    category = "Обучение",
                    description = "Обучение Advanced 2.0",
                    amount = 79990
            )
    )

    @Test
    void categoryDescriptionShouldBeChangedFromTable(SpendJson[] spends) {
        SpendJson spend = spends[0];
        final String newDescription = "Обучение Niffler Next Generation";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("kisa", "kisa")
                .editSpending(spend.description())
                .setNewSpendingDescription(newDescription)
                .save();

        new MainPage().checkThatTableContainsSpending(newDescription);
    }

    @Test
    void spendCheck() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("kisa", "kisa")
                .checkThatTableContainsSpending("Еда");
    }

    @User(
            username = "nisa",
            spendings = @Spending(
                    category = "kek",
                    description = "lol",
                    amount = 79990
            )
    )
    @Test
    void componentTest(SpendJson[] spends) {
        SpendJson spend = spends[0];

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("nisa", "nisa");

        new SpendingTable().selectPeriod("Today")
                .checkTableContains(spend.description())
                .searchSpendingByDescription(spend.description())
                .checkTableSize(1)
                .deleteSpending(spend.description());
    }

    @Test
    @DisplayName("Создание траты")
    void addSpendTest() {
        UsersDbClient usersDbClient = new UsersDbClient();
        String username = randomUsername();
        String password = randomPassword();
        usersDbClient.createUser(username, password);

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(username, password);

        new EditSpendingPage()
                .getHeader()
                .addSpendingPage()
                .setSpendingCategory("kokoko")
                .setNewSpendingDescription("test")
                .setSpendingAmount("10")
                .getCalendar()
                .selectDateInCalendar("3.September.2015");

        new EditSpendingPage().save();

        new MainPage().checkThatTableContainsSpending("test");
    }
}
