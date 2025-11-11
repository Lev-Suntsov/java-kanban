package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private String name;
    private String description;
    private int id;
    private Status.TaskStatus status;
    private Duration duration;
    public LocalDateTime startTime;

    public Task(String name, String description, LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.description = description;
        id = name.hashCode();
        status = Status.TaskStatus.NEW;
        this.startTime = startTime;
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Status.TaskStatus getStatus() {
        return status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) {

        this.id = id;

    }

    public void setStatus(Status.TaskStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "model.Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", startTime: " + startTime.getYear() + "." + startTime.getMonth()
                + "." + startTime.getDayOfMonth() + "." + startTime.getHour() + "." + startTime.getMinute() +
                ", duriator: " + duration.toDays() + "." + duration.toHours() + "." + duration.toMinutes() +
                '}';
    }

    public LocalDateTime getEndTime() {
        return startTime.plusDays(duration.toDays()).plusHours(duration.toHours()).plusMinutes(duration.toMinutes());
    }

}
