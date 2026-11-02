package org.etlforge.application.extension;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

final class ExtensionObjectLookupOrder {
   private List<Class> order;
   private Set<Class> processedInterfaces;

   public List<Class> calculateLookupOrder(Class var1) {
      this.initSearch();
      this.calculateOrder(var1);
      return this.order;
   }

   private void calculateOrder(Class var1) {
      for (Class var2 = var1; var2 != null; var2 = var2.getSuperclass()) {
         this.order.add(var2);
         this.calculateInterfaceOrder(var2.getInterfaces());
      }
   }

   private void initSearch() {
      this.order = Lists.newArrayList();
      this.processedInterfaces = Sets.newHashSet();
   }

   private void calculateInterfaceOrder(Class[] var1) {
      if (var1.length != 0) {
         ArrayList var2 = Lists.newArrayList();

         for (Class var6 : var1) {
            if (this.processedInterfaces.add(var6)) {
               this.order.add(var6);
               var2.add(var6);
            }
         }

         for (Object __etlItem42 : var2) {
            Class var8 = (Class) __etlItem42;
            this.calculateInterfaceOrder(var8.getInterfaces());
         }
      }
   }
}
