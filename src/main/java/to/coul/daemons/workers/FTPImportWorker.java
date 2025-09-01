package to.coul.daemons.workers;

import to.coul.daemons.annotations.ForTask;
import to.coul.daemons.tasks.FTPImportTask;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.Dependent;

@Dependent
@ForTask(FTPImportTask.class)
public class FTPImportWorker extends DaemonWorker<FTPImportTask> {

    @Override
    public void process(final FTPImportTask task) {
        Log.info("Processing ftp import task");
    }

    @Override
    public Class<FTPImportTask> getTaskType() {
        return FTPImportTask.class;
    }
}
