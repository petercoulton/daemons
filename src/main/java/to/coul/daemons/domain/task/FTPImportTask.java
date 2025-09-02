package to.coul.daemons.domain.task;

public record FTPImportTask() implements DaemonTask {
    @Override
    public boolean ackImmediately() {
        return true;
    }
}
