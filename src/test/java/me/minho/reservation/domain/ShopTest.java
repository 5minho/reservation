package me.minho.reservation.domain;

import me.minho.reservation.domain.vo.Period;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class ShopTest {

    public static final Shop TEST_SHOP = new Shop("testshop", "010-1234-1234",  "서울시",
            "XX 미용실", Period.between(LocalTime.of(9, 0), LocalTime.of(18, 0)), 30, MemberTest.TEST_ADMIN);

    @Test
    @DisplayName("멤버 타입이 Normal 인 멤버는 Shop 을 가질 수 없다.")
    public void createShopTest() {
        Member testAdminMember = MemberTest.TEST_ADMIN;
        assertThatThrownBy(() ->
            new Shop("testshop", "010-1234-1234",  "서울시", "XX 미용실", Period.between(LocalTime.of(9, 0), LocalTime.of(18, 0)), 30, testAdminMember)
        );
    }

    @Test
    @DisplayName("이미 예약한 시간 리스트를 주면 예약 가능한 시간대를 리턴해야 한다.")
    public void findReservationAvailableTest() {
        final LocalDate today = LocalDate.of(2021, 2, 21);
        // given
        List<LocalDateTime> reservationPeriods = List.of(
                LocalDateTime.of(today, LocalTime.of(9, 0)),
                LocalDateTime.of(today, LocalTime.of(11, 0)),
                LocalDateTime.of(today, LocalTime.of(15, 30))
        );

        List<Period<LocalDateTime>> reservationAvailable = TEST_SHOP.findReservationAvailable(today, reservationPeriods);
        System.out.println(reservationAvailable);
        assertThat(reservationAvailable.size()).isEqualTo(15);
    }

}