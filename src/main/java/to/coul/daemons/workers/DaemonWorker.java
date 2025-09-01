package to.coul.daemons.workers;


import to.coul.daemons.tasks.DaemonTask;

public abstract class DaemonWorker<T extends DaemonTask> {
    public abstract Class<T> getTaskType();
    public abstract void process(T task);
}
