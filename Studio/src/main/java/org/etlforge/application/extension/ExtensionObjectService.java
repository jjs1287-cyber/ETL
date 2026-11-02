package org.etlforge.application.extension;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import org.etlforge.application.errorlog.ErrorLog;

public final class ExtensionObjectService {
   private static final ExtensionObjectService instance = new ExtensionObjectService();
   private final Map<ExtensionObjectService.InstanceKey, Object> instanceMap = Maps.newHashMap();
   private final ListMultimap<Class, ExtensionObjectFactory> factories = ArrayListMultimap.create();
   private ListMultimap<Class, Class> cachedLookupOrder;

   public static ExtensionObjectService getService() {
      return instance;
   }

   public synchronized <T> void registerInstance(Object var1, Class<? super T> var2, T var3) {
      ExtensionObjectService.InstanceKey var4 = new ExtensionObjectService.InstanceKey(var1, var2);
      if (var3 != null) {
         this.instanceMap.put(var4, var3);
      } else {
         Object var5 = this.instanceMap.remove(var4);
         if (var5 == null) {
            ErrorLog.internal(this.getClass(), null, new Exception("Failed to remove extension object for the key " + var4));
         }
      }
   }

   public synchronized void registerFactory(Class var1, ExtensionObjectFactory var2) {
      this.factories.put(var1, var2);
      this.cachedLookupOrder = null;
   }

   public synchronized void removeFactory(Class var1, ExtensionObjectFactory var2) {
      this.factories.remove(var1, var2);
      this.cachedLookupOrder = null;
   }

   synchronized void removeAll() {
      this.factories.clear();
      this.cachedLookupOrder = null;
   }

   public <T> T getExtensionObject(Object var1, Class<T> var2) {
      Supplier var3;
      synchronized (this) {
         var3 = this.getExtensionObjectSupplier(var1, var2);
      }

      return (T)(var3 == null ? null : var3.get());
   }

   private <T> Supplier<T> getExtensionObjectSupplier(final Object var1, final Class<T> var2) {
      Object var3 = this.searchForDirectInstance(var1, var2);
      if (var3 != null) {
         return Suppliers.ofInstance((T)var3);
      }

      for (Class var6 : this.getLookupOrder(var1.getClass())) {
         List var7 = this.factories.get(var6);

         for (int var8 = var7.size() - 1; var8 >= 0; var8--) {
            final ExtensionObjectFactory var9 = (ExtensionObjectFactory)var7.get(var8);

            for (Class var13 : var9.getExtensionTypes()) {
               if (var2.isAssignableFrom(var13)) {
                  return new Supplier<T>() {
                     @Override
                     public T get() {
                        Object var1x = var9.getExtensionObject(var1, var2);
                        return (T)var2.cast(var1x);
                     }
                  };
               }
            }
         }
      }

      return null;
   }

   private <T> T searchForDirectInstance(Object var1, Class<T> var2) {
      ExtensionObjectService.InstanceKey var3 = new ExtensionObjectService.InstanceKey(var1, var2);
      Object var4 = this.instanceMap.get(var3);
      return (T)(var4 != null ? var2.cast(var4) : null);
   }

   private List<Class> getLookupOrder(Class var1) {
      List var2 = null;
      if (this.cachedLookupOrder == null) {
         this.cachedLookupOrder = ArrayListMultimap.create();
      } else {
         var2 = this.cachedLookupOrder.get(var1);
      }

      if (var2 == null || var2.isEmpty()) {
         var2 = this.calculateLookupOrder(var1);
         this.cachedLookupOrder.putAll(var1, var2);
      }

      return var2;
   }

   private List<Class> calculateLookupOrder(Class var1) {
      return new ExtensionObjectLookupOrder().calculateLookupOrder(var1);
   }

   private ExtensionObjectService() {
   }

   private static final class InstanceKey {
      private final Object o;
      private final Class<?> extensionType;

      public InstanceKey(Object var1, Class<?> var2) {
         this.o = var1;
         this.extensionType = var2;
      }

      @Override
      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         }

         if (!(var1 instanceof ExtensionObjectService.InstanceKey)) {
            return false;
         }

         ExtensionObjectService.InstanceKey var2 = (ExtensionObjectService.InstanceKey)var1;
         return this.o == var2.o && this.extensionType.equals(var2.extensionType);
      }

      @Override
      public int hashCode() {
         int var1 = 17;
         var1 = 31 * var1 + System.identityHashCode(this.o);
         return 31 * var1 + this.extensionType.hashCode();
      }

      @Override
      public String toString() {
         return this.o + " [" + this.extensionType.getName() + "]";
      }
   }
}
