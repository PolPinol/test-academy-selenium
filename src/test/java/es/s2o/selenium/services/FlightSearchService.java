package es.s2o.selenium.services;

import es.s2o.selenium.domain.FlightSearchDTO;
import es.s2o.selenium.pages.VuelingSearchPage;
import net.serenitybdd.core.Serenity;
import net.thucydides.model.environment.SystemEnvironmentVariables;
import net.thucydides.model.util.EnvironmentVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlightSearchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlightSearchService.class);
    private final VuelingSearchPage vuelingSearchPage;

    public FlightSearchService() {
        this.vuelingSearchPage = new VuelingSearchPage();
    }

    public void openSearchPage() {
        EnvironmentVariables variables = SystemEnvironmentVariables.createEnvironmentVariables();
        String baseUrl = variables.getProperty("WEB_ROOT");

        LOGGER.info("Opening Vueling search page at URL: {}", baseUrl);

        vuelingSearchPage.openAt(baseUrl);
        Serenity.takeScreenshot();

        vuelingSearchPage.acceptCookiesIfPresent();
        Serenity.takeScreenshot();
    }

    public void searchFlights(FlightSearchDTO search) {
        LOGGER.info("Initiating flight search: Origin='{}', Destination='{}', Departure Date='{}', Passengers={}",
                search.origin(), search.destination(), search.departureDate(), search.passengers());

        vuelingSearchPage.setOrigin(search.origin());
        Serenity.takeScreenshot();

        vuelingSearchPage.setDestination(search.destination());
        Serenity.takeScreenshot();

        vuelingSearchPage.selectOneWayTrip();
        Serenity.takeScreenshot();

        vuelingSearchPage.selectDateInCalendar(search.departureDate());
        Serenity.takeScreenshot();

        vuelingSearchPage.clickSearch();
        Serenity.takeScreenshot();

        vuelingSearchPage.switchToNewWindow();
        Serenity.takeScreenshot();
    }

    public boolean hasFlightResults() {
        boolean results = vuelingSearchPage.hasResults();
        LOGGER.info("Flight search results found: {}", results);
        return results;
    }
}
