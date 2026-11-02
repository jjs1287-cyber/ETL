package org.etlforge.util.event;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class ListenerManagerFactory {
   private ListenerManagerFactory() {
   }

   public static <T> ListenerManager<T> create(Class<T> var0) {
      Preconditions.checkNotNull(var0, "listenerClass");
      ClassLoader var1 = Thread.currentThread().getContextClassLoader();
      Class[] var2 = new Class[]{var0, ListenerManager.class};
      ListenerManagerFactory.ManagerImpl var3 = new ListenerManagerFactory.ManagerImpl();
      return (ListenerManager<T>)Proxy.newProxyInstance(var1, var2, var3);
   }

   private static final class ManagerImpl<E> implements ListenerManager<E>, InvocationHandler {
      private final List<E> listeners = new CopyOnWriteArrayList<>();

      public ManagerImpl() {
      }

      @Override
      public Object invoke(Object var1, Method var2, Object[] var3) throws Throwable {
         try {
            if (var2.getDeclaringClass().equals(ListenerManager.class)) {
               return var2.getName().equals("call") ? var1 : var2.invoke(this, var3);
            }

            for (Object var5 : this.listeners) {
               var2.invoke(var5, var3);
            }

            return null;
         } catch (InvocationTargetException var6) {
            throw new RuntimeException(var6.getTargetException());
         } catch (Exception var7) {
            throw Throwables.propagate(var7);
         }
      }

      @Override
      public void add(E var1) {
         this.listeners.add((E)var1);
      }

      @Override
      public E call() {
         throw new UnsupportedOperationException();
      }

      @Override
      public void removeAll() {
         this.listeners.clear();
      }

      @Override
      public void remove(E var1) {
         this.listeners.remove(var1);
      }

      @Override
      public boolean isEmpty() {
         return this.listeners.isEmpty();
      }
   }
}
