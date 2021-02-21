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
import java.time.LocalDate;
import java.time.LocalDateTime;
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

        testShop = new Shop("testshop", "010-1234-1234",  "서울시", "XX 미용실",
                LocalTime.of(9, 0), LocalTime.of(18, 0), 30, testAdminMember);
    }

    @Test
    @DisplayName("샵 리스트를 잘 가지고 와야 한다.")
    public void shopListTest() {
        shopService.addShop(testShop);
        entityManager.flush();

        List<Shop> shops = shopService.findAll();
        assertThat(shops).contains(testShop);
    }

    @Test
    @DisplayName("샵의 예약 가능한 시간을 가지고 올때 shop id 에 해당하는 shop 이 없으면 예외가 발생해야 한다.")
    public void testFindReservationTimeNotExistShopException() {
        shopService.addShop(testShop);

        assertThatThrownBy(() -> shopService.findReservationTimeList(-1, LocalDate.now()));
    }

    @Test
    @DisplayName("샵의 예약 가능한 시간을 잘 가지고 와야 한다.")
    public void testFindReservationTime() {
        shopService.addShop(testShop);

        final LocalDate today = LocalDate.of(2021, 2, 18);

        List<LocalDateTime> expectedReservationTimeList = List.of(
                today.atTime(9, 0),
                today.atTime(9, 30),
                today.atTime(10, 0),
                today.atTime(10, 30),
                today.atTime(11, 0),
                today.atTime(11, 30),
                today.atTime(12, 0),
                today.atTime(12, 30),
                today.atTime(13, 0),
                today.atTime(13, 30),
                today.atTime(14, 0),
                today.atTime(14, 30),
                today.atTime(15, 0),
                today.atTime(15, 30),
                today.atTime(16, 0),
                today.atTime(16, 30),
                today.atTime(17, 0),
                today.atTime(17, 30)
        );

        assertThat(shopService.findReservationTimeList(1L, today)).isEqualTo(expectedReservationTimeList);
    }
}