package org.etlforge.integration.dataelement;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.Map;

public enum DataElementType {
   UNDEFINED("undefined"),
   BOOLEAN("bool"),
   INTEGER("int"),
   LONG("long"),
   FLOAT("float"),
   DOUBLE("double"),
   STRING("string"),
   DATE("date"),
   DATETIME("datetime"),
   TIME("time"),
   TIMESPAN("timespan");

   private static final Map<String, DataElementType> STRING_REP_MAP = Maps.newHashMap();
   private final String stringRep;

   DataElementType(String var3) {
      this.stringRep = var3;
   }

   public static DataElementType fromString(String var0) {
      DataElementType var1 = STRING_REP_MAP.get(var0);
      Preconditions.checkArgument(var1 != null, "Invalid type name: " + var0);
      return var1;
   }

   @Override
   public String toString() {
      return this.stringRep;
   }

   static {
      for (DataElementType var3 : values()) {
         STRING_REP_MAP.put(var3.stringRep, var3);
      }
   }
}
