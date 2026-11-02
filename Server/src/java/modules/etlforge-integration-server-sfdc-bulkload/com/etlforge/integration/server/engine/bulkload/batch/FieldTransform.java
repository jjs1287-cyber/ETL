package com.etlforge.integration.server.engine.bulkload.batch;

import com.google.common.collect.ImmutableSet;
import com.etlforge.integration.server.engine.bulkload.BulkLoadParams;
import com.etlforge.integration.server.sfdc.dataloader.DateTransform;
import java.util.List;
import org.etlforge.integration.salesforce.DateConversionParams;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;

class FieldTransform {
   private final int numberOfFields;
   private final FieldTransform.FieldType[] fieldTypes;
   private final DateTransform dateTransform;
   private static final ImmutableSet<String> BOOLEAN_TRUE = ImmutableSet.of("true", "1", "yes", "y", "on");
   private static final ImmutableSet<String> BOOLEAN_FALSE = ImmutableSet.of("false", "0", "no", "n", "off");

   public FieldTransform(BulkLoadParams var1) {
      List var2 = var1.getTargetFields();
      this.numberOfFields = var2.size();
      DateConversionParams var3 = var1.getDateConversionParams();
      if (var3 == null) {
         this.fieldTypes = null;
         this.dateTransform = null;
      } else {
         this.fieldTypes = new FieldTransform.FieldType[this.numberOfFields];

         for (int var4 = 0; var4 < this.numberOfFields; var4++) {
            SObjectSchema.Field var5 = (SObjectSchema.Field)var2.get(var4);
            this.fieldTypes[var4] = FieldTransform.FieldType.ofField(var5);
         }

         this.dateTransform = new DateTransform(var3);
         this.dateTransform.setReturnInputValueIfInvalid(true);
      }
   }

   public void setStrict(boolean var1) {
      if (this.dateTransform != null) {
         this.dateTransform.setReturnInputValueIfInvalid(!var1);
      }
   }

   public String transform(int var1, String var2) throws Exception {
      boolean var3 = "#N/A".equals(var2);
      if (var3) {
         return var2;
      } else {
         return this.isConversionNeeded(var1) ? this.convertValue(var1, var2) : var2;
      }
   }

   private boolean isConversionNeeded(int var1) {
      return this.fieldTypes != null && var1 < this.numberOfFields;
   }

   private String convertValue(int var1, String var2) throws Exception {
      FieldTransform.FieldType var3 = this.fieldTypes[var1];
      switch (var3) {
         case DATE:
            return this.dateTransform.convertDate(var2);
         case DATE_TIME:
            return this.dateTransform.convertDateTime(var2);
         case BOOLEAN:
            return convertBoolean(var2);
         case OTHER:
         default:
            return var2;
      }
   }

   private static String convertBoolean(String var0) {
      String var1 = var0.toLowerCase();
      if (BOOLEAN_TRUE.contains(var1)) {
         return "1";
      } else {
         return BOOLEAN_FALSE.contains(var1) ? "0" : var0;
      }
   }

   private enum FieldType {
      DATE,
      DATE_TIME,
      BOOLEAN,
      OTHER;

      public static FieldTransform.FieldType ofField(SObjectSchema.Field var0) {
         switch (var0.getSoapType()) {
            case DATE:
               return DATE;
            case DATETIME:
               return DATE_TIME;
            case BOOLEAN:
               return BOOLEAN;
            default:
               return OTHER;
         }
      }
   }
}
