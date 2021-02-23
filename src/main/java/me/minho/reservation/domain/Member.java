package me.minho.reservation.domain;

import org.hibernate.validator.constraints.Length;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "MEMBER")
@SequenceGenerator(name = "MEMBER_SEQ_GENERATOR")
public class Member {
    public static final Member TEST_MEMBER;
    public static final Member TEST_ADMIN;
    static {
        TEST_MEMBER = new Member();
        TEST_MEMBER.id = 1;
        TEST_MEMBER.memberType = MemberType.NORMAL;
        TEST_MEMBER.password = "test1234";
        TEST_MEMBER.email = "test@email.com";
        TEST_MEMBER.name = "테스트";

        TEST_ADMIN = new Member();
        TEST_ADMIN.id = 2;
        TEST_ADMIN.memberType = MemberType.ADMIN;
        TEST_ADMIN.password = "test1234";
        TEST_ADMIN.email = "test@admin.com";
        TEST_ADMIN.name = "테스트어드민";
    }

    public static final String LOGIN_ATTRIBUTE_NAME = "MEMBER_ID";

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_SEQ_GENERATOR")
    @Column(name = "MEMBER_ID")
    private long id;

    @Email
    @Column(name = "EMAIL", unique = true, nullable = false)
    private String email;

    @Length(min = 8, max = 255)
    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @NotBlank
    @Column(name = "NAME", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "MEMBER_TYPE", nullable = false)
    private MemberType memberType;

    public boolean isAdmin() {
        return memberType.isAdminType();
    }

    protected Member() { }

    private Member(Builder memberBuilder) {
        this.name = memberBuilder.name;
        this.email = memberBuilder.email;
        this.password = memberBuilder.password;
        this.memberType = memberBuilder.memberType;
    }

    public static Builder builder() {
        return new Builder();
    }

    private boolean matchPassword(String password) {
        if (!StringUtils.hasText(password)) {
            return false;
        }
        return this.password.equals(password);
    }

    public boolean login(String password, HttpSession httpSession) {
        if (!matchPassword(password)) {
            return false;
        }
        httpSession.setAttribute(LOGIN_ATTRIBUTE_NAME, this.id);
        return true;
    }

    public static class Builder {
        private String email;
        private String password;
        private String name;
        private MemberType memberType;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder memberType(MemberType memberType) {
            this.memberType = memberType;
            return this;
        }

        public Member build() {
            return new Member(this);
        }
    }

    public long getId() {
        return id;
    }

    public boolean equalId(long memberId) {
        return this.id == memberId;
    }
}
