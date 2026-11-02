package org.etlforge.integration.data.entity.operation;

import com.google.common.collect.Sets;
import java.util.Set;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.SystemSource;
import org.etlforge.integration.data.entity.id.SourceId;
import org.etlforge.integration.data.functors.EntityFilter;

public class RunOperationFilter implements EntityFilter {
   private static final Set<SourceId> notSupported = Sets.newHashSet(
      SystemSource.SOURCE_FROM_PREVIOUS_OPERATION.getID(), SystemSource.FROM_HTTP_ENDPOINT.getID()
   );
   private final NextOperationFilter nextOpFilter = new NextOperationFilter();

   @Override
   public boolean apply(IntegrationEntity var1) {
      return this.nextOpFilter.apply(var1) && this.hasTriggerableSource((Operation)var1);
   }

   private boolean hasTriggerableSource(Operation var1) {
      return !notSupported.contains(var1.getSourceId());
   }
}
