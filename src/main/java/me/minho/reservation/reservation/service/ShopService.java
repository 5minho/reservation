package me.minho.reservation.reservation.service;

import lombok.RequiredArgsConstructor;
import me.minho.reservation.reservation.domain.Shop;
import me.minho.reservation.reservation.domain.ShopRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ShopService {

    private final ShopRepository shopRepository;

    @Transactional
    public Long createShop(Shop shop) {
        return shopRepository.save(shop).getId();
    }

    public List<Shop> getShopList() {
        return shopRepository.findAll();
    }

    public Shop getShop(long id) {
        return shopRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 샵이 없습니다. id=" + id));
    }
}
