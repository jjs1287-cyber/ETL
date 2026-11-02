package org.etlforge.util.event;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class AbstractListenerManager<T> implements ListenerManager<T> {
   private final List<T> listeners = new CopyOnWriteArrayList<>();
   private final T notifier = this.createNotifier();

   @Override
   public final void add(T var1) {
      this.listeners.add((T)var1);
   }

   @Override
   public final T call() {
      return this.notifier;
   }

   @Override
   public final void removeAll() {
      this.listeners.clear();
   }

   @Override
   public final void remove(T var1) {
      this.listeners.remove(var1);
   }

   @Override
   public final boolean isEmpty() {
      return this.listeners.isEmpty();
   }

   public final Iterable<T> getListeners() {
      return this.listeners;
   }

   protected abstract T createNotifier();
}
