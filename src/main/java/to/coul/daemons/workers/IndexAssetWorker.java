package to.coul.daemons.workers;

import to.coul.daemons.domain.task.IndexAssetTask;
import to.coul.daemons.domain.worker.DaemonWorker;
import to.coul.daemons.domain.worker.ForTask;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.Dependent;

@Dependent
@ForTask(IndexAssetTask.class)
public class IndexAssetWorker extends DaemonWorker<IndexAssetTask> {

    @Override
    public void process(final IndexAssetTask task) {
        Log.info("Processing index asset task");
    }

    @Override
    public Class<IndexAssetTask> getTaskType() {
        return IndexAssetTask.class;
    }
}
