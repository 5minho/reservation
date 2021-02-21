package me.minho.reservation.domain.vo;

import javax.persistence.Embeddable;

@Embeddable
public class Period <T> {
    private T start;
    private T end;

    protected Period() {}

    private Period(T start, T end) {
        this.start = start;
        this.end = end;
    }

    public static <T> Period<T> of (T start, T end) {
        return new Period<>(start, end);
    }
}
