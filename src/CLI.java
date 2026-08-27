import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class CLI {
    private static final String HELP_TEXT = """
            Task Tracker CLI - A simple command-line tool to manage your tasks.

            USAGE:
                task-tracker <command> [arguments]

            COMMANDS:
                add <description>          Add a new task. Returns the task ID.
                update <id> <description>  Update the description of an existing task.
                delete <id>                Delete a task by its ID.
                mark-in-progress <id>      Change a task status to "in-progress".
                mark-done <id>             Change a task status to "done".
                list                       List all tasks.
                list <status>              List tasks filtered by status.
                                           Allowed statuses: todo, in-progress, done.

            OPTIONS:
                -h, --help                 Show this help message and exit.

            EXAMPLES:
                task-tracker add "Buy groceries"
                task-tracker update 1 "Buy groceries and milk"
                task-tracker mark-in-progress 1
                task-tracker list in-progress
            """;

    private static void handleWrongCommand() {
        System.err.println("It's wrong command. Use -h or --help to find the right one.");
    }

    private static void printTasks(List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }
        for (Task task : tasks) {
            System.out.println(task);
        }
    }

    private static int parseId(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid task ID: " + value);
        }
    }

    public static void main(String[] args) {
        if (args.length == 0 || "-h".equals(args[0]) || "--help".equals(args[0])) {
            System.out.println(HELP_TEXT);
            return;
        }

        TaskManager taskManager = new TaskManager();

        try {
            switch (args[0]) {
                case "add" -> {
                    if (args.length < 2) {
                        handleWrongCommand();
                        return;
                    }
                    String description = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                    int id = taskManager.add(description);
                    taskManager.executeUpdate();
                    System.out.println(id);
                }
                case "update" -> {
                    if (args.length < 3) {
                        handleWrongCommand();
                        return;
                    }
                    int id = parseId(args[1]);
                    String description = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
                    taskManager.update(id, description);
                    taskManager.executeUpdate();
                    System.out.println("Task updated successfully.");
                }
                case "delete" -> {
                    if (args.length != 2) {
                        handleWrongCommand();
                        return;
                    }
                    int id = parseId(args[1]);
                    taskManager.delete(id);
                    taskManager.executeUpdate();
                    System.out.println("Task deleted successfully.");
                }
                case "mark-in-progress" -> {
                    if (args.length != 2) {
                        handleWrongCommand();
                        return;
                    }
                    int id = parseId(args[1]);
                    taskManager.markInProgress(id);
                    taskManager.executeUpdate();
                    System.out.println("Task marked as in progress.");
                }
                case "mark-done" -> {
                    if (args.length != 2) {
                        handleWrongCommand();
                        return;
                    }
                    int id = parseId(args[1]);
                    taskManager.markDone(id);
                    taskManager.executeUpdate();
                    System.out.println("Task marked as done.");
                }
                case "list" -> {
                    if (args.length == 1) {
                        printTasks(taskManager.list());
                    } else if (args.length == 2) {
                        String status = args[1].toLowerCase(Locale.ROOT);
                        printTasks(taskManager.list(status));
                    } else {
                        handleWrongCommand();
                    }
                }
                default -> handleWrongCommand();
            }
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
