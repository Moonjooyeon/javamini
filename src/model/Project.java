package model;

import java.time.LocalDate;

public class Project extends Work {
    private String owner;
    private LocalDate startDate;
    private LocalDate dueDate;

    public Project(String title, String status, String owner, LocalDate startDate, LocalDate dueDate) {
        super(title, status);
        this.owner = owner;
        this.startDate = startDate;
        this.dueDate = dueDate;
    }

    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
}
