package me.minho.reservation.service;

import me.minho.reservation.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ReservationServiceTest {

    @Autowired private ReservationService reservationService;
    @Autowired private MemberService memberService;
    @Autowired private ShopService shopService;

    private Member member;
    private Shop shop;

    @BeforeEach
    public void setup() {
        this.member = MemberTest.TEST_MEMBER;
        memberService.join(member);

        this.shop = ShopTest.TEST_SHOP;
        shopService.addShop(shop);
    }

    @Test
    @DisplayName("예약할 때 예약자와 예약시간이 필요하다")
    public void reservationTest() {
        // given
        final LocalDateTime reservationTime = LocalDateTime.of(2021, 12, 24, 9, 30);

        // when
        final Reservation reservation = reservationService.reserve(member.getId(), shop.getId(), reservationTime);

        // then
        assertThat(reservation.getId()).isGreaterThan(0L);
    }

    @Test
    @DisplayName("예약을 했으면 예약한 리스트에 나의 예약이 나와야 한다.")
    public void reservationListTest() {
        // given
        final LocalDateTime reservationTime = LocalDateTime.of(2021, 12, 24, 9, 30);
        final Reservation reservation = reservationService.reserve(member.getId(), shop.getId(), reservationTime);

        // when
        List<Reservation> reservations = reservationService.findReservations(member.getId(), reservationTime.toLocalDate());

        //then
        assertThat(reservations.size()).isEqualTo(1);
        assertThat(reservations).contains(reservation);
    }
}