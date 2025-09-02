package to.coul.daemons.domain.task;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public record IndexAssetTask(@JsonProperty("id") List<UUID> ids) implements DaemonTask {}
