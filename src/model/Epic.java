package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Integer> subtaskIds = new ArrayList<>();
    public LocalDateTime startTime;
    public Duration duration;
    private LocalDateTime endTime;

    public Epic(String name, String dedescription, LocalDateTime startTime, Duration duration) {
        super(name, dedescription, startTime, duration);
        this.setName(name);
        this.setDescription(dedescription);
        this.setId(name.hashCode());
        this.setStatus(Status.TaskStatus.NEW);
        System.out.println("Успешно добавлено");
    }

    public void cleanSubtaskIds() {
        subtaskIds.clear();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtaskIds, epic.subtaskIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtaskIds);
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void setSubtaskIds(ArrayList<Integer> idSubtask) {
        this.subtaskIds = subtaskIds;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        if (subtaskIds.isEmpty()) {
            return "model.Epic{" + "name='" + getName() + '\'' +
                    ", description='" + getDescription() + '\'' +
                    ", id=" + getId() +
                    ", status=" + getStatus() +
                    "idSubtask= 0}";
        } else {
            return "model: Epic, " + "name: '" + getName() +
                    ", description: " + getDescription() +
                    ", id: " + getId() +
                    ", status: " + getStatus() +
                    ", idSubtask: " + subtaskIds +
                    ", startTime: " + startTime.getYear() + "." + startTime.getMonth()
                    + "." + startTime.getDayOfMonth() + "." + startTime.getHour() + "." + startTime.getMinute() +
                    ", duriator: " + duration.toDays() + "." + duration.toHours() + "." + duration.toMinutes() +
                    '}';
        }
    }
}
