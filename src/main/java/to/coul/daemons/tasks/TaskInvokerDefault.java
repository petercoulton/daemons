package to.coul.daemons.tasks;

import to.coul.daemons.workers.WorkerRegistry;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class TaskInvokerDefault implements TaskInvoker {

    @Inject
    WorkerRegistry registry;

    @Override
    public CompletionStage<Void> invoke(final DaemonTask task) {
        return CompletableFuture.runAsync(() -> {
            this.registry.withWorker(task, worker -> {
                worker.process(task);
                Log.info("Task completed: " + task.getClass().getName());
            });
        });
    }
}
