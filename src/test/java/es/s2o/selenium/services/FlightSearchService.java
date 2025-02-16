package es.s2o.selenium.services;


import es.s2o.selenium.domain.FlightSearchDTO;
import es.s2o.selenium.pages.VuelingSearchPage;
import net.thucydides.model.environment.SystemEnvironmentVariables;
import net.thucydides.model.util.EnvironmentVariables;

public class FlightSearchService {

    private final VuelingSearchPage vuelingSearchPage;

    public FlightSearchService() {
        this.vuelingSearchPage = new VuelingSearchPage();
    }

    public void openSearchPage() {
        EnvironmentVariables variables = SystemEnvironmentVariables.createEnvironmentVariables();
        String baseUrl = variables.getProperty("WEB_ROOT");
        vuelingSearchPage.openAt(baseUrl);
        vuelingSearchPage.acceptCookiesIfPresent();
    }

    public void searchFlights(FlightSearchDTO search) {
        vuelingSearchPage.setOrigin(search.origin());
        vuelingSearchPage.setDestination(search.destination());

        vuelingSearchPage.selectOneWayTrip();

        vuelingSearchPage.selectDateInCalendar(search.departureDate());

        vuelingSearchPage.clickSearch();

        switchToNewWindow();
    }

    public boolean hasFlightResults() {
        return vuelingSearchPage.hasResults();
    }

    private void switchToNewWindow() {
        String currentWindow = vuelingSearchPage.getDriver().getWindowHandle();
        for (String handle : vuelingSearchPage.getDriver().getWindowHandles()) {
            if (!handle.equals(currentWindow)) {
                vuelingSearchPage.getDriver().switchTo().window(handle);
                break;
            }
        }
    }
}
