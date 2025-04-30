import eus.ehu.cinemaProject.domain.Cinema;
import eus.ehu.cinemaProject.domain.Schedule;
import eus.ehu.cinemaProject.domain.ScreeningRoom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ScheduleTest {
    Schedule schedule;

    @BeforeEach
    public void setup(){
        Cinema cinema = new Cinema("Test Cinema", "Test Address", 123456789, LocalTime.of(12,30), LocalTime.of(1, 30));
        ScreeningRoom screeningRoom = new ScreeningRoom(cinema, 1);
        schedule = new Schedule(LocalDate.now(), screeningRoom);
        System.out.println(schedule.getOpeningTime() + " " + schedule.getClosingTime());
    }

    //Check if the schedule is empty. We rest 15 minutes, because the last frame will be from <(ClosingTime-15minutes)> to <ClosingTime>
    @Test
    public void areAllFramesFree() {
        Duration diff = Duration.between(schedule.getOpeningTime(), schedule.getClosingTime().minusMinutes(15));
        LocalTime duration = LocalTime.MIDNIGHT.plus(diff);
        assertTrue(schedule.isBetweenBoundsFree(schedule.getOpeningTime(), duration));
    }

    //Check if the frames that the film has occupied have been booked. Before 00:00 - before 00:00 conditions
    @Test
    public void normalBooking() {
        LocalTime startHour = LocalTime.of(17, 45);
        LocalTime duration = LocalTime.of(1, 30);
        schedule.bookBetweenTimeBounds(startHour, duration);
        assertTrue(!schedule.isFilmInBounds(startHour, duration));
    }

    //Check if the frames that the film has occupied have been booked. Before 00:00 - after 00:00 conditions
    @Test
    public void normalAndNightBooking() {
        LocalTime startHour = LocalTime.of(23, 45);
        LocalTime duration = LocalTime.of(0, 45);
        schedule.bookBetweenTimeBounds(startHour, duration);
        assertTrue(!schedule.isFilmInBounds(startHour, duration));
    }

    //Check if the frames that the film has occupied have been booked. After 00:00 - after 00:00 conditions
    @Test
    public void nightAndNightBooking() {
        LocalTime startHour = LocalTime.of(0, 0);
        LocalTime duration = LocalTime.of(1, 0);
        schedule.bookBetweenTimeBounds(startHour, duration);
        schedule.printAllReserves();
        assertTrue(!schedule.isFilmInBounds(startHour, duration));
    }




}
