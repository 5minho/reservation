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

    @GetMapping("/shop/{id}")
    public RestResponseData<ShopResponse> findById(@PathVariable("id") long id) {
        try {
            return new RestResponseData(HttpStatus.OK, ShopResponse.of(shopService.findById(id)));
        } catch (IllegalArgumentException e) {
            return new RestResponseData(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/shop/{shopId}/reservations") // TODO: LocalDate만 받아도 될 듯
    public RestResponseData<List<ReservationResponse>> findShopWithOneDayReservationList(@PathVariable("shopId") long shopId, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime dateTime) {
        try {
            return new RestResponseData(HttpStatus.OK, ReservationResponse.of(shopService.findShopWithOneDayReservationList(shopId, dateTime), dateTime));
        } catch (IllegalArgumentException e) {
            return new RestResponseData(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/shop/{shopId}/reservation")
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
