# Task Tracker CLI

A simple command-line task manager for creating, updating, deleting, and listing tasks.

## Overview

This project does not require Maven, Gradle, or any build tool. It works with a standard Java JDK installed on the machine.

The CLI stores task data in a local `tasks.json` file in the project root and supports the following commands:

- `add <description>`
- `update <id> <description>`
- `delete <id>`
- `mark-in-progress <id>`
- `mark-done <id>`
- `list`
- `list <status>`
- `-h` / `--help`

## Requirements

Before using the CLI, make sure Java is installed:

```bash
java -version
javac -version
```

If both commands work, the project is ready to run.

## Running the CLI from the project root

From the repository root, run:

```bash
./task-tracker <command> [arguments]
```

On Windows:

```bat
task-tracker.bat <command> [arguments]
```

The script automatically compiles the Java sources into the `out/` directory and runs the `CLI` main class.

## Commands

### 1. Add a task

```bash
./task-tracker add "Buy groceries"
```

Returns the created task ID.

Example output:

```text
1
```

### 2. Update a task

```bash
./task-tracker update 1 "Buy groceries and milk"
```

Updates the description of task `1`.

### 3. Delete a task

```bash
./task-tracker delete 1
```

Deletes task `1`.

### 4. Mark a task as in progress

```bash
./task-tracker mark-in-progress 1
```

Changes the task status to `in-progress`.

### 5. Mark a task as done

```bash
./task-tracker mark-done 1
```

Changes the task status to `done`.

### 6. List all tasks

```bash
./task-tracker list
```

Prints every task stored in `tasks.json`.

### 7. List tasks by status

```bash
./task-tracker list todo
./task-tracker list in-progress
./task-tracker list done
```

Allowed statuses:

- `todo`
- `in-progress`
- `done`

### 8. Help

```bash
./task-tracker --help
```

or:

```bash
./task-tracker -h
```

This prints the usage guide and command list.

## Supported statuses

Tasks can have one of the following statuses internally:

- `TODO`
- `IN_PROGRESS`
- `DONE`

The CLI accepts the user-facing lowercase values:

- `todo`
- `in-progress`
- `done`

## Example workflow

```bash
./task-tracker add "Write project documentation"
./task-tracker mark-in-progress 1
./task-tracker update 1 "Write project documentation and examples"
./task-tracker mark-done 1
./task-tracker list done
```

## Notes

- The CLI does not use a database; it persists tasks in `tasks.json`.
- If `tasks.json` does not exist, the program creates it automatically on first write.
- Invalid commands and invalid IDs produce an error message and exit with a non-zero status.
- Generated compilation output is stored in the `out/` directory and is ignored by Git.

## Quick start

```bash
chmod +x task-tracker
./task-tracker --help
```

Then start using the CLI immediately from the repository root.
