package org.etlforge.util.persist;

public interface PersistableFactory<T extends Persistable> {
   T restore(Persistor var1);
}
