package to.coul.daemons;

import to.coul.daemons.annotations.ForTaskLiteral;
import to.coul.daemons.workers.DaemonTask;
import to.coul.daemons.workers.DaemonWorker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.logging.Log;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.smallrye.reactive.messaging.annotations.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class DaemonService {
    @Inject @Any
    Instance<DaemonWorker<?>> workers;

    @Inject
    ObjectMapper mapper;

    @Incoming("jobs")
    @Blocking
    public CompletionStage<Void> onMessage(Message<String> message) {
        final String json = message.getPayload();

        final DaemonTask task;
        try {
            task = mapper.readValue(json, DaemonTask.class);
        } catch (JsonProcessingException e) {
            return message.nack(new IllegalArgumentException("Failed to deserialize task", e));
        }

        final var handle =
            this.workers.select(new ForTaskLiteral(task.getClass()));
        if (handle.isUnsatisfied()) {
            return message.nack(new IllegalStateException("No worker for task type " + task.getClass().getName()));
        }

        DaemonWorker<DaemonTask> worker = null;
        try {
            //noinspection unchecked
            worker = (DaemonWorker<DaemonTask>) handle.get();

            worker.process(task);

            return message.ack();
        } catch (Exception e) {
            return message.nack(e);
        } finally {
            if (worker != null) {
                try {
                    handle.destroy(worker);
                } catch (Exception ignore) {
                    /* noop */
                }
            }
        }
    }


    void onStart(@Observes StartupEvent ev) {
        Log.info("DaemonService started");
    }

    void onStop(@Observes ShutdownEvent ev) {
        Log.info("DaemonService stopped!");
    }
}
