package to.coul.daemons.workers;

import static to.coul.daemons.workers.IndexAssetWorker.*;

import to.coul.daemons.annotations.ForTask;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.Dependent;

import java.util.List;
import java.util.UUID;

@Dependent
@ForTask(IndexAssetTask.class)
public class IndexAssetWorker extends DaemonWorker<IndexAssetTask> {

    @Override
    public void process(final IndexAssetTask task) {
        Log.info("Processing index asset task");
    }

    public record IndexAssetTask(@JsonProperty("id") List<UUID> ids) implements DaemonTask {}
}
