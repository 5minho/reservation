package me.minho.reservation.reservation.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.minho.reservation.reservation.domain.Reservation;
import me.minho.reservation.reservation.domain.ReservationStatus;
import me.minho.reservation.reservation.domain.ReservationType;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ReservationSaveRequest {

    // TODO: config로 전역적으로 설정할 수도 있음
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endTime;
    private ReservationType type;

    public Reservation toReservation() {
        return Reservation.builder()
                .startTime(startTime)
                .endTime(endTime)
                .reservationType(type)
                .reservationStatus(ReservationStatus.COMPLETED)
                .build();
    }
}
