package com.etlforge.integration.data.download.location;

import com.google.common.base.Throwables;
import com.etlforge.integration.data.download.InvalidDownloadedDataException;
import java.lang.reflect.Method;
import org.etlforge.collections.StringKeyValuePairs;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.location.DataLocationType;

public final class DataLocationFactory {
   private final Class<? extends DataLocation> locationClass;

   public static DataLocationFactory getFactory(Source var0) throws InvalidDownloadedDataException {
      int var1 = var0.getIntProperty("source_type_id", -1);
      if (var1 == -1) {
         throw new InvalidDownloadedDataException("Missing value for the source_type_id property");
      } else {
         return createFactory(var1);
      }
   }

   public static DataLocationFactory getFactory(Target var0) throws InvalidDownloadedDataException {
      int var1 = var0.getIntProperty("source_type_id", -1);
      if (var1 == -1) {
         throw new InvalidDownloadedDataException("Missing value for the source_type_id property");
      } else {
         return createFactory(var1);
      }
   }

   private static DataLocationFactory createFactory(int var0) throws InvalidDownloadedDataException {
      try {
         DataLocationType var1 = DataLocationType.fromId(var0);
         return new DataLocationFactory(var1.getLocationClass());
      } catch (Exception var2) {
         throw new InvalidDownloadedDataException("Invalid location type ID: " + var0);
      }
   }

   protected DataLocationFactory(Class<? extends DataLocation> var1) {
      this.locationClass = var1;
   }

   public DataLocation createLocation(StringKeyValuePairs var1) throws InvalidDownloadedDataException {
      DataLocation var2;
      try {
         var2 = this.locationClass.newInstance();
      } catch (Exception var7) {
         throw Throwables.propagate(var7);
      }

      for (String var6 : this.getKeys()) {
         var2.setProperty(var6, var1.get(var6));
      }

      this.customizeLocation(var2);
      return var2;
   }

   protected void customizeLocation(DataLocation var1) {
   }

   private String[] getKeys() {
      try {
         Method var1 = this.locationClass.getMethod("getPropertyKeys");
         return (String[])var1.invoke(null);
      } catch (Exception var2) {
         throw Throwables.propagate(var2);
      }
   }
}
