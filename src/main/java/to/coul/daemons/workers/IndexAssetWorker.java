package to.coul.daemons.workers;

import to.coul.daemons.annotations.ForTask;
import to.coul.daemons.workers.DaemonTask.IndexAssetTask;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.Dependent;

@Dependent
@ForTask(IndexAssetTask.class)
public class IndexAssetWorker extends DaemonWorker<IndexAssetTask> {
    @Override
    public void process(final IndexAssetTask task) {
        Log.info("Processing index asset task");
    }
}
