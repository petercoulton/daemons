package to.coul.daemons.tasks;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public record IndexAssetTask(@JsonProperty("id") List<UUID> ids) implements DaemonTask {}
