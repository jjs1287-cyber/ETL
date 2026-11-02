package com.etlforge.integration.client.ui.sfdc.page.soql;

import com.etlforge.integration.client.ui.sfdc.soql.AbstractQueryArea;
import com.etlforge.integration.client.ui.sfdc.soql.QueryAreaTransferHandler;
import com.etlforge.integration.client.ui.sfdc.soql.SoqlInputInserter;
import java.util.List;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.ui.widget.text.KongaTextArea;

final class SoqlInputArea extends AbstractQueryArea {
   private SoqlInputInserter inserter;

   @Override
   protected void customize(KongaTextArea var1) {
      this.inserter = new SoqlInputInserter(var1, "...");
      this.installDropSupport(var1);
   }

   private void installDropSupport(KongaTextArea var1) {
      QueryAreaTransferHandler var2 = new QueryAreaTransferHandler(this.inserter, var1.getTransferHandler());
      var1.setTransferHandler(var2);
   }

   public void insertSObject(SObjectSchema var1) {
      this.inserter.insertSObject(var1);
   }

   public void insertObjectName(String var1) {
      this.inserter.insertObjectName(var1);
   }

   public void insertFields(SObjectSchema var1, List<SObjectSchema.Field> var2) {
      this.inserter.insertFields(var1, var2);
   }

   public void insertFieldNames(String var1, List<String> var2) {
      this.inserter.insertFieldNames(var1, var2);
   }
}
