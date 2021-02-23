package me.minho.reservation.repository;

import me.minho.reservation.domain.Reservation;
import me.minho.reservation.domain.ReservationStatus;
import me.minho.reservation.domain.vo.Period;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("select r from Reservation r where r.shop.id = :shopId and r.reservationTimePeriod.start between :#{#period.start} and :#{#period.end} and r.reservationStatus = :status")
    List<Reservation> findReservationByShopIdAndStartTimeBetweenAndStatus(@Param("shopId") long shopId,
                                                                 @Param("period") Period<LocalDateTime> period,
                                                                 @Param("status")ReservationStatus status);

    @Query("select r from Reservation r where r.member.id = :memberId and r.reservationTimePeriod.start between :#{#period.start} and :#{#period.end}")
    List<Reservation> findReservationByMemberIdAndStartTimeBetween(@Param("memberId") long memberId,
                                                                   @Param("period") Period<LocalDateTime> period);

}
