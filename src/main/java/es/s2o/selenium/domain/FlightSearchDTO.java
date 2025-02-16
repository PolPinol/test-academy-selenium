package es.s2o.selenium.domain;

import java.time.LocalDate;

/**
 * A record representing flight search parameters.
 * Includes a static inner class for a builder-like approach.
 */
public record FlightSearchDTO(
        String origin,
        String destination,
        LocalDate departureDate,
        int passengers
) {

    /**
     * Static method to create a new builder instance.
     */
    public static FlightSearchBuilder builder() {
        return new FlightSearchBuilder();
    }

    /**
     * A static inner class that provides a builder API for creating FlightSearch records.
     */
    public static class FlightSearchBuilder {
        private String origin;
        private String destination;
        private LocalDate departureDate;
        private int passengers = 1;

        public FlightSearchBuilder origin(String origin) {
            this.origin = origin;
            return this;
        }

        public FlightSearchBuilder destination(String destination) {
            this.destination = destination;
            return this;
        }

        public FlightSearchBuilder departureDate(LocalDate departureDate) {
            this.departureDate = departureDate;
            return this;
        }

        public FlightSearchBuilder passengers(int passengers) {
            this.passengers = passengers;
            return this;
        }

        /**
         * Builds an immutable FlightSearch record instance.
         */
        public FlightSearchDTO build() {
            return new FlightSearchDTO(origin, destination, departureDate, passengers);
        }
    }
}
