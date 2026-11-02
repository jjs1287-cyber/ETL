package org.etlforge.integration.salesforce.wsdl.partner.wsc;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

final class ListSplitter {
   public static <T> List<List<T>> splitCollection(Collection<T> var0, int var1) {
      if (var0 instanceof List) {
         return split((List<T>)var0, var1);
      }

      ArrayList var2 = Lists.newArrayList(var0);
      return split(var2, var1);
   }

   public static <T> List<List<T>> split(List<T> var0, int var1) {
      ArrayList var2 = Lists.newArrayList();
      if (var0.size() <= var1) {
         var2.add(var0);
      } else {
         int var3 = 0;

         while (var3 < var0.size()) {
            int var4 = var3;
            var3 += var1;
            int var5 = Math.min(var0.size(), var3);
            List var6 = var0.subList(var4, var5);
            var2.add(var6);
         }
      }

      return var2;
   }
}
