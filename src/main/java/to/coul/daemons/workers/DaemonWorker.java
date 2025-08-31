package to.coul.daemons.workers;


public abstract class DaemonWorker<Task extends DaemonTask> {
    public abstract void process(Task task);
}
