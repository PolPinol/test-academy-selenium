package es.s2o.selenium.stepsdefs.search;

import es.s2o.selenium.domain.FlightSearchDTO;
import es.s2o.selenium.services.FlightSearchService;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.annotations.Steps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class SearchStepdefs {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Steps
    FlightSearchService flightSearchService;

    @Before
    public void beforeScenario() {
        LOGGER.debug("beforeScenario starts");
    }

    @After
    public void afterScenario() {
        LOGGER.debug("afterScenario starts");
    }

    @Given("I'm on the Vueling flight search page")
    public void iMOnTheVuelingFlightSearchPage() {
        flightSearchService.openSearchPage();
    }

    @When("I enter the following flight search details:")
    public void iEnterTheFollowingFlightSearchDetails(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        Map<String, String> row = rows.get(0);

        LocalDate date = LocalDate.parse(row.get("departureDate"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        FlightSearchDTO search = FlightSearchDTO.builder()
                .origin(row.get("origin"))
                .destination(row.get("destination"))
                .departureDate(date)
                .passengers(Integer.parseInt(row.get("passengers")))
                .build();

        flightSearchService.searchFlights(search);
    }

    @Then("I should see available flights in the results list")
    public void iShouldSeeAvailableFlightsInTheResultsList() {
        boolean resultsDisplayed = flightSearchService.hasFlightResults();
        assertTrue("Expected to see flight results displayed", resultsDisplayed);
    }
}
