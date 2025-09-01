package to.coul.daemons;

import to.coul.daemons.tasks.TaskInvoker;
import to.coul.daemons.tasks.TaskParser;

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
    @Blocking(value="daemon-pool", ordered = false)
    public CompletionStage<Void> onMessage(Message<String> message) {
        return parser.parse(message.getPayload())
                     .map(task -> this.invoker.invoke(task)
                                              .thenCompose(v -> message.ack())
                                              .exceptionally(ex -> {
                                                  message.nack(ex);
                                                  return null;
                                              }))
                     .getOrElseGet(ex -> message.nack(new IllegalArgumentException(ex)));
    }
}
