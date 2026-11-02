package org.etlforge.integration.structure.mapping;

import com.google.common.base.Function;
import org.etlforge.lang.KongaStringUtils;

public final class MapStringFormat {
   private static final String DELIMITER = "\t";
   public static final Function<String, Mapping> STRING_TO_MAPPING = new Function<String, Mapping>() {
      public Mapping apply(String var1) {
         return MapStringFormat.parse(var1);
      }
   };
   public static final Function<Mapping, String> MAPPING_TO_STRING = new Function<Mapping, String>() {
      public String apply(Mapping var1) {
         return MapStringFormat.format(var1);
      }
   };

   public static String format(String var0, String var1) {
      return var0 + "\t" + var1;
   }

   public static String format(Mapping var0) {
      String var1 = var0.getExpression();
      String var2 = var0.getTargetPath().toString();
      return format(var2, var1);
   }

   public static Mapping parse(String var0) {
      try {
         String[] var1 = KongaStringUtils.splitWithFirstTab(var0);
         return MappingFactory.newMapping(var1[0], var1[1]);
      } catch (RuntimeException var2) {
         throw new IllegalArgumentException("Invalid map string: " + var0);
      }
   }

   private MapStringFormat() {
   }
}
