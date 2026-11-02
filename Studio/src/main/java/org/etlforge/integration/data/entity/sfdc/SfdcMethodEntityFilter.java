package org.etlforge.integration.data.entity.sfdc;

import com.google.common.base.Preconditions;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.salesforce.SfdcMethod;

public final class SfdcMethodEntityFilter implements EntityFilter {
   private final SfdcMethod method;

   public SfdcMethodEntityFilter(SfdcMethod var1) {
      this.method = Preconditions.checkNotNull(var1);
   }

   @Override
   public boolean apply(IntegrationEntity var1) {
      EntityType var2 = var1.getEntityType();
      switch (this.method) {
         case QUERY:
         case QUERY_ALL:
            return var2 == EntityType.SalesforceQuery;
         case UPSERT:
            return var2 == EntityType.SalesforceUpsert;
         case UPDATE:
            return var2 == EntityType.SalesforceUpdate;
         case CREATE:
            return var2 == EntityType.SalesforceCreate;
         case DELETE:
            return var2 == EntityType.SalesforceDelete;
         default:
            throw new UnsupportedOperationException("method not applicable - or filter-implementation is missing! - for method " + this.method);
      }
   }
}
