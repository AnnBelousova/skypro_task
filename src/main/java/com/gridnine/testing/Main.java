package com.gridnine.testing;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.Predicate;

public class Main {
    public static void main(String[] args) {
        List<Flight> flightList = FlightBuilder.createFlights();
        Predicate<Flight> severalSegments = flight -> flight.getSegments().size() > 1;
        LocalDateTime currentTime = LocalDateTime.now();

        Predicate<Flight> flightsArrivalBeforeDepartureTime = flight -> flight
                .getSegments()
                .stream()
                .allMatch(segment -> segment.getArrivalDate().isBefore(segment.getArrivalDate()));

        Predicate<Flight> flightsDepartureBeforeCurrentTime = flight -> flight
                .getSegments()
                .stream()
                .anyMatch(segment -> segment.getDepartureDate().isBefore(currentTime));

        Predicate<Flight> timeBetweenSegmentsMoreThanTwoHours = flight -> {
            long totalMinutes = 0;
            for (int i = 0; i < flight.getSegments().size()-1; i++) {
                totalMinutes += Math.abs(ChronoUnit.MINUTES.between(flight.getSegments().get(i+1).getDepartureDate(), flight.getSegments().get(i).getArrivalDate()));
            }
            return totalMinutes/60 > 2;
        };
        FlightFilter flightFilter = new FlightFilter();

        List<Flight> filteredFlightsBeforeCurTime = flightFilter.filteringFlights(flightList, List.of(flightsDepartureBeforeCurrentTime));
        List<Flight> filteredFlightsArrivalBeforeDeparture = flightFilter.filteringFlights(flightList, List.of(flightsArrivalBeforeDepartureTime));
        List<Flight> filteredFlightsWaitingTimeMoreTwoHours = flightFilter.filteringFlights(flightList, List.of(timeBetweenSegmentsMoreThanTwoHours, flightsArrivalBeforeDepartureTime.negate(), severalSegments));

        System.out.println("Flight list:\n" + flightList);
        System.out.println("\nFlight list departure before current time:\n " + filteredFlightsBeforeCurTime);
        System.out.println("\nSegments with date before departure time:\n" + filteredFlightsArrivalBeforeDeparture + "\n");
        System.out.println("\nTotal time between segments is on the ground mere thar 2 hours:\n" + filteredFlightsWaitingTimeMoreTwoHours);
    }
}