package org.etlforge.util.event;

public interface ListenerManager<T> {
   void add(T var1);

   void remove(T var1);

   void removeAll();

   boolean isEmpty();

   T call();
}
