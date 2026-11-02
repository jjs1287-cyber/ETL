package org.etlforge.integration.salesforce.wsdl;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;

public enum ObjectCategory implements Predicate<SObjectSchema> {
   STANDARD {
      private final ImmutableSet<String> names = ImmutableSet.of(
         "Account", "Case", "Contact", "Event", "Lead", "Opportunity", "Pricebook2", "Product2", "Task", "User"
      );

      public boolean apply(SObjectSchema var1) {
         return this.names.contains(var1.getName());
      }

      @Override
      public String toString() {
         return "Standard Common Objects";
      }
   },
   CUSTOM {
      public boolean apply(SObjectSchema var1) {
         return var1.isCustom();
      }

      @Override
      public String toString() {
         return "All Custom Objects";
      }
   },
   ALL {
      public boolean apply(SObjectSchema var1) {
         return true;
      }

      @Override
      public String toString() {
         return "All Objects";
      }
   };

   ObjectCategory() {
   }
}
