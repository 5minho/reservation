package me.minho.reservation.service;

import me.minho.reservation.domain.Member;
import me.minho.reservation.domain.Shop;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static me.minho.reservation.domain.MemberType.ADMIN;
import static org.assertj.core.api.Assertions.assertThat;

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

        testShop = new Shop("testshop", "010-1234-1234",  "서울시", "XX 미용실",
                "09:00", "18:00", 30, testAdminMember);
    }

    @Test
    @DisplayName("샵 리스트가 잘 가져와 지는지 테스트")
    public void shopListTest() {
        shopService.addShop(testShop);
        entityManager.flush();

        List<Shop> shops = shopService.findAll();
        assertThat(shops).contains(testShop);
    }
}