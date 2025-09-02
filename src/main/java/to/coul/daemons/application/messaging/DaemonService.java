package to.coul.daemons.application.messaging;

import to.coul.daemons.domain.task.DaemonTask;
import to.coul.daemons.application.invoker.TaskInvoker;

import io.quarkus.logging.Log;
import io.smallrye.reactive.messaging.annotations.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class DaemonService {

    @Inject
    TaskParser parser;

    @Inject
    TaskInvoker invoker;

    @Incoming("jobs")
    @Blocking(value = "daemon-pool", ordered = false)
    public CompletionStage<Void> onMessage(Message<String> message) {
        return parser.parse(message.getPayload())
                     .map(task -> task.ackImmediately()
                                  ? ackThenInvoke(message, task)
                                  : invokeThenAck(message, task))
                     .getOrElseGet(ex -> message.nack(new IllegalArgumentException(ex)));
    }

    private CompletionStage<Void> ackThenInvoke(final Message<String> message,
                                                final DaemonTask task) {
        return message.ack()
                      .handle((v, ex) -> {
                          if (ex != null) {
                              Log.error("Ack failed", ex);
                              return message.nack(ex);
                          }
                          return this.invoker.invoke(task);
                      })
                      .thenCompose(x -> x);
    }

    private CompletionStage<Void> invokeThenAck(final Message<String> message,
                                                final DaemonTask task) {
        return this.invoker.invoke(task)
                           .exceptionally(ex -> {
                               Log.warnf(ex, "Task %s failed", task);
                               return null;
                           })
                           .thenCompose(v -> message.ack())
                           .handle((v, ex) -> {
                               if (ex != null) {
                                   Log.error("Ack failed", ex);
                                   return message.nack(ex);
                               }
                               return v;
                           })
                           .exceptionally(ex -> {
                               Log.error("Nack failed", ex);
                               return null;
                           })
                           .thenApply(x -> null);
    }
}
