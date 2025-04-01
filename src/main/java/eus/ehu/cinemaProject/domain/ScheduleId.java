package eus.ehu.cinemaProject.domain;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

@Embeddable
public class ScheduleId implements Serializable {

    private LocalDate date;

    @ManyToOne
    private ScreeningRoom screeningRoom;


    public ScheduleId() {}


    public ScheduleId(LocalDate date, ScreeningRoom screeningRoom) {
        this.date = date;
        this.screeningRoom = screeningRoom;
    }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public ScreeningRoom getScreeningRoom() { return screeningRoom; }
    public void setScreeningRoom(ScreeningRoom screeningRoom) { this.screeningRoom = screeningRoom; }

    // Necessary for the super keys
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduleId that = (ScheduleId) o;
        return Objects.equals(date, that.date) &&
                Objects.equals(screeningRoom, that.screeningRoom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, screeningRoom);
    }
}
