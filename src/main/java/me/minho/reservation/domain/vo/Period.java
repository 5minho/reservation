package me.minho.reservation.domain.vo;


import javax.persistence.Embeddable;
import java.time.temporal.Temporal;
import java.util.Objects;

@Embeddable
public class Period <T extends Temporal> {
    private T start;
    private T end;

    protected Period() {}

    private Period(T start, T end) {
        this.start = start;
        this.end = end;
    }

    public static <T extends Temporal & Comparable<?>> Period<T> between(T start, T end) {
        return new Period<>(start, end);
    }

    public T getStart() {
        return start;
    }

    public T getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "Period{" + "start=" + start + ", end=" + end + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Period<?> period = (Period<?>) o;
        return Objects.equals(getStart(), period.getStart()) && Objects.equals(getEnd(), period.getEnd());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStart(), getEnd());
    }
}
