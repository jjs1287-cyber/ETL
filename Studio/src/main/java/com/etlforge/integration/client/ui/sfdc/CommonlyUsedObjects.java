package com.etlforge.integration.client.ui.sfdc;

import com.google.common.collect.ImmutableSet;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;

public final class CommonlyUsedObjects {
   public static final CommonlyUsedObjects NONE = new CommonlyUsedObjects();
   public static final CommonlyUsedObjects DEFAULT = new CommonlyUsedObjects("Account", "Contact", "Case");
   private final ImmutableSet<String> names;

   public CommonlyUsedObjects(String... var1) {
      this.names = ImmutableSet.copyOf(var1);
   }

   public boolean contains(SObjectSchema var1) {
      return this.names.contains(var1.getName());
   }

   public Iterable<String> preferredOrder() {
      return this.names;
   }
}
