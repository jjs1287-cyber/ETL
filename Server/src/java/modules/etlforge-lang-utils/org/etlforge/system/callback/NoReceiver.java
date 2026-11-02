package org.etlforge.system.callback;

import java.io.Serializable;

public final class NoReceiver<T> implements Receiver<T>, Serializable {
   public static <E> NoReceiver<E> create() {
      return new NoReceiver();
   }

   @Override
   public void handle(T var1) {
   }
}
