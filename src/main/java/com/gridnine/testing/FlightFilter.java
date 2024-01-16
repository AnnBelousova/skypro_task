package com.gridnine.testing;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FlightFilter {
    public List<Flight> filteringFlights(List<Flight> listFlight, List<Predicate<Flight>> rules) {
        Predicate<Flight> result = rules.stream().reduce(p -> true, Predicate::and);
        return listFlight.stream().filter((result)).collect(Collectors.toList());
    }
}
