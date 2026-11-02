package org.etlforge.integration.salesforce.wsdl.partner.wsc;

import com.google.common.collect.Lists;
import com.sforce.soap.partner.Field;
import com.sforce.soap.partner.PicklistEntry;
import java.util.ArrayList;
import org.etlforge.integration.salesforce.wsdl.Picklist;

final class PicklistBuilder {
   public static Picklist fromField(Field var0) {
      PicklistEntry[] var1 = var0.getPicklistValues();
      if (var1.length == 0) {
         return Picklist.EMPTY;
      }

      ArrayList var2 = Lists.newArrayList();

      for (PicklistEntry var6 : var1) {
         var2.add(new org.etlforge.integration.salesforce.wsdl.PicklistEntry(var6.getLabel(), var6.getValue()));
      }

      return new Picklist(var2);
   }
}
