package org.etlforge.integration.client.ui.entity.move;

import com.google.common.base.Preconditions;
import java.util.Collections;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.DeleteRequestHandler;

public final class ReplaceExistingEntityResolver implements ConflictResolver {
   private final DeleteRequestHandler deleter;

   public ReplaceExistingEntityResolver(DeleteRequestHandler var1) {
      this.deleter = Preconditions.checkNotNull(var1, "deleter");
   }

   @Override
   public boolean resolve(IntegrationEntity var1, IntegrationEntity var2, IntegrationEntity var3) {
      boolean var4 = this.deleter.deleteRequested(Collections.singletonList(var3));
      if (var4) {
         var1.setParent(var2);
         return true;
      } else {
         return false;
      }
   }
}
