package com.etlforge.integration.client.ui.sfdc;

import java.util.Comparator;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;

public final class ObjectPresentationOrder implements Comparator<SObjectSchema> {
   private final CommonlyUsedObjects commonlyUsed;
   private final Comparator<String> tieBreaker = String.CASE_INSENSITIVE_ORDER;

   public ObjectPresentationOrder(CommonlyUsedObjects var1) {
      this.commonlyUsed = var1;
   }

   public int compare(SObjectSchema var1, SObjectSchema var2) {
      int var3 = this.checkCommonlyUsedObjects(var1, var2);
      if (var3 == 0) {
         var3 = this.tieBreaker.compare(var1.getName(), var2.getName());
      }

      return var3;
   }

   private int checkCommonlyUsedObjects(SObjectSchema var1, SObjectSchema var2) {
      for (String var4 : this.commonlyUsed.preferredOrder()) {
         if (is(var1, var4)) {
            return -1;
         }

         if (is(var2, var4)) {
            return 1;
         }
      }

      return 0;
   }

   private static boolean is(SObjectSchema var0, String var1) {
      return var0.getName().equals(var1);
   }
}
