package model;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity(name = "TIMESLOTS")
@Table(uniqueConstraints= {
        @UniqueConstraint(columnNames = {"DAYSOFWEEK", "STARTTIME", "ENDTIME"})
})
public class TimeSlot {

    //Bit flags are used for the days of week and there are only 7 bits needed for the days
    @Column(nullable = false)
    private byte daysOfWeek;

    @Column(nullable = false)
    private java.time.LocalTime startTime;

    @Column(nullable = false)
    private java.time.LocalTime endTime;

    // Surrogate Key
    @Id
    @Column(name = "TIMESLOT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int timeSlotId;

    // Constructor
    public TimeSlot() {
    }

    public TimeSlot(byte daysOfWeek, java.time.LocalTime startTime, java.time.LocalTime endTime) {
        this.daysOfWeek = daysOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public byte getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(byte daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public int getTimeSlotId() {
        return timeSlotId;
    }

    @Override
    public String toString() {
        return "Days: " + daysOfWeek +
                ". Start time: " + startTime + " End time: " + endTime;
    }
}
