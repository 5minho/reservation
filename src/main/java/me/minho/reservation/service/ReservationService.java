package me.minho.reservation.service;

import me.minho.reservation.domain.Member;
import me.minho.reservation.domain.Reservation;
import me.minho.reservation.domain.ReservationStatus;
import me.minho.reservation.domain.Shop;
import me.minho.reservation.domain.vo.Period;
import me.minho.reservation.domain.vo.Periods;
import me.minho.reservation.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static me.minho.reservation.domain.ReservationStatus.READY;

@Service
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberService memberService;
    private final ShopService shopService;

    public ReservationService(ReservationRepository reservationRepository, MemberService memberService, ShopService shopService) {
        this.reservationRepository = reservationRepository;
        this.memberService = memberService;
        this.shopService = shopService;
    }

    public Reservation reserve(long memberId, long shopId, LocalDateTime reservationTime) {
        checkAvailableReservationTime(shopId, reservationTime);
        final Member member = memberService.findById(memberId);
        final Shop shop = shopService.findById(shopId);
        return reservationRepository.save(Reservation.createReservation(shop, member, reservationTime));
    }

    @Transactional(readOnly = true)
    public List<Period<LocalDateTime>> findReservedTimePeriods(long shopId, LocalDate reservationDate) {
        Shop shop = shopService.findById(shopId);
        List<Reservation> reservationsInToday = reservationRepository.findReservationByShopIdAndStartTimeBetweenAndStatus(shopId, shop.getWorkingTimePeriod(reservationDate), READY);
        List<LocalDateTime> exceptPeriods = reservationsInToday.stream().map(Reservation::getReservationStartTime).collect(Collectors.toList());
        return shop.findReservationAvailable(reservationDate, exceptPeriods);
    }

    @Transactional(readOnly = true)
    public List<Reservation> findReservations(long memberId, LocalDate targetDate) {
        final Period<LocalDateTime> targetDateStartEnd = Period.between(targetDate.atStartOfDay(), targetDate.atTime(LocalTime.MAX));
        return reservationRepository.findReservationByMemberIdAndStartTimeBetween(memberId, targetDateStartEnd);
    }

    public Reservation cancel(long reservationId, long memberId) {
        Reservation reservation = findById(reservationId);
        reservation.cancel(memberId);
        return reservation;
    }

    public void updateReservationTime(long reservationId, LocalDateTime reservationTime) {
        Reservation reservation = findById(reservationId);
        checkAvailableReservationTime(reservation.getShopId(), reservationTime);
        reservation.updateReservationTime(reservationTime);
    }

    private void checkAvailableReservationTime(long shopId, LocalDateTime reservationTime) {
        Periods<LocalDateTime> reservationPeriods = Periods.of(findReservedTimePeriods(shopId, reservationTime.toLocalDate()));
        if (!reservationPeriods.contains(reservationTime)) {
            throw new IllegalArgumentException(reservationTime + " 에는 예약 불가 입니다.");
        }
    }

    public Reservation findById(long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("ID:[" + reservationId + "] 인 reservation 은 없다."));
    }

}
