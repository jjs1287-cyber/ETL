package com.etlforge.integration.client.ui.sfdc.query.v4;

import com.google.common.collect.Lists;
import com.etlforge.integration.client.ui.sfdc.soql.AbstractSoqlInputInserter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.text.JTextComponent;
import org.etlforge.integration.salesforce.SObject;

public final class QueryAreaInserter extends AbstractSoqlInputInserter {
   public QueryAreaInserter(JTextComponent var1, String var2) {
      super(var1, var2);
   }

   public void insertSObject(SObject var1) {
      this.insertObjectName(var1.getLocalName());
   }

   public void insertFields(SObject var1, List<SObject.Field> var2) {
      this.insertFieldNames(var1.getLocalName(), getFieldNames(var2));
   }

   private static List<String> getFieldNames(List<SObject.Field> var0) {
      ArrayList var1 = Lists.newArrayList();

      for (SObject.Field var3 : var0) {
         var1.add(var3.getName());
      }

      return var1;
   }
}
