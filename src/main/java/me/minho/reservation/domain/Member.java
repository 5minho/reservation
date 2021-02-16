package me.minho.reservation.domain;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "MEMBER")
public class Member {

    @Id @GeneratedValue
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
}
