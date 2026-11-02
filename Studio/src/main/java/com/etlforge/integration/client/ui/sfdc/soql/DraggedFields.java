package com.etlforge.integration.client.ui.sfdc.soql;

import java.util.Collections;
import java.util.List;
import org.etlforge.collections.Collections2;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.util.name.Nameables;

public final class DraggedFields {
   public final SObjectSchema sobject;
   public final List<SObjectSchema.Field> fields;

   public DraggedFields(SObjectSchema var1, SObjectSchema.Field var2) {
      this(var1, Collections.singletonList(var2));
   }

   public DraggedFields(SObjectSchema var1, List<SObjectSchema.Field> var2) {
      this.sobject = var1;
      this.fields = var2;
   }

   public String getListOfFieldNames() {
      return Collections2.join(this.fields, Nameables.toNameFunction(), ", ");
   }
}
