package com.gridnine.testing;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public class FlightFilter implements Filter<Flight> {
    @Override
    public List<Flight> filter(List<Flight> flights, Predicate<Flight> rule) {
        List<Flight> result = new LinkedList<>();
        for (Flight flight : flights) {
            if (!rule.test(flight)) {
                result.add(flight);
            }
        }
        return result;
    }
}
