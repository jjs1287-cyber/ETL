package com.etlforge.integration.client.ui.sfdc.query.page;

import org.etlforge.integration.data.entity.sfquery.FlatQueryMappingFactory;
import org.etlforge.integration.salesforce.parser.ParsedQuery;
import org.etlforge.integration.structure.mapping.Mapping;

final class FieldWrapper {
   private final String objectName;
   private final String originalSfdcFieldName;
   private final String simpleSfdcFieldName;

   public FieldWrapper(ParsedQuery var1, String var2) {
      this(var1.getObjectName(), var2);
   }

   public FieldWrapper(String var1, String var2) {
      this.objectName = var1;
      this.originalSfdcFieldName = var2;
      this.simpleSfdcFieldName = ParsedQuery.getFieldNameWithoutObjectQualifier(var1, var2);
   }

   @Override
   public int hashCode() {
      return this.simpleSfdcFieldName.toLowerCase().hashCode();
   }

   @Override
   public boolean equals(Object var1) {
      return var1 == this || this.simpleSfdcFieldName.equalsIgnoreCase(((FieldWrapper)var1).simpleSfdcFieldName);
   }

   public String getName() {
      return this.originalSfdcFieldName;
   }

   String getSimpleSfdcFieldName() {
      return this.simpleSfdcFieldName;
   }

   public Mapping getMappingToFlatTarget() {
      FlatQueryMappingFactory var1 = new FlatQueryMappingFactory(this.objectName);
      return var1.getMapping(this.originalSfdcFieldName);
   }

   @Override
   public String toString() {
      return this.originalSfdcFieldName;
   }
}
