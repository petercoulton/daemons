package to.coul.daemons.annotations;

import to.coul.daemons.workers.DaemonTask;

import jakarta.inject.Qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface ForTask {
    Class<? extends DaemonTask> value();
}
