package me.minho.reservation.shop.service;

import lombok.RequiredArgsConstructor;
import me.minho.reservation.reservation.domain.Reservation;
import me.minho.reservation.shop.domain.Shop;
import me.minho.reservation.shop.domain.ShopRepository;
import me.minho.reservation.reservation.service.ReservationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ShopService {

    private final ReservationService reservationService;
    private final ShopRepository shopRepository;

    @Transactional
    public Long createShop(Shop shop) {
        return shopRepository.save(shop).getId();
    }

    public List<Shop> getShopList() {
        return shopRepository.findAll();
    }

    public Shop getShop(long id) {
        return shopRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 샵이 없습니다. id = " + id));
    }

    public Shop getShopWithOneDayReservationList(long shopId, LocalDateTime dateTime) {
        Shop shop = getShop(shopId);
        List<Reservation> reservationList = reservationService.getOneDayReservationList(shop.getId(), dateTime);
        shop.setReservationList(reservationList);
        return shop;
    }
}
