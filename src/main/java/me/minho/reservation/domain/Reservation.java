package me.minho.reservation.domain;

import me.minho.reservation.domain.vo.Period;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "RESERVATION")
public class Reservation {

    @Id @GeneratedValue
    @Column(name = "RESERVATION_ID")
    private long id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "start", column = @Column(name = "START_TIME")),
            @AttributeOverride(name = "end", column = @Column(name = "END_TIME"))
    })
    private Period<LocalDateTime> reservationTimePeriod;

    @Enumerated(EnumType.STRING)
    @Column(name = "RESERVATION_STATUS", nullable = false)
    private ReservationStatus reservationStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "RESERVATION_TYPE", nullable = false)
    private ReservationType reservationType;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "SHOP_ID")
    private Shop shop;

    public long getId() {
        return id;
    }

    protected Reservation() {}

    private Reservation(Shop shop, Member member, Period<LocalDateTime> reservationTimePeriod) {
        this.shop = shop;
        this.member = member;
        this.reservationType = ReservationType.NORMAL;
        this.reservationStatus = ReservationStatus.READY;
        this.reservationTimePeriod = reservationTimePeriod;
    }

    public static Reservation createReservation(Shop shop, Member member, LocalDateTime reservationStartTime) {
        return new Reservation(shop, member, shop.getReservationPeriod(reservationStartTime));
    }

    public LocalDateTime getReservationStartTime() {
        return reservationTimePeriod.getStart();
    }
}
