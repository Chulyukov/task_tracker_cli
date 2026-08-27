import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Task {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private static final Pattern JSON_PATTERN = Pattern.compile(
            "\\\"id\\\"\\s*:\\s*(\\d+)\\s*,\\s*\\\"description\\\"\\s*:\\s*\\\"([^\\\"]*)\\\"\\s*,\\s*\\\"status\\\"\\s*:\\s*\\\"([^\\\"]+)\\\"\\s*,\\s*\\\"createdAt\\\"\\s*:\\s*\\\"([^\\\"]+)\\\"\\s*,\\s*\\\"updatedAt\\\"\\s*:\\s*\\\"([^\\\"]+)\\\""
    );
    private static int lastId = 0;
    private int id;
    private String description;
    private TaskStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Task() {
    }

    public Task(String description) {
        this.id = ++lastId;
        this.description = description;
        this.status = TaskStatus.TODO;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public static Task fromJson(String rowData) {
        Matcher matcher = JSON_PATTERN.matcher(rowData);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Invalid task JSON: " + rowData);
        }

        Task task = new Task();
        task.id = Integer.parseInt(matcher.group(1));
        task.description = matcher.group(2);
        task.status = TaskStatus.valueOf(matcher.group(3));
        task.createdAt = LocalDateTime.parse(matcher.group(4), FORMATTER);
        task.updatedAt = LocalDateTime.parse(matcher.group(5), FORMATTER);
        if (lastId < task.getId()) {
            lastId = task.getId();
        }
        return task;
    }

    public String toJson() {
        return "{ \"id\": " + id +
                ", \"description\": \"" + description +
                "\", \"status\": \"" + status +
                "\", \"createdAt\": \"" + FORMATTER.format(createdAt) +
                "\", \"updatedAt\": \"" + FORMATTER.format(updatedAt) + "\" }";
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", createdAt=" + FORMATTER.format(createdAt) +
                ", updatedAt=" + FORMATTER.format(updatedAt) +
                '}';
    }
}
