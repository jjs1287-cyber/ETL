package com.etlforge.integration.client.ui.sfdc.soql;

import java.util.List;
import javax.swing.text.JTextComponent;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.util.name.Nameables;

public final class SoqlInputInserter extends AbstractSoqlInputInserter {
   public SoqlInputInserter(JTextComponent var1, String var2) {
      super(var1, var2);
   }

   public void insertSObject(SObjectSchema var1) {
      this.insertObjectName(var1.getName());
   }

   public void insertFields(SObjectSchema var1, List<SObjectSchema.Field> var2) {
      this.insertFieldNames(var1.getName(), Nameables.getNames(var2));
   }
}
