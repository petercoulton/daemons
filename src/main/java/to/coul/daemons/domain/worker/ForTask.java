package to.coul.daemons.domain.worker;

import to.coul.daemons.domain.task.DaemonTask;

import jakarta.inject.Qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface ForTask {
    Class<? extends DaemonTask> value();
}
