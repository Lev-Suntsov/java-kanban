import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Subtask extends  Task{
    public int getApicId() {
        return apicId;
    }

    public void setApicId(int apicId) {
        this.apicId = apicId;
    }

    private int apicId;
    Subtask(String name, String description, int apicId, HashMap<Integer, Epic> epics){
      super(name, description);
      this.setName(name);
      this.setDescription(description);
      this.setId(name.hashCode());
      this.setStatus(Status.TaskStatus.NEW);
      epics.get(apicId).getIdSubtask().add(this.getId());
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status=" + getStatus() +
                "apicId=" + apicId +
                '}';
    }
}

