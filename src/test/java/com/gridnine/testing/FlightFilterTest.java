package com.gridnine.testing;

import org.junit.Assert;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class FlightFilterTest {

    //exclude flights with a total flight duration of more than 4 hours
    @Test
    public void whenFlightDurationOfMoreThan4hoursThenResultIs2() {
        LocalDateTime now = LocalDateTime.now();
        Flight flight1 = new Flight(List.of(
                new Segment(now, now.plusHours(2))
        ));
        Flight flight2 = new Flight(List.of(
                new Segment(now, now.plusHours(3)),
                new Segment(now.plusHours(4), now.plusHours(6))
        ));
        Flight flight3 = new Flight(List.of(
                new Segment(now, now.plusHours(6))
        ));
        Flight flight4 = new Flight(List.of(
                new Segment(now.minusHours(3), now)
        ));
        List<Flight> flights = List.of(flight1, flight2, flight3, flight4);
        FlightFilter flightFilter = new FlightFilter();
        List<Flight> result = flightFilter.filter(flights, flight -> {
            int totalDuration = 0;
            List<Segment> segments = flight.getSegments();
            for (Segment segment : segments) {
                totalDuration += Duration.between(segment.getDepartureDate(), segment.getArrivalDate()).toMillis();
            }
            return totalDuration > 4 * 60 * 60 * 1000;
        });
        Assert.assertEquals(result.size(), 2);
        Assert.assertTrue(result.contains(flight1));
        Assert.assertTrue(result.contains(flight4));
    }

    //exclude flights with more than one transfer
    @Test
    public void whenFlightWithMoreThen1transferThenResultIs3() {
        LocalDateTime now = LocalDateTime.now();
        Flight flight1 = new Flight(List.of(
                new Segment(now, now.plusHours(2)),
                new Segment(now.plusHours(4), now.plusHours(5)),
                new Segment(now.plusHours(6), now.plusHours(9))));
        Flight flight2 = new Flight(List.of(
                new Segment(now, now.plusHours(1)),
                new Segment(now.plusHours(2), now.plusHours(6))
        ));
        Flight flight3 = new Flight(List.of(
                new Segment(now, now.plusHours(2))
        ));
        Flight flight4 = new Flight(List.of(
                new Segment(now.minusHours(4), now)
        ));
        Flight flight5 = new Flight(List.of(
                new Segment(now.minusHours(3), now),
                new Segment(now.plusHours(1), now.plusHours(3)),
                new Segment(now.plusHours(4), now.plusHours(7))));
        List<Flight> flights = List.of(flight1, flight2, flight3, flight4, flight5);
        FlightFilter flightFilter = new FlightFilter();
        List<Flight> result = flightFilter.filter(flights, flight -> flight.getSegments().size() > 2);
        Assert.assertEquals(result.size(), 3);
        Assert.assertTrue(result.contains(flight2));
        Assert.assertTrue(result.contains(flight3));
        Assert.assertTrue(result.contains(flight4));
    }

}