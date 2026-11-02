package org.etlforge.integration.salesforce.wsdl;

import com.google.common.base.Function;
import java.util.Arrays;
import java.util.List;
import org.etlforge.collections.Collections2;
import org.etlforge.system.ArgCheck;

public final class FieldEncoder {
   public static Function<SObjectSchema.Field, String> encodeFunction(String var0) {
      ArgCheck.checkNotEmpty(var0);
      return new FieldEncoder.ToString(var0);
   }

   public static Function<String, SObjectSchema.Field> decodeFunction(String var0) {
      ArgCheck.checkNotEmpty(var0);
      return new FieldEncoder.FromString(var0);
   }

   public static String encode(SObjectSchema.Field var0, String var1) {
      List var2 = Arrays.asList(
         var0.getName(),
         var0.getType().name(),
         var0.getSoapType().name(),
         var0.isExternalId() ? "1" : "0",
         var0.isIdLookup() ? "1" : "0",
         var0.isFilterable() ? "1" : "0",
         var0.isCreateable() ? "1" : "0",
         var0.isUpdateable() ? "1" : "0"
      );
      return Collections2.join(var2, var1);
   }

   public static SObjectSchema.Field decode(String var0, String var1) {
      try {
         String[] var2 = var0.split(var1);
         String var3 = var2[0];
         SObjectSchema.Field.Type var4 = SObjectSchema.Field.Type.valueOf(var2[1]);
         SObjectSchema.Field.SoapType var5 = SObjectSchema.Field.SoapType.valueOf(var2[2]);
         boolean var6 = "1".equals(var2[3]);
         boolean var7 = "1".equals(var2[4]);
         boolean var8 = var2.length > 5 && "1".equals(var2[5]);
         boolean var9;
         boolean var10;
         if (var2.length < 7) {
            var9 = !"Id".equals(var3);
            var10 = var9;
         } else {
            var9 = "1".equals(var2[6]);
            var10 = "1".equals(var2[7]);
         }

         DefaultSObjectField var11 = new DefaultSObjectField(var3, var4, var5, var6, var7);
         var11.setCreateable(var9);
         var11.setUpdateable(var10);
         var11.setFilterable(var8);
         return var11;
      } catch (Exception var12) {
         throw new RuntimeException("Invalid SObject field data: " + var0, var12);
      }
   }

   private FieldEncoder() {
   }

   private static final class FromString implements Function<String, SObjectSchema.Field> {
      private final String separator;

      public FromString(String var1) {
         this.separator = var1;
      }

      public SObjectSchema.Field apply(String var1) {
         return FieldEncoder.decode(var1, this.separator);
      }

      @Override
      public String toString() {
         return "Decoder(String -> Field)";
      }
   }

   private static final class ToString implements Function<SObjectSchema.Field, String> {
      private final String separator;

      public ToString(String var1) {
         this.separator = var1;
      }

      public String apply(SObjectSchema.Field var1) {
         return FieldEncoder.encode(var1, this.separator);
      }

      @Override
      public String toString() {
         return "Encoder(Field -> String)";
      }
   }
}
