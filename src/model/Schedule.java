package model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Schedule implements Schedulable {
    private String name;
    private LocalDate date;
    private String memo;

    public Schedule(String name, LocalDate date, String memo) {
        this.name = name;
        this.date = date;
        this.memo = memo == null ? "" : memo;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getMemo() { return memo; }
    public void setMemo(String memo) { this.memo = memo; }

    public long getRemainingDays() {
        return ChronoUnit.DAYS.between(LocalDate.now(), date);
    }
}
