package to.coul.daemons.tasks;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = IndexAssetTask.class, name = "indexAsset"),
    @JsonSubTypes.Type(value = FTPImportTask.class, name = "importFTP")
})
public sealed interface DaemonTask permits IndexAssetTask, FTPImportTask {
    @JsonIgnore
    default boolean ackImmediately() {
        return false;
    }
}
