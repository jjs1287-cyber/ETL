package com.etlforge.integration.client.ui.sfdc.ucu.page;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import org.etlforge.integration.data.entity.sfdc.DateConversionMapping;
import org.etlforge.integration.salesforce.DateConversionParams;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.integration.structure.mapping.Mapping;
import org.etlforge.integration.structure.mapping.MappingFactory;
import org.etlforge.integration.structure.mapping.NodePath;

final class DateMapping {
   private final NodePath targetPath;
   private final String sourceDe;
   private final boolean editedByUser;
   private final Optional<DateConversionParams> newParams;
   private final SObjectSchema.Field.SoapType soapType;

   public static DateMapping forDate(Mapping var0, Optional<DateConversionParams> var1, Optional<DateConversionParams> var2) {
      return new DateMapping(var0, var1, var2, SObjectSchema.Field.SoapType.DATE);
   }

   public static DateMapping forDateTime(Mapping var0, Optional<DateConversionParams> var1, Optional<DateConversionParams> var2) {
      return new DateMapping(var0, var1, var2, SObjectSchema.Field.SoapType.DATETIME);
   }

   private DateMapping(Mapping var1, Optional<DateConversionParams> var2, Optional<DateConversionParams> var3, SObjectSchema.Field.SoapType var4) {
      this.targetPath = var1.getTargetPath();
      this.newParams = Preconditions.checkNotNull(var3);
      Preconditions.checkArgument(
         var4 == SObjectSchema.Field.SoapType.DATE || var4 == SObjectSchema.Field.SoapType.DATETIME,
         "soapType must be one of DATE or DATETIME (was " + var4 + ")"
      );
      this.soapType = var4;
      DateConversionParser var5 = new DateConversionParser(var4, var1.getExpression(), Preconditions.checkNotNull(var2));
      this.sourceDe = var5.getSourceDataElement();
      this.editedByUser = var5.isUserEdited();
   }

   public boolean isUserEdited() {
      return this.editedByUser;
   }

   public Mapping generateNewMapping() {
      String var1 = this.createMappingExpression();
      return var1 == null ? null : MappingFactory.newMapping(this.targetPath, var1);
   }

   private String createMappingExpression() {
      if (this.sourceDe == null) {
         return null;
      } else {
         return this.soapType == SObjectSchema.Field.SoapType.DATE
            ? DateConversionMapping.forDate(this.sourceDe, this.newParams)
            : DateConversionMapping.forDateTime(this.sourceDe, this.newParams);
      }
   }

   public String getTargetFieldName() {
      return this.targetPath.getLeafName();
   }
}
