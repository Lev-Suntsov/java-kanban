package model;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {
    public Duration duration;
    public LocalDateTime startTime;

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    private int epicId;

    public Subtask(String name, String description, int epicId, LocalDateTime startTime, Duration duration) {
        super(name, description, startTime, duration);
        this.setName(name);
        this.setDescription(description);
        this.setId(name.hashCode());
        this.setStatus(Status.TaskStatus.NEW);
        this.epicId = epicId;
        this.startTime = startTime;
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subtask subtask = (Subtask) o;
        return Objects.equals(epicId, subtask.epicId);
    }


    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }

    @Override
    public String toString() {
        return "model: Subtask, " + "name:  " + getName() + ", description: " + getDescription() + ", id: " + getId() + ", status: " + getStatus() + "apicId: " + epicId + ", startTime: " + startTime.getYear() + "." + startTime.getMonth() + "." + startTime.getDayOfMonth() + "." + startTime.getHour() + "." + startTime.getMinute() + ", duriator: " + duration.toDays() + "." + duration.toHours() + "." + duration.toMinutes() + '}';
    }

    public LocalDateTime getEndTime() {
        return startTime.plusDays(duration.toDays()).plusHours(duration.toHours()).plusMinutes(duration.toMinutes());
    }
}

