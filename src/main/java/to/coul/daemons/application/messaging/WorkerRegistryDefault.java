package to.coul.daemons.application.messaging;

import to.coul.daemons.domain.worker.DaemonWorker;
import to.coul.daemons.domain.worker.ForTaskLiteral;
import to.coul.daemons.domain.task.DaemonTask;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;


@ApplicationScoped
public class WorkerRegistryDefault implements WorkerRegistry {

    @Inject @Any
    Instance<DaemonWorker<?>> workers;

    @Override
    @SuppressWarnings("unchecked")
    public <T extends DaemonTask> DaemonWorker<T> resolve(T task) {
        final Class<? extends DaemonTask> taskType = task.getClass();
        Instance<DaemonWorker<?>> selected = workers.select(new ForTaskLiteral(taskType));
        if (selected.isUnsatisfied()) {
            throw new IllegalStateException("No worker for " + taskType.getName());
        }
        if (selected.isAmbiguous()) {
            throw new IllegalStateException("Multiple workers for " + taskType.getName());
        }
        return (DaemonWorker<T>) selected.get();
    }

    @Override
    public void destroy(DaemonWorker<?> worker) {
        workers.destroy(worker);
    }
}
