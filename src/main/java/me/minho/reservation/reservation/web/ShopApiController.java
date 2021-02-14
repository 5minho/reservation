package me.minho.reservation.reservation.web;

import lombok.RequiredArgsConstructor;
import me.minho.reservation.reservation.service.ShopService;
import me.minho.reservation.reservation.web.dto.ReservationResponse;
import me.minho.reservation.reservation.web.dto.ShopResponse;
import me.minho.reservation.util.RestResponseData;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ShopApiController {

    private final ShopService shopService;

    @GetMapping("/shops")
    public RestResponseData<List<ShopResponse>> getShopList() {
        return new RestResponseData(HttpStatus.OK, ShopResponse.of(shopService.getShopList()));
    }

    @GetMapping("/shop/{id}")
    public RestResponseData<ShopResponse> getShop(@PathVariable("id") long id) {
        try {
            return new RestResponseData(HttpStatus.OK, ShopResponse.of(shopService.getShop(id)));
        } catch (IllegalArgumentException e) {
            return new RestResponseData(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/reservations/{shopId}")
    public RestResponseData<List<ReservationResponse>> getReservationList(@PathVariable("shopId") long shopId, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime date) {
        try {
            return new RestResponseData(HttpStatus.OK, ReservationResponse.of(shopService.getReservationList(shopId, date)));
        } catch (IllegalArgumentException e) {
            return new RestResponseData(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
