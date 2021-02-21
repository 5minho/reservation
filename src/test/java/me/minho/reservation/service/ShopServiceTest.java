package me.minho.reservation.service;

import me.minho.reservation.domain.Member;
import me.minho.reservation.domain.Shop;
import me.minho.reservation.domain.vo.Period;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalTime;
import java.util.List;

import static me.minho.reservation.domain.MemberType.ADMIN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ShopServiceTest {

    @Autowired
    private ShopService shopService;
    private Shop testShop;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    public void setup() {
        Member testAdminMember = Member.builder()
                .name("어드민테스트")
                .email("admin@admin.com")
                .password("test1234")
                .memberType(ADMIN)
                .build();

        testShop = new Shop("testshop", "010-1234-1234",  "서울시", "XX 미용실", Period.between(LocalTime.of(9, 0), LocalTime.of(18, 0)), 30, testAdminMember);
    }

    @Test
    @DisplayName("샵 리스트를 잘 가지고 와야 한다.")
    public void shopListTest() {
        shopService.addShop(testShop);
        entityManager.flush();

        List<Shop> shops = shopService.findAll();
        assertThat(shops).contains(testShop);
    }
}