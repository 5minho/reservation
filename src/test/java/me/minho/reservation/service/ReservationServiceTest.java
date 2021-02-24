package me.minho.reservation.service;

import me.minho.reservation.domain.*;
import me.minho.reservation.domain.vo.Period;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static me.minho.reservation.domain.MemberType.ADMIN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ReservationServiceTest {

    @Autowired private ReservationService reservationService;
    @Autowired private MemberService memberService;
    @Autowired private ShopService shopService;
    @Autowired private EntityManager entityManager;

    private Member member;
    private Shop shop;

    // 테스트 실패 같이 보기
    @BeforeEach
    public void setup() {
        this.member = Member.TEST_MEMBER;
        this.shop = Shop.TEST_SHOP;
        memberService.join(member);
        memberService.join(Member.TEST_ADMIN);
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

    @Test
    @DisplayName("예약을 취소하면 예약상태가 취소 상태로 바뀌어야 한다.")
    public void reservationCancelTest() {
        // given
        final LocalDateTime reservationTime = LocalDateTime.of(2021, 12, 24, 9, 30);
        final Reservation reservation = reservationService.reserve(member.getId(), shop.getId(), reservationTime);

        // when
        reservationService.cancel(reservation.getId(), member.getId());

        // then
        List<Reservation> reservations = reservationService.findReservations(member.getId(), reservationTime.toLocalDate());
        assertThat(reservations.get(0).isCanceled()).isTrue();
    }

    @Test
    @DisplayName("예약 시간을 변경할 수 있다.")
    public void updateReservationTimeTest() {
        // given
        final LocalDateTime reservationTime = LocalDateTime.of(2021, 12, 24, 9, 30);
        final Reservation reservation = reservationService.reserve(member.getId(), shop.getId(), reservationTime);

        // when
        reservationService.updateReservationTime(reservation.getId(), reservationTime.plusDays(1));

        // then
        assertThat(reservation.getReservationStartTime()).isEqualTo(LocalDateTime.of(2021, 12, 25, 9, 30));
    }

    @Test
    @DisplayName("변경할 예약시간이 이미 예약이 있으면 변경하지 못한다.")
    public void updateReservationTimeFailTest() {
        // given
        final LocalDateTime reservationTime = LocalDateTime.of(2021, 12, 24, 9, 30);
        final Reservation reservation = reservationService.reserve(member.getId(), shop.getId(), reservationTime);

        // when
        // then
        assertThatThrownBy(() -> reservationService.updateReservationTime(reservation.getId(), reservationTime));
    }
}