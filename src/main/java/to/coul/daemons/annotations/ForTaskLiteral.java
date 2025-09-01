package to.coul.daemons.annotations;

import to.coul.daemons.tasks.DaemonTask;

import jakarta.enterprise.util.AnnotationLiteral;

@SuppressWarnings("ClassExplicitlyAnnotation")
public class ForTaskLiteral extends AnnotationLiteral<ForTask> implements ForTask {

    private final Class<? extends DaemonTask> value;

    public ForTaskLiteral(Class<? extends DaemonTask> value) {
        this.value = value;
    }

    @Override
    public Class<? extends DaemonTask> value() {
        return this.value;
    }
}
