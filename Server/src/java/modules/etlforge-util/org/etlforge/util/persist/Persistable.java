package org.etlforge.util.persist;

public interface Persistable {
   String getId();

   void save(Persistor var1);
}
