package io.datafx.crud;

import io.datafx.core.concurrent.ConcurrentUtils;
import io.datafx.core.concurrent.ObservableExecutor;
import io.datafx.crud.util.EntityWithId;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Worker;

import java.util.concurrent.Executor;

public class CrudObjectProperty<S extends EntityWithId<T>, T> extends SimpleObjectProperty<S> {

    private CrudService<S, T> crudService;
    private Executor executor;
    private CrudListProperty<S, T> listProperty;

    public CrudObjectProperty(S entity, CrudListProperty<S, T> listProperty, CrudService<S, T> crudService) {
        this(entity, crudService, ObservableExecutor.getDefaultInstance());
        this.listProperty = listProperty;
    }

    public CrudObjectProperty(S entity, CrudService<S, T> crudService) {
        this(crudService);
        set(entity);
    }

    public CrudObjectProperty(CrudService<S, T> crudService) {
        this(crudService, ObservableExecutor.getDefaultInstance());
    }

    public CrudObjectProperty(S entity, CrudListProperty<S, T> listProperty, CrudService<S, T> crudService, Executor executor) {
        this(entity, crudService, executor);
        this.listProperty = listProperty;
    }

    public CrudObjectProperty(S entity, CrudService<S, T> crudService, Executor executor) {
        this(crudService, executor);
        set(entity);
    }

    private CrudObjectProperty(CrudService<S, T> crudService, Executor executor) {
        this.crudService = crudService;
        this.executor = executor;
    }

    public Worker<S> update() {
        return ConcurrentUtils.executeService(executor, ConcurrentUtils.createService(() -> {
            try {
                T id = ConcurrentUtils.runCallableAndWait(() -> get().getId());
                S updatedData = crudService.getById(id);
                ConcurrentUtils.runAndWait(() -> set(updatedData));
                return updatedData;
            } catch (Exception e) {
                throw new RuntimeException("TODO", e);
            }
        }));
    }

    public Worker<S> save() {
        return ConcurrentUtils.executeService(executor, ConcurrentUtils.createService(() -> {
            try {
                S updatedData = crudService.save(get());
                ConcurrentUtils.runAndWait(() -> set(updatedData));
                return updatedData;
            } catch (Exception e) {
                throw new RuntimeException("TODO", e);
            }
        }));
    }

    public Worker<Void> delete() {
        return ConcurrentUtils.executeService(executor, ConcurrentUtils.createService(() -> {
            try {
                crudService.delete(get());
                ConcurrentUtils.runAndWait(() -> {
                    set(null);
                    if (listProperty != null) {
                        listProperty.remove(this);
                    }
                });
            } catch (Exception e) {
                throw new RuntimeException("TODO", e);
            }
        }));
    }

}
