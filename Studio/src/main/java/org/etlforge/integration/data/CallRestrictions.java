package org.etlforge.integration.data;

import org.etlforge.collections.StringKeyValuePairs;
import org.etlforge.integration.data.function.Function;
import org.etlforge.system.annotations.InternalConstructor;
import org.etlforge.util.StorageSizeUnit;
import org.etlforge.util.StringKeyValuePairsPersistor;
import org.etlforge.util.persist.Persistor;

public final class CallRestrictions {
   public static final String FUNCTION_ID = "function_id";
   public static final String FUNCTION_TYPE_ID = "functiontype_id";
   public static final String MIN_CALL_DELAY = "mincalldelay_ms";
   public static final String MAX_SIZE = "maxsize_bytes";
   public static final String MAX_SIZE_UI_UNIT = "maxsize_uiunit";
   public static final String RETRIES = "retries";
   public static final String RETRY_DELAY = "retrydelay_ms";
   private StringKeyValuePairs properties = new StringKeyValuePairs();
   private static final String[] PROPERTIES = new String[]{
      "function_id", "functiontype_id", "mincalldelay_ms", "maxsize_bytes", "maxsize_uiunit", "retries", "retrydelay_ms"
   };

   @InternalConstructor
   public CallRestrictions() {
   }

   public CallRestrictions(Function var1) {
      if (var1 == null) {
         throw new NullPointerException("function cannot be null.");
      }

      this.setProperty("function_id", var1.getFunctionID().toString());
      this.setMaximumSizeUiUnit(StorageSizeUnit.Kilobytes);
   }

   public CallRestrictions(StringKeyValuePairs var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("data cannot be null.");
      }

      this.properties = var1;
      String var2 = this.properties.get("maxsize_uiunit");
      if (var2 == null) {
         this.setMaximumSizeUiUnit(StorageSizeUnit.Kilobytes);
      }
   }

   public long getMinimumCallDelayInMilliSecs() {
      return this.properties.getLong("mincalldelay_ms", 0L);
   }

   public void setMinimumCallDelayInMilliSecs(long var1) {
      this.properties.putLong("mincalldelay_ms", var1);
   }

   public long getMaximumSizeInBytes() {
      return this.properties.getLong("maxsize_bytes", 0L);
   }

   public void setMaximumSizeInBytes(long var1) {
      this.properties.putLong("maxsize_bytes", var1);
   }

   public StorageSizeUnit getMaximumSizeUiUnit() {
      String var1 = this.properties.get("maxsize_uiunit");
      StorageSizeUnit var2 = null;
      if (var1 != null) {
         var2 = StorageSizeUnit.fromAbbreviation(var1);
      }

      if (var2 == null) {
         var2 = StorageSizeUnit.Kilobytes;
      }

      return var2;
   }

   public void setMaximumSizeUiUnit(StorageSizeUnit var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("unit cannot be null.");
      }

      this.properties.putObject("maxsize_uiunit", var1.abbreviation());
   }

   public int getRetries() {
      return this.properties.getInt("retries", 0);
   }

   public void setRetries(int var1) {
      if (var1 < 0) {
         throw new IllegalArgumentException("retries must be >= 0 (value was " + var1 + ").");
      }

      this.properties.putInt("retries", var1);
   }

   public long getRetryDelayInMilliSecs() {
      return this.properties.getLong("retrydelay_ms", 0L);
   }

   public void setRetryDelayInMilliSecs(long var1) {
      this.properties.putLong("retrydelay_ms", var1);
   }

   public CallRestrictions getCopy() {
      return new CallRestrictions(new StringKeyValuePairs(this.properties));
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 instanceof CallRestrictions ? this.properties.equals(((CallRestrictions)var1).properties) : false;
      }
   }

   @Override
   public int hashCode() {
      return this.properties.hashCode();
   }

   public static String[] getPropertyKeys() {
      return (String[])PROPERTIES.clone();
   }

   public String[] getPropertyValues(String var1, int var2) {
      String[] var3 = getPropertyKeys();
      int var4 = var3.length;
      String[] var5 = new String[var4];

      for (int var6 = 0; var6 < var4; var6++) {
         String var7 = this.getProperty(var3[var6]);
         var5[var6] = var7;
      }

      var5[0] = var1;
      var5[1] = Integer.toString(var2);
      return var5;
   }

   public String getProperty(String var1) {
      return this.properties.get(var1);
   }

   public void setProperty(String var1, String var2) {
      this.properties.put(var1, var2);
   }

   public void writeTo(Persistor var1) {
      StringKeyValuePairsPersistor.save(this.properties, var1);
   }

   public void restoreFrom(Persistor var1) {
      this.properties = StringKeyValuePairsPersistor.restoreFrom(var1);
   }

   @Override
   public String toString() {
      return this.properties.toString();
   }
}
