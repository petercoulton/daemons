package to.coul.daemons.application.messaging;

import to.coul.daemons.domain.task.DaemonTask;
import to.coul.daemons.util.Either;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class JacksonTaskParser implements TaskParser {

    @Inject
    ObjectMapper mapper;

    @Override
    public Either<ParsingException, DaemonTask> parse(final String json) {
        try {
            return Either.right(mapper.readValue(json, DaemonTask.class));
        } catch (JsonProcessingException e) {
            return Either.left(new ParsingException("Failed to parse task JSON", e));
        }
    }
}
