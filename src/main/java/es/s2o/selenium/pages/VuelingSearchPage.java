package es.s2o.selenium.pages;

import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.By;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

public class VuelingSearchPage extends PageObjectBase {

    private final By ORIGIN_INPUT           = By.id("originInput");
    private final By ORIGIN_BUTTON          = By.cssSelector(".cities-sugestion-popup_main");
    private final By DESTINATION_INPUT      = By.id("destinationInput");
    private final By DESTINATION_BUTTON     = By.cssSelector(".cities-sugestion-popup_main");
    private final By ONE_WAY_TOGGLE         = By.cssSelector(".vy-switch_button");
    private final By CALENDAR_MONTH_HEADER  = By.cssSelector(".ui-datepicker-month");
    private final By CALENDAR_YEAR_HEADER   = By.cssSelector(".ui-datepicker-year");
    private final By NEXT_MONTH_BUTTON      = By.id("nextButtonCalendar");
    private final By SEARCH_BUTTON          = By.id("btnSubmitHomeSearcher");
    private final By ACCEPT_COOKIES_BUTTON  = By.id("onetrust-accept-btn-handler");
    private final By FLIGHT_CARD_SELECTOR   = By.cssSelector(".trip-selector_item");


    private static final Map<String, Month> SPANISH_TO_MONTH = new HashMap<>();

    static {
        SPANISH_TO_MONTH.put("ENERO", Month.JANUARY);
        SPANISH_TO_MONTH.put("FEBRERO", Month.FEBRUARY);
        SPANISH_TO_MONTH.put("MARZO", Month.MARCH);
        SPANISH_TO_MONTH.put("ABRIL", Month.APRIL);
        SPANISH_TO_MONTH.put("MAYO", Month.MAY);
        SPANISH_TO_MONTH.put("JUNIO", Month.JUNE);
        SPANISH_TO_MONTH.put("JULIO", Month.JULY);
        SPANISH_TO_MONTH.put("AGOSTO", Month.AUGUST);
        SPANISH_TO_MONTH.put("SEPTIEMBRE", Month.SEPTEMBER);
        SPANISH_TO_MONTH.put("OCTUBRE", Month.OCTOBER);
        SPANISH_TO_MONTH.put("NOVIEMBRE", Month.NOVEMBER);
        SPANISH_TO_MONTH.put("DICIEMBRE", Month.DECEMBER);
    }

    public void setOrigin(String origin) {
        $(ORIGIN_INPUT).clear();
        $(ORIGIN_INPUT).sendKeys(origin);
        $(ORIGIN_BUTTON).click();
        resetDocument();
    }

    public void setDestination(String destination) {
        $(DESTINATION_INPUT).clear();
        $(DESTINATION_INPUT).sendKeys(destination);
        $(DESTINATION_BUTTON).click();
        resetDocument();
    }

    public void selectOneWayTrip() {
        $(ONE_WAY_TOGGLE)
                .waitUntilClickable()
                .click();
        resetDocument();
    }

    public void selectDateInCalendar(LocalDate desiredDate) {
        while (true) {
            String monthText = $(CALENDAR_MONTH_HEADER).getText().toUpperCase().trim(); // e.g. "FEBRERO"
            String yearText = $(CALENDAR_YEAR_HEADER).getText().trim();                 // e.g. "2025"

            System.out.println("Calendar shows: " + monthText + " " + yearText);

            Month monthShown = SPANISH_TO_MONTH.get(monthText);
            int yearShown = Integer.parseInt(yearText);

            if (monthShown.getValue() == desiredDate.getMonthValue()
                    && yearShown == desiredDate.getYear()) {

                int zeroBasedMonth = desiredDate.getMonthValue() - 1;

                String dayXPath = String.format(
                        "//td[@data-year='%d' and @data-month='%d']//a[normalize-space(text()[1])='%d']",
                        desiredDate.getYear(),
                        zeroBasedMonth,
                        desiredDate.getDayOfMonth()
                );

                $(By.xpath(dayXPath)).waitUntilClickable().click();
                resetDocument();
                break;
            } else {
                if (yearShown < desiredDate.getYear()
                        || (yearShown == desiredDate.getYear()
                        && monthShown.getValue() < desiredDate.getMonthValue())) {
                    $(NEXT_MONTH_BUTTON).click();
                }
                resetDocument();
            }
        }
    }

    public void clickSearch() {
        $(SEARCH_BUTTON).click();
        resetDocument();
    }

    public boolean hasResults() {
        return !findAll(FLIGHT_CARD_SELECTOR).isEmpty();
    }

    public void acceptCookiesIfPresent() {
        WebElementFacade acceptButton = $(ACCEPT_COOKIES_BUTTON)
                .withTimeoutOf(Duration.ofSeconds(10))
                .waitUntilVisible();

        if (acceptButton.isCurrentlyVisible()) {
            acceptButton.click();
            resetDocument();
        }
    }
}
