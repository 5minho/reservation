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

    public void cancel(long memberId) {
        if (!isReservedBy(memberId)) {
            throw new IllegalStateException("취소할 수 없다. 예약자 Id[" + this.member.getId() + "] 예약 취소자 Id[" + memberId + "]");
        }
        if (reservationStatus.cannotCancel()) {
            throw new IllegalStateException("취소할 수 없다. 현재 예약 상태 [" + reservationStatus + "]");
        }
        reservationStatus = ReservationStatus.CANCELED;
    }

    private boolean isReservedBy(long memberId) {
        return member.equalId(memberId);
    }

    public boolean isCanceled() {
        return reservationStatus.isCanceled();
    }
}
