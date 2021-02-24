package me.minho.reservation.reservation.service;

import lombok.RequiredArgsConstructor;
import me.minho.reservation.member.domain.Member;
import me.minho.reservation.reservation.domain.Reservation;
import me.minho.reservation.reservation.domain.ReservationRepository;
import me.minho.reservation.reservation.domain.ReservationStatus;
import me.minho.reservation.reservation.web.dto.ReservationTimeUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public List<Reservation> findOneDayReservationList(long shopId, LocalDateTime dateTime) {
        return reservationRepository.findAllByShopIdAndStartTimeAfterAndStartTimeBefore(shopId, dateTime, dateTime.plusDays(1));
    }

    @Transactional
    public long save(Reservation reservation) {
        Reservation completedReservation = findByShopIdAndStartTimeAndStatus(reservation.getShop().getId(), reservation.getStartTime(), ReservationStatus.COMPLETED);
        if (completedReservation != null) {
            throw new IllegalStateException("이미 예약 완료건이 존재합니다");
        }

        return reservationRepository.save(reservation).getId();
    }

    // TODO: shopId를 알고 있네? reservation은 shop을 모르게 바꿔보자
    private Reservation findByShopIdAndStartTimeAndStatus(long shopId, LocalDateTime startTime, ReservationStatus status) {
        return reservationRepository.findByShopIdAndStartTimeAndReservationStatus(shopId, startTime, status);
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAllByMember(Member.SUPER_MEMBER);
    }

    @Transactional
    public long updateStatus(long id, ReservationStatus status) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 예약이 없습니다. id = " + id));
        /**
         * TODO: activate Vs setReservationStatus
         * activate로 하면 endpoint 새로 만들어야 하지만, activate 메소드 안에서 여러 예외처리 가능함
         * setReservationStatus로 하면 enpoint 새로 만들 필요 없지만, 외부에서 값을 넣어줘야 하고 예외 처리하려면 if문 덕지덕지
         */
        reservation.setReservationStatus(status);
        reservation.activate();
        return reservation.getId();
    }

    @Transactional
    public long updateTime(long id, ReservationTimeUpdateRequest reservationTimeUpdateRequest) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 예약이 없습니다. id = " + id));
        reservation.updateTime(reservationTimeUpdateRequest.getStartTime(), reservationTimeUpdateRequest.getEndTime());
        return reservation.getId();
    }
}
