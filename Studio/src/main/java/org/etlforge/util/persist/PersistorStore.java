package org.etlforge.util.persist;

import java.util.Collection;

public interface PersistorStore<T extends Persistable> {
   void store(T var1, String var2);

   void remove(T var1);

   Collection<T> load(PersistableFactory<T> var1);
}
