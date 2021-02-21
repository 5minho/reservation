package me.minho.reservation.service;

import me.minho.reservation.domain.Shop;
import me.minho.reservation.repository.ShopRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ShopService {

    private final ShopRepository shopRepository;

    public ShopService(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    public void addShop(Shop shop) {
        shopRepository.save(shop);
    }

    @Transactional(readOnly = true)
    public List<Shop> findAll() {
        return shopRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Shop findById(long shopId) {
        return shopRepository.findById(shopId).orElseThrow(() -> new IllegalArgumentException("ID:[" + shopId + "] 인 shop 이 없다."));
    }
}
