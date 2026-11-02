package com.etlforge.integration.client.ui.sfdc.wizard.soql.relational;

import com.etlforge.integration.salesforce.ui.NameDisplayMode;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;

public final class ChildRelationship extends FieldOwner {
   public ChildRelationship(String var1, RootObject var2, SObjectSchema var3) {
      super(var1, var2.getPath(), var3);
   }

   @Override
   public String getDisplayName(NameDisplayMode var1) {
      return this.getName();
   }
}
