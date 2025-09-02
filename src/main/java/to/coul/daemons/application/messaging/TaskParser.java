package to.coul.daemons.application.messaging;

import to.coul.daemons.domain.task.DaemonTask;
import to.coul.daemons.util.Either;

public interface TaskParser {
    Either<ParsingException, DaemonTask> parse(String json);
}
