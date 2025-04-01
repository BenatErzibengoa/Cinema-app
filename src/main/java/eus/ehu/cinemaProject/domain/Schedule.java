//WORKS
package eus.ehu.cinemaProject.domain;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Schedule {

    private LocalTime openingTime;
    private LocalTime endingTime;
    private int size;
    private boolean schedule[];


    public static void main(String[] args) {
        Schedule schedule = new Schedule(LocalTime.of(16, 00), LocalTime.of(23, 59));
        schedule.setFilm(LocalTime.of(12,30), LocalTime.of(2,15));
        schedule.setFilm(LocalTime.of(16,00), LocalTime.of(2,15));
        schedule.setFilm(LocalTime.of(18,45), LocalTime.of(4,22));
        schedule.printAllReserves();
    }


    public Schedule(LocalTime openingTime, LocalTime endingTime) {
        this.openingTime = openingTime;
        this.endingTime = endingTime;
        Duration duration = Duration.between(openingTime, endingTime);
        this.size = (int)duration.toHours() * 4 + duration.toMinutesPart()/15;
        schedule = new boolean[size];
        setAllFree();
    }

    public void setFilm(LocalTime filmStartingTime, LocalTime duration){
        if(isFilmInBounds(filmStartingTime, duration) && isBoundFree(filmStartingTime, duration) ){
            int bound1 = filmStartTimeScheduleIndex(filmStartingTime);
            int bound2 = filmEndTimeScheduleIndex(bound1, duration);
            bookBetweenBounds(bound1, bound2);
        }
        else{
            System.out.println("Film starting time: " + filmStartingTime + " or duration: " + duration +" are not valid");
        }
    }

    public boolean isFilmInBounds(LocalTime filmStartingTime, LocalTime duration){
        int filmEndingTimeInMinutes = filmStartingTime.toSecondOfDay() / 60 + duration.toSecondOfDay() / 60;
        int closingTimeInMinutes = endingTime.toSecondOfDay() / 60;

        LocalTime filmEndingTime = LocalTime.of((filmEndingTimeInMinutes / 60) % 24, filmEndingTimeInMinutes % 60); //Only for debugging
        System.out.println(String.format("Film starting time: %s, Film ending time: %s", filmStartingTime.toString(), filmEndingTime.toString())); //Only for debugging
        //Ending time is treated differently to catch the cases in which time exceeds the day (>23:59)
        return areOrdered(openingTime, filmStartingTime) && filmEndingTimeInMinutes < closingTimeInMinutes;
    }

    public boolean isBoundFree(LocalTime filmStartingTime, LocalTime duration){
        int startingIndex = filmStartTimeScheduleIndex(filmStartingTime);
        int endingIndex = filmEndTimeScheduleIndex(startingIndex, duration);
        for(int i = startingIndex; i < endingIndex + 1; i++){
            if(schedule[i]){
                return false;
            }
        }
        return true;
    }

    //If a film finishes at 17:00, we will book also 17:00 in order to have a minimum of 15 minutes between film and film
    public void bookBetweenBounds(int startingIndex, int endingIndex){
        for (int i = startingIndex; i < endingIndex + 1; i++) {
            schedule[i] = true;
        }
    }

    public int filmStartTimeScheduleIndex(LocalTime filmStartingTime){
        int differenceInMinutes = (filmStartingTime.toSecondOfDay() / 60) - (openingTime.toSecondOfDay()/60); //Difference between cinema opening time and film starting time
        int startingTimeIndex = (int)Math.ceil(differenceInMinutes / 15.0); //Number of schedule[] index
        return startingTimeIndex;
    }

    public int filmEndTimeScheduleIndex(int startingTimeIndex, LocalTime duration){
        int indexesToJump = (int)Math.ceil((duration.toSecondOfDay() /60 )/ 15.0);
        return startingTimeIndex + indexesToJump;
    }


    public boolean areOrdered(LocalTime time1, LocalTime time2){
        return Duration.between(time1,time2).toMinutes() >= 0;
    }

    public void setAllFree() {
        for (int i = 0; i < size; i++) {
            schedule[i] = false;
        }
    }

    public void printAllReserves(){
        LocalTime time = openingTime;
        for(Boolean isReserved: schedule){
            System.out.println(time + ": " + isReserved);
            time = time.plusMinutes(15);
        }
    }

}