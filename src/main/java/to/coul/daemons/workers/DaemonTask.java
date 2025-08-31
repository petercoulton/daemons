package to.coul.daemons.workers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;
import java.util.UUID;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes(
    @JsonSubTypes.Type(value=DaemonTask.IndexAssetTask.class, name="indexAsset")
)
public sealed interface DaemonTask permits DaemonTask.IndexAssetTask {
    record IndexAssetTask(@JsonProperty("id") List<UUID> ids) implements DaemonTask {}
}

