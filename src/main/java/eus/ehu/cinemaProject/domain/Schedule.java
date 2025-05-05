package eus.ehu.cinemaProject.domain;

import jakarta.persistence.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "schedules")
public class Schedule {

    @EmbeddedId
    private ScheduleId id;

    @ManyToOne
    @MapsId("screeningRoom") // Maps the composite key's screeningRoom part
    @JoinColumn(name = "screeningRoom_roomNumber", insertable = false, updatable = false)
    private ScreeningRoom screeningRoom;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShowTime> showTimes = new ArrayList<>();


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
    public boolean setShowTime(ShowTime showtime){
        LocalTime filmStartingTime = showtime.getScreeningTime();
        LocalTime duration = showtime.getFilm().getDuration();
        if(isFilmInBounds(filmStartingTime, duration)){
            if(isBetweenBoundsFree(filmStartingTime, duration)){
                bookBetweenTimeBounds(filmStartingTime, duration);
                showTimes.add(showtime);
                showtime.setSchedule(this);
                System.out.println("Successfully reserved at: " + filmStartingTime + " - " + filmStartingTime.plusMinutes(duration.toSecondOfDay() / 60));
                return true;
            }
            else{
                System.out.println("This ScreeningRoom is not empty between " + filmStartingTime + " - " + filmStartingTime.plusMinutes(duration.toSecondOfDay() / 60));
                System.out.println("This is the availability of the ScreeningRoom in " + id.getDate() + ":");
                //printAllReserves();
                return false;
            }
        }
        else{
            System.out.println("Film starting time: " + filmStartingTime + " or/and duration: " + duration + " are not valid. Try to match this schedule:");
            System.out.println("Opening Time: " + openingTime);
            System.out.println("Closing Time: " + closingTime);
            return false;
        }
    }

    //Given a film starting time and the film duration, it will determine if the film is between openingTime and closingTime
    public boolean isFilmInBounds(LocalTime filmStartingTime, LocalTime duration) {
        int openingMinutes = openingTime.toSecondOfDay() / 60;
        int closingMinutes = closingTime.toSecondOfDay() / 60;
        int startMinutes = filmStartingTime.toSecondOfDay() / 60;
        int durationMinutes = duration.toSecondOfDay() / 60;
        int endMinutes = startMinutes + durationMinutes;

        // If it closes after 00:00
        if (closingTime.isBefore(openingTime)) {
            // If the film starts after 00:00 but not after opening time
            if (startMinutes < openingMinutes) startMinutes += 1440;
            // If the film ends after 00:00 but not after opening time
            if (endMinutes < openingMinutes) endMinutes += 1440;
            closingMinutes += 1440;
        }
        int frameSize = 15;
        // Check if the film starts after cinema is opened and ends before the cinema is closed
        return startMinutes >= openingMinutes && endMinutes <= (closingMinutes - frameSize);
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

    //This is the method that should be used to book a film.
    // It will book every 15 minutes frame between the starting time and the ending time
    public void bookBetweenTimeBounds(LocalTime startingTime, LocalTime endingTime){
        int startingIndex = filmStartTimeScheduleIndex(startingTime);
        int endingIndex = filmEndTimeScheduleIndex(startingIndex, endingTime);
        bookBetweenBounds(startingIndex, endingIndex);
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
        int openingMinutes = openingTime.toSecondOfDay() / 60;
        int filmStartMinutes = filmStartingTime.toSecondOfDay() / 60;

        //If the cinema closes after 00:00 and the film is after 00:00 (before opening time)
        if (closingTime.isBefore(openingTime) && filmStartMinutes < openingMinutes) {
            filmStartMinutes += 1440; // Sum 24 hours in minutes
        }

        int differenceInMinutes = filmStartMinutes - openingMinutes;
        return (int) Math.ceil(differenceInMinutes / 15.0);
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
    public LocalTime getOpeningTime(){return openingTime;}
    public LocalTime getClosingTime(){return closingTime;}
    public ScreeningRoom getScreeningRoom(){return id.getScreeningRoom();}

    //Considering movie duration returns valid starting times in this schedule
    public List<LocalTime> getAvailableStartTimes(LocalTime duration){
        List<LocalTime> availableTimes = new ArrayList<>();
        LocalTime currentTime = this.openingTime;

        for (int i = 0; i < size; i++) {
            if (!this.isFilmInBounds(currentTime, duration)) {
                currentTime = currentTime.plusMinutes(15);
                continue; // ← was "break"
            }
            if (this.isBetweenBoundsFree(currentTime, duration)) {
                availableTimes.add(currentTime);
            }
            currentTime = currentTime.plusMinutes(15);
        }

        return availableTimes;
    }


    public void reconstructBookingStateFromShowTimes() {
        setAllFree(); // Clear state first

        List<ShowTime> copy = new ArrayList<>(showTimes);
        for (ShowTime st : copy) {
            addShowTimeInternal(st);  // <- Pure logic, no logging or checks
        }
    }


    private void addShowTimeInternal(ShowTime showTime) {
        LocalTime start = showTime.getScreeningTime();
        LocalTime duration = showTime.getFilm().getDuration();

        // Directly book time bounds (no validation)
        bookBetweenTimeBounds(start, duration);

        // Add to internal list, but avoid duplicates
        if (!showTimes.contains(showTime)) {
            showTimes.add(showTime);
        }
    }

    public List<ShowTime> getShowTimes() {
        return showTimes;
    }

    public int getSize() {
        return size;
    }
}