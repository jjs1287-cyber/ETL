package org.etlforge.util.persist;

public abstract class AbstractPersistor implements Persistor {
   @Override
   public void putText(String var1, String var2) {
      this.putString(var1, var2);
   }

   @Override
   public String getText(String var1) {
      return this.getString(var1);
   }

   @Override
   public final void putInteger(String var1, int var2) {
      this.putString(var1, String.valueOf(var2));
   }

   @Override
   public final Integer getInteger(String var1) {
      String var2 = this.getString(var1);
      return var2 != null ? Integer.valueOf(var2) : null;
   }

   @Override
   public final void putLong(String var1, long var2) {
      this.putString(var1, String.valueOf(var2));
   }

   @Override
   public final Long getLong(String var1) {
      String var2 = this.getString(var1);
      return var2 != null ? Long.valueOf(var2) : null;
   }

   @Override
   public final void putDouble(String var1, double var2) {
      this.putString(var1, String.valueOf(var2));
   }

   @Override
   public final Double getDouble(String var1) {
      String var2 = this.getString(var1);
      return var2 != null ? Double.valueOf(var2) : null;
   }

   @Override
   public final void putBoolean(String var1, boolean var2) {
      this.putString(var1, String.valueOf(var2));
   }

   @Override
   public final Boolean getBoolean(String var1) {
      String var2 = this.getString(var1);
      return var2 != null ? Boolean.valueOf(var2) : null;
   }
}
