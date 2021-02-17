package me.minho.reservation.service;

import me.minho.reservation.domain.Shop;
import me.minho.reservation.repository.ShopRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopService {

    private final ShopRepository shopRepository;

    public ShopService(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    public void addShop(Shop shop) {
        shopRepository.save(shop);
    }

    public List<Shop> findAll() {
        return shopRepository.findAll();
    }
}
