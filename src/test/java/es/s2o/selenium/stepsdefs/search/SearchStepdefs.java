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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class SearchStepdefs {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchStepdefs.class);

    @Steps
    private FlightSearchService flightSearchService;

    @Before
    public void setUp() {
        LOGGER.info("Setting up the scenario.");
    }

    @After
    public void tearDown() {
        LOGGER.info("Tearing down the scenario.");
    }

    @Given("I'm on the Vueling flight search page")
    public void iMOnTheVuelingFlightSearchPage() {
        LOGGER.info("Navigating to the Vueling flight search page.");
        flightSearchService.openSearchPage();
    }

    @When("I enter the following flight search details:")
    public void iEnterTheFollowingFlightSearchDetails(DataTable dataTable) {
        LOGGER.info("Processing flight search details from data table.");
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        if (rows.isEmpty()) {
            LOGGER.error("No flight search details provided in the data table.");
            throw new IllegalArgumentException("Data table must contain at least one row with search details.");
        }

        Map<String, String> row = rows.get(0);
        LocalDate departureDate = LocalDate.parse(row.get("departureDate"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        FlightSearchDTO search = FlightSearchDTO.builder()
                .origin(row.get("origin"))
                .destination(row.get("destination"))
                .departureDate(departureDate)
                .passengers(Integer.parseInt(row.get("passengers")))
                .build();

        LOGGER.info("Initiating search with details: {}", search);
        flightSearchService.searchFlights(search);
    }

    @Then("I should see available flights in the results list")
    public void iShouldSeeAvailableFlightsInTheResultsList() {
        LOGGER.info("Verifying that available flights are displayed.");
        boolean resultsDisplayed = flightSearchService.hasFlightResults();
        assertTrue("Expected to see flight results displayed", resultsDisplayed);
        LOGGER.info("Flight results are displayed as expected.");
    }
}
