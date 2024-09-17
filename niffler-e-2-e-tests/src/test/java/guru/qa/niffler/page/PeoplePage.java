package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.DisplayName;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class PeoplePage {

  private final SelenideElement peopleTable = $("#all").as("список людей и отправленных заявок в друзбя");

  @DisplayName("Проверка того, что у пользователя есть отправленная заявка в друзья")
  public PeoplePage invitationSentToUserCheck(String username) {
    SelenideElement friendRow = peopleTable.$$("tr").find(text(username));
    friendRow.shouldHave(text("Waiting..."));
    return this;
  }
}
