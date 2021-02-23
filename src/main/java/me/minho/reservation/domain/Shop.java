package me.minho.reservation.domain;

import me.minho.reservation.domain.vo.Period;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "SHOP")
@SequenceGenerator(name = "SHOP_SEQ_GENERATOR")
public class Shop {
    public static final Shop TEST_SHOP;
    static {
        TEST_SHOP = new Shop();
        TEST_SHOP.id = 1;
        TEST_SHOP.openingTimePeriod = Period.between(LocalTime.of(9, 0), LocalTime.of(18, 0));
        TEST_SHOP.timeInterval = 30;
        TEST_SHOP.owner = Member.TEST_ADMIN;
        TEST_SHOP.name = "테스트 미용실";
        TEST_SHOP.contact = "010-1234-1234";
        TEST_SHOP.address = "구로구";
    }


    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SHOP_SEQ_GENERATOR")
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

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "start", column = @Column(name = "OPEN_TIME")),
            @AttributeOverride(name = "end", column = @Column(name = "CLOSE_TIME"))
    })
    private Period<LocalTime> openingTimePeriod;

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
                @NotNull Period<LocalTime> openingPeriod,
                @Min(10) int timeInterval,
                @NotNull Member owner) {
        if (!owner.isAdmin()) {
            throw new IllegalArgumentException("admin 타입의 멤버만 owner 가 될 수 있다.");
        }
        this.name = name;
        this.contact = contact;
        this.address = address;
        this.description = description;
        this.openingTimePeriod = openingPeriod;
        this.timeInterval = timeInterval;
        this.owner = owner;
    }

    public long getId() {
        return id;
    }

    public Period<LocalDateTime> getWorkingTimePeriod(LocalDate targetDate) {
        final LocalDateTime start = targetDate.atTime(openingTimePeriod.getStart());
        final LocalDateTime end = targetDate.atTime(openingTimePeriod.getEnd());
        return Period.between(start, end);
    }

    public Period<LocalDateTime> getReservationPeriod(LocalDateTime startTime) {
        return Period.between(startTime, startTime.plusMinutes(timeInterval));
    }

    public List<Period<LocalDateTime>> findReservationAvailable(LocalDate date, List<LocalDateTime> excepts) {
        final LocalDateTime start = date.atTime(openingTimePeriod.getStart());
        final LocalDateTime end = date.atTime(openingTimePeriod.getEnd());

        List<Period<LocalDateTime>> reservationAvailable = new ArrayList<>();

        for (LocalDateTime reservationTime = start ; reservationTime.isBefore(end) ; reservationTime = reservationTime.plusMinutes(timeInterval)) {
            if (excepts.contains(reservationTime)) {
                continue;
            }
            reservationAvailable.add(Period.between(reservationTime, reservationTime.plusMinutes(timeInterval)));
        }

        return reservationAvailable;
    }

}
