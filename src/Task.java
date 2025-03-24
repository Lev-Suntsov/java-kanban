import java.util.Objects;
public class Task  {
    private String name;
    private String description;
    private int id;
    private Status.TaskStatus status;

    Task(String name, String description){
        this.name = name;
        this.description = description;
        id = name.hashCode();
        status = Status.TaskStatus.NEW;
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

    public String getName(){
        return name;
    }
    public int getId(){
        return id;
    }
    public String getDescription(){
        return description;
    }
    public  Status.TaskStatus getStatus(){
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
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }
}
