package org.etlforge.integration.client.ui.entity.move;

import com.google.common.base.Supplier;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.data.entity.EntityUtils;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.util.name.IllegalNameException;

public final class RenameMovedEntityResolver implements ConflictResolver {
   private final Supplier<String> nameSupplier;

   public RenameMovedEntityResolver(Supplier<String> var1) {
      this.nameSupplier = var1;
   }

   @Override
   public boolean resolve(IntegrationEntity var1, IntegrationEntity var2, IntegrationEntity var3) {
      String var4 = this.nameSupplier.get();
      return var4 != null ? this.rename(var1, var2, var4) : false;
   }

   private boolean rename(IntegrationEntity var1, IntegrationEntity var2, String var3) {
      try {
         this.ensureNameIsUnique(var2, var3);
         synchronized (var1.getDataLock()) {
            var1.setParent(null);
            var1.setName(var3);
            var1.setParent(var2);
         }

         return true;
      } catch (IllegalNameException var7) {
         ErrorLog.log(this.getClass(), null, var7);
         return false;
      }
   }

   private void ensureNameIsUnique(IntegrationEntity var1, String var2) throws IllegalNameException {
      if (!EntityUtils.isChildNameUnique(var1, var2)) {
         throw new IllegalNameException("The name " + var2 + " is already taken.");
      }
   }
}
