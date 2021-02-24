package me.minho.reservation.reservation.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.minho.reservation.reservation.domain.ReservationStatus;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ReservationTimeUpdateRequest {
    private ReservationStatus status;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endTime;
}
