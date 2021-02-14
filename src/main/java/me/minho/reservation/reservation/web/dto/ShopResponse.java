package me.minho.reservation.reservation.web.dto;

import lombok.Builder;
import lombok.Getter;
import me.minho.reservation.reservation.domain.Shop;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ShopResponse {
    private String name;
    private String contact;
    private String address;
    private String description;
    private LocalDateTime openTime;
    private LocalDateTime closeTime;
    private int interval;

    public static ShopResponse of(Shop shop) {
        return ShopResponse.builder()
                .name(shop.getName())
                .contact(shop.getContact())
                .address(shop.getAddress())
                .description(shop.getDescription())
                .openTime(shop.getOpenTime())
                .closeTime(shop.getCloseTime())
                .interval(shop.getInterval())
                .build();
    }

    public static List<ShopResponse> of(List<Shop> shopList) {
        return shopList.stream()
                .map(shop -> ShopResponse.of(shop))
                .collect(Collectors.toList());
    }
}
