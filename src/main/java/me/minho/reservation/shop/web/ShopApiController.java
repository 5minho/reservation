package me.minho.reservation.shop.web;

import lombok.RequiredArgsConstructor;
import me.minho.reservation.reservation.web.dto.ReservationResponse;
import me.minho.reservation.reservation.web.dto.ReservationSaveRequest;
import me.minho.reservation.shop.service.ShopService;
import me.minho.reservation.shop.web.dto.ShopResponse;
import me.minho.reservation.util.RestResponseData;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ShopApiController {

    private final ShopService shopService;

    @GetMapping("/shops")
    public RestResponseData<List<ShopResponse>> findAll() {
        return new RestResponseData(HttpStatus.OK, ShopResponse.of(shopService.findAll()));
    }

    @GetMapping("/shops/{id}")
    public RestResponseData<ShopResponse> findById(@PathVariable("id") long id) {
        try {
            return new RestResponseData(HttpStatus.OK, ShopResponse.of(shopService.findById(id)));
        } catch (IllegalArgumentException e) {
            return new RestResponseData(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    // TODO: LocalDate만 받아도 될 듯
    @GetMapping("/shops/{shopId}/reservations")
    public RestResponseData<List<ReservationResponse>> findShopWithOneDayReservationList(@PathVariable("shopId") long shopId, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime dateTime) {
        try {
            return new RestResponseData(HttpStatus.OK, ReservationResponse.of(shopService.findShopWithOneDayReservationList(shopId, dateTime), dateTime));
        }
        // TODO: 네이버 예약은 shopId에 해당하는 거 없으면 200 내려줌(정답은 없으니 프론트와 규약만 잘 지키자)
        catch (IllegalArgumentException e) {
            return new RestResponseData(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/shops/{shopId}/reservations")
    public RestResponseData<Long> saveReservation(@PathVariable("shopId") long shopId, @RequestBody ReservationSaveRequest reservationSaveRequest) {
        try {
            return new RestResponseData(HttpStatus.OK, shopService.saveReservation(shopId, reservationSaveRequest.toReservation()), "예약 완료!");
        } catch (IllegalArgumentException e) {
            return new RestResponseData(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IllegalStateException e) {
            return new RestResponseData(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }
}
