package to.coul.daemons.tasks;

import java.util.concurrent.CompletionStage;

public interface TaskInvoker {
    <T extends DaemonTask> CompletionStage<Void> invoke(T task);
}
