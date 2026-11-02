package org.etlforge.integration.salesforce.wsdl;

import java.io.Serializable;
import java.util.Comparator;

public final class DefaultFieldOrder implements Comparator<SObjectSchema.Field>, Serializable {
   public int compare(SObjectSchema.Field var1, SObjectSchema.Field var2) {
      String var3 = var1.getName();
      String var4 = var2.getName();
      if (var3.equals("Id")) {
         return var4.equals("Id") ? 0 : -1;
      } else {
         return var4.equals("Id") ? 1 : var3.compareTo(var4);
      }
   }
}
