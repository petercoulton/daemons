package to.coul.daemons.tasks;

public record FTPImportTask() implements DaemonTask {
    @Override
    public boolean ackImmediately() {
        return true;
    }
}
