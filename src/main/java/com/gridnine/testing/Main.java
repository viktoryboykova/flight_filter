package com.gridnine.testing;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<Flight> testFlights = FlightBuilder.createFlights();
        FlightFilter flightFilter = new FlightFilter();
        System.out.println("Список полетов до фильтрации: ");
        testFlights.forEach(System.out::println);
        System.out.println("Исключаем полеты с вылетом до текущего момента времени: ");
        LocalDateTime now = LocalDateTime.now();
        flightFilter.filter(testFlights, flight -> flight.getSegments().stream().anyMatch(s -> s.getDepartureDate().isBefore(now)))
                .forEach(System.out::println);
        System.out.println("Исключаем полеты, где имеются сегменты с датой прилёта раньше даты вылета: ");
        flightFilter.filter(testFlights, flight -> flight.getSegments().stream().anyMatch(s -> s.getDepartureDate().isAfter(s.getArrivalDate())))
                .forEach(System.out::println);
        System.out.println("Исключаем полеты, где общее время, проведённое на земле превышает два часа: ");
        flightFilter.filter(testFlights, flight -> {
                            int transfer = 0;
                            List<Segment> segments = flight.getSegments();
                            for (int i = 0; i < segments.size() - 1; i++) {
                                transfer += Duration.between(segments.get(i).getArrivalDate(), segments.get(i + 1).getDepartureDate()).toMillis();
                            }
                            return transfer > 2 * 60 * 60 * 1000;
                        }
                )
                .forEach(System.out::println);
    }
}