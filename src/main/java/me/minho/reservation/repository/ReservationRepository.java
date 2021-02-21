package me.minho.reservation.repository;

import me.minho.reservation.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("select r from Reservation r where r.shop.id = :shopId and r.reservationTimePeriod.start between :startTime and :endTime")
    List<Reservation> findReservationByShopAndStartTimeBetween(@Param("shopId") long shopId, LocalDateTime startTime, LocalDateTime endTime);

}
