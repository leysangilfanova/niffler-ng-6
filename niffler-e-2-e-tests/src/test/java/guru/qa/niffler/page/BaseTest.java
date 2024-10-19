package guru.qa.niffler.page;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.component.Calendar;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.SearchField;

public class BaseTest {
    public static final Config CFG = Config.getInstance();
    protected final Header header;
    protected final Calendar calendar;
    protected final SearchField searchField;

    public BaseTest() {
        this.header = new Header();
        this.calendar = new Calendar();
        this.searchField = new SearchField();
    }

    public Header getHeader() {
        return header;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public SearchField getSearchField() {
        return searchField;
    }
}
