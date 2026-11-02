package org.etlforge.util.persist;

import java.util.Collection;
import java.util.Collections;

public class NonFunctioningPersistorStore<T extends Persistable> implements PersistorStore<T> {
   @Override
   public Collection<T> load(PersistableFactory<T> var1) {
      return Collections.emptyList();
   }

   @Override
   public void store(T var1, String var2) {
   }

   @Override
   public void remove(T var1) {
   }
}
