package org.etlforge.application.prefs;

import com.google.common.collect.ImmutableList;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import org.etlforge.application.errorlog.ErrorLog;

public class FixedSetPreference<T> extends Preference<T> {
   private final ImmutableList<T> values;
   private final String idMethod;

   public FixedSetPreference(Class<?> var1, String[] var2, String var3, T[] var4, T var5, String var6) {
      this(var1, var2, var3, Arrays.asList((T[])var4), (T)var5, var6);
   }

   public FixedSetPreference(Class<?> var1, String[] var2, String var3, Iterable<T> var4, T var5, String var6) {
      super(var1, var2, var3, (T)var5);
      this.values = ImmutableList.copyOf(var4);
      this.idMethod = var6;
   }

   public final ImmutableList<T> getValues() {
      return this.values;
   }

   public final int getNumberOfValues() {
      return this.values.size();
   }

   public void set(T var1) {
      Object var2 = this.get();
      if (var1 != null) {
         try {
            String var3 = this.getValueId((T)var1);
            this.getNode().put(this.getKey(), var3);
         } catch (Exception var4) {
            ErrorLog.log(this.getClass(), "Failed to apply the new value of the preference", var4);
         }
      } else {
         this.clear();
      }

      this.fireValueChanged((T)var2, (T)var1);
   }

   public T get() {
      String var1 = this.getNode().get(this.getKey(), null);
      Object var2 = null;
      if (var1 != null) {
         var2 = this.convertIdToValue(var1);
      }

      return (T)(var2 != null ? var2 : this.getDefaultValue());
   }

   protected T convertIdToValue(String var1) {
      try {
         for (Object var3 : this.values) {
            if (var1.equals(this.getValueId((T)var3))) {
               return (T)var3;
            }
         }
      } catch (Exception var4) {
      }

      return null;
   }

   private String getValueId(T var1) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      Object var2 = var1.getClass().getMethod(this.idMethod).invoke(var1);
      return var2.toString();
   }
}
