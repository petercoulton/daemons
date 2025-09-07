package to.coul.daemons.workers;

import to.coul.daemons.domain.task.FTPImportTask;
import to.coul.daemons.domain.worker.DaemonWorker;
import to.coul.daemons.domain.task.annotations.ForTask;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.Dependent;

@Dependent
@ForTask(FTPImportTask.class)
public class FTPImportWorker extends DaemonWorker<FTPImportTask> {

    @Override
    public void process(final FTPImportTask task) {
        Log.info("Processing ftp import task");
    }
}
