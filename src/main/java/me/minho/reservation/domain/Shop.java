package me.minho.reservation.domain;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MINUTES;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "SHOP")
public class Shop {

    @Id @GeneratedValue
    @Column(name = "SHOP_ID")
    private long id;

    @NotBlank
    @Column(name = "NAME", nullable = false)
    private String name;

    @NotBlank
    @Column(name = "CONTACT", nullable = false)
    private String contact;

    @NotBlank
    @Column(name = "ADDRESS", nullable = false)
    private String address;

    @NotBlank
    @Lob
    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @NotNull
    @Pattern(regexp = "^[012]\\d:[012345]\\d$")
    @Column(name = "OPEN_TIME", nullable = false)
    private String openTime;

    @NotNull
    @Pattern(regexp = "^[012]\\d:[012345]\\d$")
    @Column(name = "CLOSE_TIME", nullable = false)
    private String closeTime;

    // 현재는 분 단위 필요하면 TIME_UNIT 을 추가할 것
    @Min(10)
    @Column(name = "TIME_INTERVAL", nullable = false)
    private int timeInterval;

    @NotNull
    @OneToOne(fetch = LAZY, cascade = PERSIST)
    @JoinColumn(name = "OWNER_ID")
    private Member owner;

    protected Shop() {}

    public Shop(@NotBlank String name,
                @NotBlank String contact,
                @NotBlank String address,
                @NotBlank String description,
                @Pattern(regexp = "^[012]\\d:[012345]\\d$") String openTime,
                @Pattern(regexp = "^[012]\\d:[012345]\\d$") String closeTime,
                @Min(10) int timeInterval,
                @NotNull Member owner) {
        if (!owner.isAdmin()) {
            throw new IllegalArgumentException("admin 타입의 멤버만 owner 가 될 수 있다.");
        }
        this.name = name;
        this.contact = contact;
        this.address = address;
        this.description = description;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.timeInterval = timeInterval;
        this.owner = owner;
    }

    public List<LocalDateTime> getReservationTimeList(LocalDate reservationDate) {
        final LocalDateTime openTime = atTime(reservationDate, this.openTime);
        final LocalDateTime closeTime = atTime(reservationDate, this.closeTime);

        List<LocalDateTime> reservationTimeList = new ArrayList<>();

        for (LocalDateTime time = openTime ; time.isBefore(closeTime) ; time = time.plusMinutes(timeInterval)) {
            reservationTimeList.add(time);
        }

        return reservationTimeList;
    }

    private LocalDateTime atTime(LocalDate localDate, String time) {
        String[] hourMinute = time.split(":");
        int hour = Integer.parseInt(hourMinute[0]);
        int minute = Integer.parseInt(hourMinute[1]);
        return localDate.atTime(hour, minute);
    }
}
