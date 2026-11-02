package org.etlforge.integration.salesforce.wsdl;

import com.google.common.base.Predicate;
import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.integration.salesforce.bulkload.BulkAction;

public final class AutoMappableFieldFilter implements Predicate<SObjectSchema.Field> {
   private final SfdcMethod sfdcMethod;

   public AutoMappableFieldFilter(BulkAction var1) {
      this(var1.toSfdcMethod());
   }

   public AutoMappableFieldFilter(SfdcMethod var1) {
      this.sfdcMethod = var1;
   }

   public boolean apply(SObjectSchema.Field var1) {
      switch (this.sfdcMethod) {
         case CREATE:
            return var1.isCreateable();
         case UPDATE:
            return var1.isIdLookup() || var1.isExternalId() || var1.isUpdateable();
         case UPSERT:
            return var1.isIdLookup() || var1.isExternalId() || var1.isCreateable() || var1.isUpdateable();
         default:
            return true;
      }
   }
}
