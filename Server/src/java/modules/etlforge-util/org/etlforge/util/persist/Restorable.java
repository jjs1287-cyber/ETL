package org.etlforge.util.persist;

public interface Restorable {
   String getId();

   void restore(Persistor var1);
}
