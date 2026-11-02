package org.etlforge.integration.salesforce.wsdl;

import com.google.common.base.Predicate;
import org.etlforge.integration.salesforce.SfdcMethod;

public final class ObjectMethodFilter implements Predicate<SObjectSchema> {
   private final SfdcMethod method;

   public ObjectMethodFilter(SfdcMethod var1) {
      this.method = var1;
   }

   public boolean apply(SObjectSchema var1) {
      switch (this.method) {
         case QUERY:
            return var1.isQueryable();
         case UPSERT:
            return var1.isCreatable() || var1.isUpdatable();
         case UPDATE:
            return var1.isUpdatable();
         case CREATE:
            return var1.isCreatable();
         case DELETE:
         case HARD_DELETE:
            return var1.isDeletable();
         default:
            throw new UnsupportedOperationException();
      }
   }
}
