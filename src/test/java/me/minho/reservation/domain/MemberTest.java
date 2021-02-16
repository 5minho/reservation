package me.minho.reservation.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;

import static org.assertj.core.api.Assertions.assertThat;

class MemberTest {

    @Test
    @DisplayName("패스워드가 틀리면 로그인 할 수 없다.")
    public void wrongPasswordLoginTest() {
        Member member = Member.builder()
                .email("test@mhoh.com")
                .name("test")
                .password("test1234")
                .memberType(MemberType.NORMAL)
                .build();

        MockHttpSession mockHttpSession = new MockHttpSession();
        assertThat(member.login("test1235", mockHttpSession)).isFalse();
    }

}