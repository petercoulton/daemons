package to.coul.daemons.workers;

import to.coul.daemons.tasks.DaemonTask;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public interface WorkerRegistry {
    <T extends DaemonTask> DaemonWorker<T> resolve(T taskType);
    void destroy(DaemonWorker<?> worker);

    default <T extends DaemonTask> void withWorker(T task, Consumer<DaemonWorker<T>> func) {
        DaemonWorker<T> worker = this.resolve(task);
        try {
            func.accept(worker);
        } finally {
            this.destroy(worker);
        }
    }

    default <T extends DaemonTask, R> R withWorker(T task, Function<DaemonWorker<T>, R> func) {
        DaemonWorker<T> worker = this.resolve(task);
        try {
            return func.apply(worker);
        } finally {
            this.destroy(worker);
        }
    }

    default <T extends DaemonTask, R> R withWorker(T task, BiFunction<DaemonWorker<T>, T, R> func) {
        DaemonWorker<T> worker = this.resolve(task);
        try {
            return func.apply(worker, task);
        } finally {
            this.destroy(worker);
        }
    }
}
