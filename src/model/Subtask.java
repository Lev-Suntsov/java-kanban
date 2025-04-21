package model;

import controllers.InMemoryTaskManager;

import java.util.HashMap;
import java.util.Objects;

public class Subtask extends Task {
    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    private int epicId;
    public Subtask(String name, String description, int epicId){
      super(name, description);
      this.setName(name);
      this.setDescription(description);
      this.setId(name.hashCode());
      this.setStatus(Status.TaskStatus.NEW);
      this.epicId = epicId;

    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subtask subtask = (Subtask) o;
        return  Objects.equals(epicId, subtask.epicId);
    }


    @Override
    public int hashCode() {
        return  Objects.hash(super.hashCode(), epicId);
    }

    @Override
    public String toString() {
        return "model.Subtask{" +
                "name= '" + getName() + '\'' +
                ", description= '" + getDescription() + '\'' +
                ", id= " + getId() +
                ", status= " + getStatus() +
                "apicId= " + epicId +
                '}';
    }
}

