package me.minho.reservation.shop.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.minho.reservation.member.domain.Member;
import me.minho.reservation.reservation.domain.Reservation;

import javax.persistence.*;

import java.time.LocalTime;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "SHOP")
public class Shop {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SHOP_ID")
    private long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "CONTACT", nullable = false)
    private String contact;

    @Column(name = "ADDRESS", nullable = false)
    private String address;

    @Lob
    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @Column(name = "OPEN_TIME", nullable = false)
    private LocalTime openTime;

    @Column(name = "CLOSE_TIME", nullable = false)
    private LocalTime closeTime;

    @Column(name = "RESERVATION_TIME_INTERVAL", nullable = false)
    private int interval;

    // TODO: cascade persist 주면 shop 저장하면서 member(관리자) 저장 가능
    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member owner;

    @Setter
    @OneToMany(mappedBy = "shop")
    private List<Reservation> reservationList;

    @Builder
    public Shop(String name, String contact, String address, String description, LocalTime openTime, LocalTime closeTime, int interval, Member owner) {
        this.name = name;
        this.contact = contact;
        this.address = address;
        this.description = description;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.interval = interval;
        this.owner = owner;
    }
}
