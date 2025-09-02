package to.coul.daemons.application.invoker;

import to.coul.daemons.domain.task.DaemonTask;

import java.util.concurrent.CompletionStage;

public interface TaskInvoker {
    <T extends DaemonTask> CompletionStage<Void> invoke(T task);
}
