package model;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Integer> subtaskIds = new ArrayList<>();
   public Epic(String name, String dedescription){
        super(name,dedescription );
        this.setName(name);
        this.setDescription(dedescription);
        this.setId(name.hashCode());
        this.setStatus(Status.TaskStatus.NEW);
        System.out.println("Успешно добавлено");
    }

    public  void cleanSubtaskIds(){
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

    @Override
    public String toString() {
        if (subtaskIds.isEmpty()){
            return "model.Epic{" + "name='" + getName() + '\'' +
                    ", description='" + getDescription() + '\'' +
                    ", id=" + getId() +
                    ", status=" + getStatus() +
                    "idSubtask= 0}";
        } else {
            return "model.Epic{" + "name='" + getName() + '\'' +
                    ", description='" + getDescription() + '\'' +
                    ", id=" + getId() +
                    ", status=" + getStatus() +
                    ", idSubtask=" +    subtaskIds +
                    '}';
        }
    }
}
