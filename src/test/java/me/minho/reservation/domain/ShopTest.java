package me.minho.reservation.domain;

import me.minho.reservation.domain.vo.Period;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class ShopTest {

    public static final Shop TEST_SHOP = new Shop("testshop", "010-1234-1234",  "서울시", "XX 미용실", Period.of(LocalTime.of(9, 0), LocalTime.of(18, 0)), 30, MemberTest.TEST_ADMIN);

    @Test
    @DisplayName("멤버 타입이 Normal 인 멤버는 Shop 을 가질 수 없다.")
    public void createShopTest() {
        Member testAdminMember = MemberTest.TEST_ADMIN;
        assertThatThrownBy(() ->
            new Shop("testshop", "010-1234-1234",  "서울시", "XX 미용실", Period.of(LocalTime.of(9, 0), LocalTime.of(18, 0)), 30, testAdminMember)
        );
    }

}