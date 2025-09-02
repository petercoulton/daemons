package to.coul.daemons.application.invoker;

import to.coul.daemons.domain.task.DaemonTask;
import to.coul.daemons.application.messaging.WorkerRegistry;

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
                Log.info("Task started: " + task.getClass().getSimpleName());

                worker.process(task);

                Log.info("Task completed: " + task.getClass().getSimpleName());
            });
        });
    }
}
