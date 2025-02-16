package es.s2o.selenium.pages;

import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.FluentWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VuelingSearchPage extends PageObjectBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(VuelingSearchPage.class);

    private final By ORIGIN_INPUT               = By.id("originInput");
    private final By ORIGIN_BUTTON              = By.cssSelector(".cities-sugestion-popup_main");
    private final By DESTINATION_INPUT          = By.id("destinationInput");
    private final By DESTINATION_BUTTON         = By.cssSelector(".cities-sugestion-popup_main");
    private final By ONE_WAY_TOGGLE             = By.cssSelector(".vy-switch_button");
    private final By CALENDAR_MONTH_HEADER      = By.cssSelector(".ui-datepicker-month");
    private final By CALENDAR_YEAR_HEADER       = By.cssSelector(".ui-datepicker-year");
    private final By NEXT_MONTH_BUTTON          = By.id("nextButtonCalendar");
    private final By SEARCH_BUTTON              = By.id("btnSubmitHomeSearcher");
    private final By ACCEPT_COOKIES_BUTTON      = By.id("onetrust-accept-btn-handler");
    private final By FLIGHT_CARD_SELECTOR       = By.cssSelector(".trip-selector_item");
    private final By NEW_FLIGHT_CARD_SELECTOR   = By.cssSelector(".vy-flight-journey");

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

    /**
     * Functional interface for passing a custom condition to wait for.
     */
    @FunctionalInterface
    private interface CheckCondition {
        boolean isSatisfied();
    }

    /**
     * A reusable method to wait for a given condition to be satisfied using FluentWait.
     * Returns true if the condition is met within the timeout, otherwise false.
     */
    private boolean waitForCondition(CheckCondition condition, int timeoutInSeconds) {
        try {
            return new FluentWait<>(getDriver())
                    .withTimeout(Duration.ofSeconds(timeoutInSeconds))
                    .pollingEvery(Duration.ofMillis(250))
                    .ignoring(Exception.class)
                    .until(driver -> condition.isSatisfied());
        } catch (TimeoutException e) {
            LOGGER.debug("Condition was not met within {} seconds: {}", timeoutInSeconds, e.getMessage());
            return false;
        }
    }

    /**
     * Sets the origin value and selects it.
     */
    public void setOrigin(String origin) {
        boolean success = waitForCondition(() -> {
            try {
                WebElementFacade originInput = $(ORIGIN_INPUT).waitUntilVisible();
                originInput.clear();
                originInput.sendKeys(origin);
                $(ORIGIN_BUTTON).waitUntilClickable().click();
                resetDocument();
                return true;
            } catch (Exception e) {
                LOGGER.debug("Could not set origin: {}", e.getMessage());
                return false;
            }
        }, 10);

        if (!success) {
            throw new RuntimeException("Unable to set origin within timeout.");
        }
    }

    /**
     * Sets the destination value and selects it.
     */
    public void setDestination(String destination) {
        boolean success = waitForCondition(() -> {
            try {
                WebElementFacade destinationInput = $(DESTINATION_INPUT).waitUntilVisible();
                destinationInput.clear();
                destinationInput.sendKeys(destination);
                $(DESTINATION_BUTTON).waitUntilClickable().click();
                resetDocument();
                return true;
            } catch (Exception e) {
                LOGGER.debug("Could not set destination: {}", e.getMessage());
                return false;
            }
        }, 10);

        if (!success) {
            throw new RuntimeException("Unable to set destination within timeout.");
        }
    }

    /**
     * Selects the one-way trip option.
     */
    public void selectOneWayTrip() {
        boolean success = waitForCondition(() -> {
            try {
                $(ONE_WAY_TOGGLE).waitUntilClickable().click();
                resetDocument();
                return true;
            } catch (Exception e) {
                LOGGER.debug("One-way toggle not clickable yet: {}", e.getMessage());
                return false;
            }
        }, 10);

        if (!success) {
            throw new RuntimeException("Unable to select one-way trip within timeout.");
        }
    }

    /**
     * Selects a date in the calendar with retries.
     */
    public void selectDateInCalendar(LocalDate desiredDate) {
        boolean dateSelected = waitForCondition(() -> {
            try {
                String monthText = $(CALENDAR_MONTH_HEADER).waitUntilVisible().getText().toUpperCase().trim();
                String yearText = $(CALENDAR_YEAR_HEADER).waitUntilVisible().getText().trim();
                LOGGER.debug("Calendar shows: {} {}", monthText, yearText);

                Month monthShown = SPANISH_TO_MONTH.get(monthText);
                int yearShown = Integer.parseInt(yearText);

                if (monthShown.getValue() == desiredDate.getMonthValue() && yearShown == desiredDate.getYear()) {
                    int zeroBasedMonth = desiredDate.getMonthValue() - 1;
                    String dayXPath = String.format(
                            "//td[@data-year='%d' and @data-month='%d']//a[normalize-space(text())='%d']",
                            desiredDate.getYear(),
                            zeroBasedMonth,
                            desiredDate.getDayOfMonth()
                    );

                    WebElementFacade dayElement = $(By.xpath(dayXPath)).waitUntilClickable();
                    dayElement.click();
                    resetDocument();
                    return true;
                } else {
                    if (yearShown < desiredDate.getYear() ||
                            (yearShown == desiredDate.getYear() && monthShown.getValue() < desiredDate.getMonthValue())) {
                        LOGGER.debug("Desired date is ahead; clicking next month button.");
                        $(NEXT_MONTH_BUTTON).waitUntilClickable().click();
                        resetDocument();
                    } else {
                        LOGGER.warn("Desired date {} is before the current calendar view {} {}. Check the date or calendar logic.",
                                desiredDate, monthText, yearText);
                    }
                    return false;
                }
            } catch (Exception e) {
                LOGGER.debug("Exception while selecting date: {}", e.getMessage());
                return false;
            }
        }, 15);

        if (!dateSelected) {
            throw new RuntimeException("Unable to select the desired date " + desiredDate);
        }
    }

    /**
     * Clicks the search button with a retry approach.
     */
    public void clickSearch() {
        boolean clicked = waitForCondition(() -> {
            try {
                $(SEARCH_BUTTON).waitUntilClickable().click();
                resetDocument();
                return true;
            } catch (Exception e) {
                LOGGER.debug("Search button not clickable yet: {}", e.getMessage());
                return false;
            }
        }, 10);

        if (!clicked) {
            throw new RuntimeException("Unable to click the search button within 10 seconds.");
        }
    }

    /**
     * Checks if there are any flight results, waiting up to 15 seconds to handle slow loading.
     */
    public boolean hasResults() {
        return waitForCondition(() -> {
            List<WebElementFacade> oldResults = findAll(FLIGHT_CARD_SELECTOR);
            List<WebElementFacade> newResults = findAll(NEW_FLIGHT_CARD_SELECTOR);

            LOGGER.debug("Found {} elements using old locator.", oldResults.size());
            LOGGER.debug("Found {} elements using new locator.", newResults.size());

            return !oldResults.isEmpty() || !newResults.isEmpty();
        }, 15);
    }

    /**
     * Accepts cookies if the accept button is present.
     */
    public void acceptCookiesIfPresent() {
        try {
            WebElementFacade acceptButton = $(ACCEPT_COOKIES_BUTTON)
                    .withTimeoutOf(Duration.ofSeconds(10))
                    .waitUntilVisible();

            if (acceptButton.isCurrentlyVisible()) {
                LOGGER.debug("Accepting cookies.");
                acceptButton.click();
                resetDocument();
            }
        } catch (Exception e) {
            LOGGER.info("Cookies accept button not found, continuing without accepting cookies.");
        }
    }
}
