public enum TaskStatus {
    TODO("Todo"),
    IN_PROGRESS("In progress"),
    DONE("Done");

    private final String value;

    TaskStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
