package eus.ehu.cinemaProject.domain;

import jakarta.persistence.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "schedules")
public class Schedule {

    @EmbeddedId
    private ScheduleId id;


    @OneToMany
    private List<ShowTime> showTimes;

    private LocalTime openingTime;
    private LocalTime closingTime;
    private int size;
    private boolean schedule[];


    public Schedule(LocalDate date, ScreeningRoom screeningRoom) {
        this.id = new ScheduleId(date, screeningRoom);
        this.openingTime = screeningRoom.getCinema().getOpeningTime();
        this.closingTime = screeningRoom.getCinema().getClosingTime();
        setSize();
        schedule = new boolean[size];
        setAllFree();
    }

    public Schedule(){}

    public void setSize(){
        //The date will be made up. We don't care about what day is it, we will only use it to treat the case in which closingTime passes 23:59
        LocalDateTime openingDateTime = LocalDateTime.of(2025, 1, 1, openingTime.getHour(), openingTime.getMinute());
        LocalDateTime closingDateTime;
        //If closingTime is after 23:59, add a day to closingTime
        if(closingTime.isBefore(openingTime)){
            closingDateTime = LocalDateTime.of(2025, 1, 2, closingTime.getHour(), closingTime.getMinute());
        }
        else{
            closingDateTime = LocalDateTime.of(2025, 1, 1, closingTime.getHour(), closingTime.getMinute());
        }
        Duration openDuration = Duration.between(openingDateTime, closingDateTime);
        this.size = (int) openDuration.toMinutes() / 15;
    }

    //Given a showtime if it is possible, it will set the showtime in the schedule of the ScreeningRoom
    public void setShowTime(ShowTime showtime){
        LocalTime filmStartingTime = showtime.getScreeningTime();
        LocalTime duration = showtime.getFilm().getDuration();
        if(isFilmInBounds(filmStartingTime, duration)){
            if(isBetweenBoundsFree(filmStartingTime, duration)){
                int bound1 = filmStartTimeScheduleIndex(filmStartingTime);
                int bound2 = filmEndTimeScheduleIndex(bound1, duration);
                bookBetweenBounds(bound1, bound2);
                System.out.println("Successfully reserved at: " + filmStartingTime + " - " + filmStartingTime.plusMinutes(duration.toSecondOfDay() / 60));
                printAllReserves();
            }
            else{
                System.out.println("This ScreeningRoom is not empty between " + filmStartingTime + " - " + filmStartingTime.plusMinutes(duration.toSecondOfDay() / 60));
                System.out.println("This is the availability of the ScreeningRoom in " + id.getDate() + ":");
                printAllReserves();
            }
        }
        else{
            System.out.println("Film starting time: " + filmStartingTime + " or/and duration: " + duration + " are not valid. Try to match this schedule:");
            System.out.println("Opening Time: " + openingTime);
            System.out.println("Closing Time: " + closingTime);
        }
    }

    //Given a film starting time and the film duration, it will determine if the film is between openingTime and closingTime
    public boolean isFilmInBounds(LocalTime filmStartingTime, LocalTime duration){
        int filmEndingTimeInMinutes = filmStartingTime.toSecondOfDay() / 60 + duration.toSecondOfDay() / 60;
        int closingTimeInMinutes = closingTime.toSecondOfDay() / 60;
        // If closingTime is after 23:59, then add 1440 minutes (one day)
        if(closingTime.isBefore(openingTime)){
            closingTimeInMinutes += 1440;
        }
        LocalTime filmEndingTime = LocalTime.of((filmEndingTimeInMinutes / 60) % 24, filmEndingTimeInMinutes % 60); //Only for debugging
        System.out.println(String.format("Film starting time: %s, Film ending time: %s", filmStartingTime.toString(), filmEndingTime.toString())); //Only for debugging
        //Ending time is treated differently to catch the cases in which time exceeds the day (>23:59)
        return openingTime.isBefore(filmStartingTime) && filmEndingTimeInMinutes < closingTimeInMinutes;
    }

    //Given a film and the film duration, it will determine if there are no other films set at the same time
    public boolean isBetweenBoundsFree(LocalTime filmStartingTime, LocalTime duration){
        int startingIndex = filmStartTimeScheduleIndex(filmStartingTime);
        int endingIndex = filmEndTimeScheduleIndex(startingIndex, duration);
        for(int i = startingIndex; i < endingIndex + 1; i++){
            if(schedule[i]){
                return false;
            }
        }
        return true;
    }

    //Given a startingIndex and endingIndex, it will book every 15 minutes frame between those bounds
    //If a film finishes at 17:00, we will book also 17:00 in order to have a minimum of 15 minutes between film and film
    public void bookBetweenBounds(int startingIndex, int endingIndex){
        for (int i = startingIndex; i < endingIndex + 1; i++) {
            schedule[i] = true;
        }
    }

    //Given a film starting time, it will determine which index represents that starting time
    public int filmStartTimeScheduleIndex(LocalTime filmStartingTime){
        int differenceInMinutes = (filmStartingTime.toSecondOfDay() / 60) - (openingTime.toSecondOfDay()/60); //Difference between cinema opening time and film starting time
        int startingTimeIndex = (int)Math.ceil(differenceInMinutes / 15.0); //Number of schedule[] index
        return startingTimeIndex;
    }

    //Given a film starting time index and the film duration, it will determine which index represents the finishing time
    public int filmEndTimeScheduleIndex(int startingTimeIndex, LocalTime duration){
        int indexesToJump = (int)Math.ceil((duration.toSecondOfDay() /60 )/ 15.0);
        return startingTimeIndex + indexesToJump;
    }

    //It sets every 15 minutes frame as free
    public void setAllFree() {
        for (int i = 0; i < size; i++) {
            schedule[i] = false;
        }
    }

    //It prints weather every 15 minutes frame is free or not: false --> free
    public void printAllReserves(){
        LocalTime time = openingTime;
        for(Boolean isReserved: schedule){
            System.out.println(time + ": " + isReserved);
            time = time.plusMinutes(15);
        }
    }

    public LocalDate getDate(){
        return id.getDate();
    }
    public ScreeningRoom getScreeningRoom(){return id.getScreeningRoom();}

}