package es.s2o.selenium.services;

import es.s2o.selenium.domain.FlightSearchDTO;
import es.s2o.selenium.pages.VuelingSearchPage;
import net.serenitybdd.annotations.Step;
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

    @Step("Open Vueling search page")
    public void openSearchPage() {
        EnvironmentVariables variables = SystemEnvironmentVariables.createEnvironmentVariables();
        String baseUrl = variables.getProperty("WEB_ROOT");
        LOGGER.info("Opening Vueling search page at URL: {}", baseUrl);
        vuelingSearchPage.openAt(baseUrl);
        vuelingSearchPage.acceptCookiesIfPresent();
    }

    @Step("Search flights from {0} to {1} departing on {2}")
    public void searchFlights(FlightSearchDTO search) {
        LOGGER.info("Initiating flight search: Origin='{}', Destination='{}', Departure Date='{}', Passengers={}",
                search.origin(), search.destination(), search.departureDate(), search.passengers());
        vuelingSearchPage.setOrigin(search.origin());
        vuelingSearchPage.setDestination(search.destination());
        vuelingSearchPage.selectOneWayTrip();
        vuelingSearchPage.selectDateInCalendar(search.departureDate());
        vuelingSearchPage.clickSearch();

        vuelingSearchPage.switchToNewWindow();
    }

    @Step("Verify flight search results are present")
    public boolean hasFlightResults() {
        boolean results = vuelingSearchPage.hasResults();
        LOGGER.info("Flight search results found: {}", results);
        return results;
    }
}
