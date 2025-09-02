package to.coul.daemons.domain.worker;


import to.coul.daemons.domain.task.DaemonTask;

public abstract class DaemonWorker<T extends DaemonTask> {
    public abstract Class<T> getTaskType();
    public abstract void process(T task);
}
