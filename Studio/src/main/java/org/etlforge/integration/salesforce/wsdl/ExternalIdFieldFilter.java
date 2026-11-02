package org.etlforge.integration.salesforce.wsdl;

import com.google.common.base.Predicate;

public final class ExternalIdFieldFilter implements Predicate<SObjectSchema.Field> {
   public boolean apply(SObjectSchema.Field var1) {
      return var1.isExternalId() || var1.isIdLookup();
   }
}
