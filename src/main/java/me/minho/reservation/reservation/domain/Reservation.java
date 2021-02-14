package me.minho.reservation.reservation.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.minho.reservation.member.domain.Member;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "RESERVATION")
public class Reservation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RESERVATION_ID")
    private long id;

    @Column(name = "START_TIME", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "END_TIME", nullable = false)
    private LocalDateTime endTime;

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

    @Builder
    public Reservation(LocalDateTime startTime, LocalDateTime endTime, ReservationStatus reservationStatus, ReservationType reservationType) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.reservationStatus = reservationStatus;
        this. reservationType = reservationType;
    }

    public boolean isCompleted() {
        return reservationStatus == ReservationStatus.COMPLETED;
    }
}
