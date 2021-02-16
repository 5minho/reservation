package me.minho.reservation.domain;

public enum MemberType {
    NORMAL, ADMIN;

    public boolean isAdminType() {
        return this.equals(ADMIN);
    }
}
