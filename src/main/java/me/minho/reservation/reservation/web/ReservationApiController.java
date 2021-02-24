package me.minho.reservation.reservation.web;

import lombok.RequiredArgsConstructor;
import me.minho.reservation.reservation.service.ReservationService;
import me.minho.reservation.reservation.web.dto.ReservationResponse;
import me.minho.reservation.reservation.web.dto.ReservationStatusUpdateRequest;
import me.minho.reservation.reservation.web.dto.ReservationTimeUpdateRequest;
import me.minho.reservation.util.RestResponseData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ReservationApiController {

    private final ReservationService reservationService;

    @GetMapping("/reservations")
    public RestResponseData<List<ReservationResponse>> findAll() {
        return new RestResponseData(HttpStatus.OK, ReservationResponse.of(reservationService.findAll()));
    }

    // TODO: 밑에 2개 합칠건가, status를 받을 것인가 status 별로 endpoint를 나눌 것인가
    @PutMapping("/reservations/{id}/status")
    public RestResponseData<Long> activate(@PathVariable("id") long id, @RequestBody ReservationStatusUpdateRequest reservationStatusUpdateRequest) {
        try {
            return new RestResponseData(HttpStatus.OK, reservationService.updateStatus(id, reservationStatusUpdateRequest.getStatus()), "변경 성공!");
        } catch (IllegalArgumentException e) {
            return new RestResponseData(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/reservations/{id}/time")
    public RestResponseData<Long> update(@PathVariable("id") long id, @RequestBody ReservationTimeUpdateRequest reservationTimeUpdateRequest) {
        try {
            return new RestResponseData(HttpStatus.OK, reservationService.updateTime(id, reservationTimeUpdateRequest), "변경 성공!");
        } catch (IllegalArgumentException e) {
            return new RestResponseData(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
