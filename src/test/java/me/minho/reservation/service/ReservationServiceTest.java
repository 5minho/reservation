package me.minho.reservation.service;

import me.minho.reservation.domain.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ReservationServiceTest {

    @Autowired private ReservationService reservationService;
    @Autowired private MemberService memberService;
    @Autowired private ShopService shopService;

    @Test
    @DisplayName("예약할 때 예약자와 예약시간이 필요하다")
    public void reservationTest() {
        // given
        final Member member = MemberTest.TEST_MEMBER;
        memberService.join(member);

        final Shop shop = ShopTest.TEST_SHOP;
        shopService.addShop(shop);

        final LocalDateTime reservationTime = LocalDateTime.of(2021, 12, 24, 9, 30);

        // when
        final Reservation reservation = reservationService.reserve(member.getId(), shop.getId(), reservationTime);

        // then
        assertThat(reservation.getId()).isGreaterThan(0L);
    }
}