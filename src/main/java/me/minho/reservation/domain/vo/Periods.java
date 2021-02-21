package me.minho.reservation.domain.vo;

import java.time.temporal.Temporal;
import java.util.Collections;
import java.util.List;

public class Periods <T extends Temporal> {
    private final List<Period<T>> periods;

    private Periods (List<Period<T>> periods) {
        this.periods = Collections.unmodifiableList(periods);
    }

    public static <T extends Temporal> Periods<T> of(List<Period<T>> periods) {
        return new Periods<>(periods);
    }

    public boolean contains(T reservationTime) {
        return periods.stream().anyMatch(period -> period.getStart().equals(reservationTime));
    }
}
