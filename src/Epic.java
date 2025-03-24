import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task{
    private ArrayList<Integer> idSubtask = new ArrayList<>();
    Epic(String name, String dedescription){
        super(name,dedescription );
        this.setName(name);
        this.setDescription(dedescription);
        this.setId(name.hashCode());
        this.setStatus(Status.TaskStatus.NEW);
        System.out.println("Успешно добавлено");
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(idSubtask, epic.idSubtask);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idSubtask);
    }

    public ArrayList<Integer> getIdSubtask() {
        return idSubtask;
    }

    public void setIdSubtask(ArrayList<Integer> idSubtask) {
        this.idSubtask = idSubtask;
    }

    @Override
    public String toString() {
        if (idSubtask.isEmpty()){
            return "Epic{" + "name='" + getName() + '\'' +
                    ", description='" + getDescription() + '\'' +
                    ", id=" + getId() +
                    ", status=" + getStatus() +
                    "idSubtask= 0}";
        } else {
            return "Epic{" + "name='" + getName() + '\'' +
                    ", description='" + getDescription() + '\'' +
                    ", id=" + getId() +
                    ", status=" + getStatus() +
                    ", idSubtask=" + idSubtask +
                    '}';
        }
    }
}
