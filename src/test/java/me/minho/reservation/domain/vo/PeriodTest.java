package me.minho.reservation.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class PeriodTest {

    @Test
    @DisplayName("동등성 테스트")
    public void equalsTest() {
        Period<LocalTime> one = Period.between(LocalTime.of(10, 0), LocalTime.of(11, 0));
        Period<LocalTime> another = Period.between(LocalTime.of(10, 0), LocalTime.of(11, 0));

        assertThat(one).isEqualTo(another);
    }

}