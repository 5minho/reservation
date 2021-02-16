package me.minho.reservation.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static me.minho.reservation.domain.MemberType.ADMIN;
import static me.minho.reservation.domain.MemberType.NORMAL;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class ShopTest {

    @Test
    @DisplayName("멤버 타입이 Normal 인 멤버는 Shop 을 가질 수 없다.")
    public void createShopTest() {
        Member testAdminMember = Member.builder()
                .name("일반유저")
                .email("normal@test.com")
                .password("test1234")
                .memberType(NORMAL)
                .build();

        assertThatThrownBy(() ->
            new Shop("testshop", "010-1234-1234",  "서울시", "XX 미용실", "09:00", "18:00", 30, testAdminMember)
        );
    }

}