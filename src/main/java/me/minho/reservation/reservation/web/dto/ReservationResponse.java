package me.minho.reservation.reservation.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import me.minho.reservation.reservation.domain.Reservation;
import me.minho.reservation.reservation.domain.ReservationStatus;
import me.minho.reservation.reservation.domain.ReservationType;
import me.minho.reservation.shop.domain.Shop;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ReservationResponse {

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private ReservationStatus status;
    private ReservationType type;

    private static ReservationResponse of(Reservation reservation) {
        return ReservationResponse.builder()
                .id(reservation.getId())
                .startTime(reservation.getStartTime())
                .endTime(reservation.getEndTime())
                .status(reservation.getReservationStatus())
                .type(reservation.getReservationType())
                .build();
    }

    public static List<ReservationResponse> of(List<Reservation> reservationList) {
        return reservationList.stream()
                .map(reservation -> of(reservation))
                .collect(Collectors.toList());
    }

    // TODO: 예약 가능한 것을 골라내는 로직을 변환할 때 하는 것이 아니라, 도메인의 메소드로 만드는게 좋을 듯
    public static List<ReservationResponse> of(Shop shop, LocalDateTime dateTime) {
        final int HOURS_OF_ONE_DAY = 24;
        final int MINUTES_OF_ONE_HOUR = 60;
        final int reservationListSize = (int) (HOURS_OF_ONE_DAY * MINUTES_OF_ONE_HOUR / (float) shop.getInterval());

        // 예약 시간 간격에 맞춰서, 준비 상태로 모두 셋팅
        List<Reservation> reservationList = new ArrayList<>();
        for (int i = 0; i < reservationListSize; i++) {
            Reservation reservation = Reservation.builder()
                    .startTime(dateTime)
                    .endTime(dateTime.plusMinutes(shop.getInterval()))
                    .reservationStatus(ReservationStatus.READY)
                    .reservationType(ReservationType.NORMAL)
                    .build();

            reservationList.add(reservation);
            dateTime = dateTime.plusMinutes(shop.getInterval());
        }

        // 예약 완료 건이 있으면, 해당 시간대는 예약 완료 상태로 변경
        for (Reservation reservation : shop.getReservationList()) {
            if (reservation.isCompleted()) {
                final int HOUR_OF_START_TIME = reservation.getStartTime().getHour();
                final int TOTAL_MINUTE_OF_START_TIME = HOUR_OF_START_TIME * 60 + reservation.getStartTime().getMinute();
                final int RESERVATION_INDEX = TOTAL_MINUTE_OF_START_TIME / shop.getInterval();

                reservationList.remove(RESERVATION_INDEX);
                reservationList.add(RESERVATION_INDEX, reservation);
            }
        }

        return of(reservationList);
    }
}
