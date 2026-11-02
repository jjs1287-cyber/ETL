package org.etlforge.integration.data.functors;

import com.google.common.base.Predicate;
import org.etlforge.integration.data.entity.IntegrationEntity;

public interface EntityFilter extends Predicate<IntegrationEntity> {
   EntityFilter ACCEPT_ALL = new EntityFilter() {
      @Override
      public boolean apply(IntegrationEntity var1) {
         return true;
      }
   };
   EntityFilter REJECT_ALL = new EntityFilter() {
      @Override
      public boolean apply(IntegrationEntity var1) {
         return false;
      }
   };

   boolean apply(IntegrationEntity var1);
}
