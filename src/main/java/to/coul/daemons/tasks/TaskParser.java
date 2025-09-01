package to.coul.daemons.tasks;

import to.coul.daemons.util.Either;

public interface TaskParser {
    Either<ParsingException, DaemonTask> parse(String json);
}
