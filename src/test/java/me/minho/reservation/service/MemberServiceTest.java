package me.minho.reservation.service;

import me.minho.reservation.domain.Member;
import me.minho.reservation.domain.MemberType;
import me.minho.reservation.domain.Shop;
import me.minho.reservation.domain.vo.Period;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.time.LocalTime;

import static me.minho.reservation.domain.MemberType.ADMIN;
import static me.minho.reservation.domain.MemberType.NORMAL;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("일반 사용자는 이메일, 비밀번호, 이름을 받아 생성한다.")
    public void joinTest() {
        Member testMember = Member.builder()
                .name("테스트")
                .email("mail@test.com")
                .password("test1234")
                .memberType(NORMAL)
                .build();

        Member saveMember = memberService.join(testMember);
        assertThat(testMember).isEqualTo(saveMember);
    }

    @Test
    @DisplayName("이메일 필드는 이메일 형식이어야 한다.")
    public void emailValidationTest() {
        Member testMember = Member.builder()
                .name("테스트")
                .email("mail")
                .password("test1234")
                .memberType(NORMAL)
                .build();

        assertThatThrownBy(() -> {
            memberService.join(testMember);
            entityManager.flush();
        }, "이메일 필드는 이메일 형식이어야 한다.");
    }

    @Test
    @DisplayName("이름은 최소한 하나 이상의 문자가 들어가 있어야 한다")
    public void nameValidationTest() {
        Member testMember = Member.builder()
                .name(" ")
                .email("mail@test.com")
                .password("test1235")
                .memberType(NORMAL)
                .build();

        assertThatThrownBy(() -> {
            memberService.join(testMember);
            entityManager.flush();
        }, "이름은 최소한 하나 이상의 문자가 들어가 있어야 한다");
    }

    @Test
    @DisplayName("비밀번호는 최소한 8자 이상이어야 한다.")
    public void passwordValidationTest() {
        Member testMember = Member.builder()
                .name("테스트")
                .email("mail@test.com")
                .password("test123")
                .memberType(NORMAL)
                .build();

        assertThatThrownBy(() -> {
            memberService.join(testMember);
            entityManager.flush();
        }, "이름은 최소한 하나 이상의 문자가 들어가 있어야 한다");
    }

    @Test
    @DisplayName("어드민 회원가입은 샵 정보도 같이 받아야 한다.")
    public void adminJoinTest() {
        Member testAdminMember = Member.builder()
                .name("어드민테스트")
                .email("admin@admin.com")
                .password("test1234")
                .memberType(ADMIN)
                .build();

        Shop testShop = new Shop("testshop", "010-1234-1234",  "서울시", "XX 미용실", Period.of(LocalTime.of(9, 0), LocalTime.of(18, 0)), 30, testAdminMember);
        Member member = memberService.joinAdminMember(testAdminMember, testShop);
        entityManager.flush();

        assertThat(testAdminMember).isEqualTo(member);
    }

    @Test
    @DisplayName("멤버 로그인시 http session 에 member id 가 저장된다.")
    public void loginTest() {
        final String password = "test1234";
        final String email = "test@mhoh.com";
        Member member = Member.builder()
                .email(email)
                .name("test")
                .password(password)
                .memberType(MemberType.NORMAL)
                .build();

        MockHttpSession mockHttpSession = new MockHttpSession();

        memberService.join(member);
        entityManager.flush();

        boolean loginSuccess = memberService.login(email, password, mockHttpSession);

        assertThat(loginSuccess).isTrue();
        assertThat((Long) mockHttpSession.getAttribute(Member.LOGIN_ATTRIBUTE_NAME)).isGreaterThan(1L);
    }

    @Test
    @DisplayName("로그인시 해당 이메일을 가진 멤버가 없으면 예외이다")
    public void loginWrongEmailTest() {
        MockHttpSession mockHttpSession = new MockHttpSession();
        assertThatThrownBy(() -> memberService.login("test@mhoh.com", "test1234", mockHttpSession));
    }
}