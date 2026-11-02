package org.etlforge.integration.structure.crom;

import java.util.HashMap;
import java.util.Map;

final class ValueTypeMap {
   private static Map<String, Integer> types = new HashMap<>();

   public static int get(String var0) {
      Integer var1 = types.get(var0);
      if (var1 != null) {
         return var1;
      } else {
         return var0.startsWith("Auto") ? 0 : 5;
      }
   }

   private ValueTypeMap() {
   }

   static {
      types.put("t_unknown", -1);
      types.put("count", 0);
      types.put("dec", 0);
      types.put("i", 0);
      types.put("int", 0);
      types.put("integer", 0);
      types.put("int2", 0);
      types.put("int4", 0);
      types.put("identity", 0);
      types.put("max", 0);
      types.put("min", 0);
      types.put("short", 0);
      types.put("srt", 0);
      types.put("smallint", 0);
      types.put("tinyint", 0);
      types.put("unsignedint", 0);
      types.put("bigint", 1);
      types.put("int8", 1);
      types.put("lng", 1);
      types.put("long", 1);
      types.put("b", 6);
      types.put("bit", 6);
      types.put("bol", 6);
      types.put("bool", 6);
      types.put("boolean", 6);
      types.put("d", 4);
      types.put("date", 4);
      types.put("datetime", 4);
      types.put("dte", 4);
      types.put("smalldatetime", 4);
      types.put("t", 4);
      types.put("time", 4);
      types.put("timestamp", 4);
      types.put("string", 5);
      types.put("c", 5);
      types.put("char", 5);
      types.put("character", 5);
      types.put("character varying", 5);
      types.put("m", 5);
      types.put("nvarchar2", 5);
      types.put("str", 5);
      types.put("text", 5);
      types.put("varchar", 5);
      types.put("varchar2", 5);
      types.put("wchar", 5);
      types.put("binary_double", 3);
      types.put("currency", 3);
      types.put("dbl", 3);
      types.put("decimal", 3);
      types.put("double", 3);
      types.put("double precision", 3);
      types.put("float8", 2);
      types.put("money", 3);
      types.put("number", 3);
      types.put("numeric", 3);
      types.put("binary_float", 2);
      types.put("flt", 2);
      types.put("float", 2);
      types.put("real", 2);
      types.put("single", 2);
      types.put("smallmoney", 2);
      types.put("binary", 9);
      types.put("blob sub_type 1", 9);
      types.put("bytea", 9);
      types.put("image", 9);
      types.put("varbinary", 9);
      types.put("collection", 10);
   }
}
