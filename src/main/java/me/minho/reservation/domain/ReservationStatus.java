package me.minho.reservation.domain;

public enum ReservationStatus {
    READY,
    COMPLETED,
    CANCELED;

    boolean cannotCancel() {
        return !this.equals(READY);
    }

    boolean isCanceled() {
        return this.equals(CANCELED);
    }
}
