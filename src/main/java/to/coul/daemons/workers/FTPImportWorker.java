package to.coul.daemons.workers;

import static to.coul.daemons.workers.FTPImportWorker.*;

import to.coul.daemons.annotations.ForTask;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.Dependent;

@Dependent
@ForTask(FTPImportTask.class)
public class FTPImportWorker extends DaemonWorker<FTPImportTask> {

    @Override
    public void process(final FTPImportTask task) {
        Log.info("Processing ftp import task");
    }

    public record FTPImportTask() implements DaemonTask {}

}
