package me.minho.reservation.repository;

import me.minho.reservation.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByShopIdAndStartTimeAfterAndStartTimeBefore(long shopId, LocalDateTime openTime, LocalDateTime closeTime);
}
