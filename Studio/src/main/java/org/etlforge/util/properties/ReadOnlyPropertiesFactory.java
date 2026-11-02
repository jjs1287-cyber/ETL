package org.etlforge.util.properties;

import java.io.File;
import java.io.IOException;

public abstract class ReadOnlyPropertiesFactory {
   public abstract ReadOnlyProperties load(String var1, String... var2) throws IOException;

   public abstract ReadOnlyProperties load(File var1) throws IOException;

   public static ReadOnlyPropertiesFactory newInstance() {
      return new ReadOnlyPropertiesFactory.FactoryImpl();
   }

   private static class FactoryImpl extends ReadOnlyPropertiesFactory {
      private FactoryImpl() {
      }

      @Override
      public ReadOnlyProperties load(File var1) throws IOException {
         return new DefaultReadOnlyProperties(var1);
      }

      @Override
      public ReadOnlyProperties load(String var1, String... var2) throws IOException {
         DefaultReadOnlyProperties var3 = new DefaultReadOnlyProperties(var1);

         for (String var7 : var2) {
            try {
               DefaultReadOnlyProperties var8 = new DefaultReadOnlyProperties(var7);
               var3 = var3.merge(var8);
            } catch (Exception var9) {
               System.err.println("Optional resource " + var7 + " not present on classpath");
            }
         }

         return var3;
      }
   }
}
