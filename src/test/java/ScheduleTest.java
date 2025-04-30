import eus.ehu.cinemaProject.domain.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ScheduleTest {
    Schedule schedule;
    ShowTime showTime;

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

    //Check if the frames that the film has occupied have been booked. Before 00:00 - Before 00:00 conditions
    @Test
    public void dayDayBooking() {
        LocalTime startHour = LocalTime.of(17, 45);
        LocalTime duration = LocalTime.of(1, 30);
        schedule.bookBetweenTimeBounds(startHour, duration);
        assertTrue(!schedule.isBetweenBoundsFree(startHour, duration));
    }

    //Check if the frames that the film has occupied have been booked. Before 00:00 - After 00:00 conditions
    @Test
    public void dayNightBooking() {
        LocalTime startHour = LocalTime.of(23, 45);
        LocalTime duration = LocalTime.of(0, 45);
        schedule.bookBetweenTimeBounds(startHour, duration);
        assertTrue(!schedule.isBetweenBoundsFree(startHour, duration));
    }

    //Check if the frames that the film has occupied have been booked. After 00:00 - After 00:00 conditions
    @Test
    public void nightNightBooking() {
        LocalTime startHour = LocalTime.of(0, 0);
        LocalTime duration = LocalTime.of(1, 0);
        schedule.bookBetweenTimeBounds(startHour, duration);
        assertTrue(!schedule.isBetweenBoundsFree(startHour, duration));
    }

    //Check if the schedule works with real ShowTime: Before 00:00 - Before 00:00 conditions
    @Test
    public void dayDayShowTimeBooking() {
        showTime = new ShowTime(schedule, LocalTime.of(17, 30), new Film(null, null, LocalTime.of(1, 30), null, null, null));
        schedule.setShowTime(showTime);
        assertTrue(!schedule.isBetweenBoundsFree(showTime.getScreeningTime(), showTime.getFilm().getDuration()));
    }

    //Check if the schedule works with real ShowTime: Before 00:00 - After 00:00 conditions
    @Test
    public void dayNightShowTimeBooking() {
        showTime = new ShowTime(schedule, LocalTime.of(23, 45), new Film(null, null, LocalTime.of(1, 15), null, null, null));
        schedule.setShowTime(showTime);
        assertTrue(!schedule.isBetweenBoundsFree(showTime.getScreeningTime(), showTime.getFilm().getDuration()));
    }

    //Check if the schedule works with real ShowTimes: After 00:00 - After 00:00 conditions
    @Test
    public void nightNightShowTimeBooking() {
        showTime = new ShowTime(schedule, LocalTime.of(00, 15), new Film(null, null, LocalTime.of(00, 30), null, null, null));
        assertTrue(schedule.setShowTime(showTime));
        assertTrue(!schedule.isBetweenBoundsFree(showTime.getScreeningTime(), showTime.getFilm().getDuration()));
    }

    //Booking 2 ShowTimes at the same time
    @Test
    public void repeatShowTimeBooking() {
        showTime = new ShowTime(schedule, LocalTime.of(00, 15), new Film(null, null, LocalTime.of(00, 30), null, null, null));
        ShowTime showTime2 = new ShowTime(schedule, LocalTime.of(00, 15), new Film(null, null, LocalTime.of(00, 30), null, null, null));
        assertTrue(schedule.setShowTime(showTime));
        assertTrue(!schedule.setShowTime(showTime2));
    }


    @AfterEach
    public void printAllReserves(){
        schedule.printAllReserves();
    }



}
