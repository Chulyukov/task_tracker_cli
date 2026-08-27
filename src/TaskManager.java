import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskManager {
    private final static Path FILE_PATH = Path.of("tasks.json");
    private List<Task> tasks;

    public TaskManager() {
        this.tasks = loadTasks();
    }

    private List<Task> loadTasks() {
        List<Task> tasks = new ArrayList<>();
        if (Files.notExists(FILE_PATH)) {
            return tasks;
        }
        try {
            String content = Files.readString(FILE_PATH).trim();
            if (content.isEmpty() || "[]".equals(content)) {
                return tasks;
            }
            String normalized = content.substring(1, content.length() - 1).trim();
            if (normalized.isEmpty()) {
                return tasks;
            }
            String[] data = normalized.split("\\s*},\\s*");
            for (String rowData : data) {
                String cleaned = rowData.trim();
                if (!cleaned.startsWith("{")) {
                    cleaned = "{" + cleaned;
                }
                if (!cleaned.endsWith("}")) {
                    cleaned = cleaned + "}";
                }
                tasks.add(Task.fromJson(cleaned));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return tasks;
    }

    private Optional<Task> findById(int id) {
        return tasks.stream().filter(t -> t.getId() == id).findFirst();
    }

    public int add(String description) {
        Task task = new Task(description);
        tasks.add(task);
        return task.getId();
    }

    public void update(int id, String newDescription) {
        Task task = findById(id).orElseThrow(() -> new RuntimeException("Task with id = " + id + " is not found."));
        task.setDescription(newDescription);
        task.setUpdatedAt(LocalDateTime.now());
    }

    public void delete(int id) {
        Task task = findById(id).orElseThrow(() -> new RuntimeException("Task with id = " + id + " is not found."));
        tasks.remove(task);
    }

    public void markInProgress(int id) {
        Task task = findById(id).orElseThrow(() -> new RuntimeException("Task with id = " + id + " is not found."));
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setUpdatedAt(LocalDateTime.now());
    }

    public void markDone(int id) {
        Task task = findById(id).orElseThrow(() -> new RuntimeException("Task with id = " + id + " is not found."));
        task.setStatus(TaskStatus.DONE);
        task.setUpdatedAt(LocalDateTime.now());
    }

    public List<Task> list() {
        return tasks;
    }

    public List<Task> list(String statusStr) {
        List<Task> result;
        switch (statusStr) {
            case "done" -> result = tasks.stream().filter(t -> t.getStatus() == TaskStatus.DONE).toList();
            case "todo" -> result = tasks.stream().filter(t -> t.getStatus() == TaskStatus.TODO).toList();
            case "in-progress" -> result = tasks.stream().filter(t -> t.getStatus() == TaskStatus.IN_PROGRESS).toList();
            default -> result = new ArrayList<>();
        }
        return result;
    }

    public void executeUpdate() {
        if (Files.notExists(FILE_PATH)) {
            try {
                Files.createFile(FILE_PATH);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        StringBuilder dataBuilder = new StringBuilder();
        dataBuilder.append("[\n");
        for (Task task : tasks) {
            dataBuilder.append(task.toJson()).append(",\n");
        }
        if (!tasks.isEmpty()) {
            dataBuilder.deleteCharAt(dataBuilder.length() - 2);
        }
        dataBuilder.append("]");
        try {
            Files.writeString(FILE_PATH, dataBuilder.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
