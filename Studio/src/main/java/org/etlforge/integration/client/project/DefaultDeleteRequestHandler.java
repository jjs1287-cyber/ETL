package org.etlforge.integration.client.project;

import java.util.List;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.DeleteRequestHandler;
import org.etlforge.integration.data.project.IntegrationProject;

public final class DefaultDeleteRequestHandler implements DeleteRequestHandler {
   @Override
   public boolean deleteRequested(final List<IntegrationEntity> var1) {
      final IntegrationProject var2 = this.getProject(var1);
      return var2 == null ? false : (new ProjectAlteringProcessProcess<Boolean>(var2) {
         protected Boolean doWork() {
            var2.deleteItems(var1);
            return true;
         }
      }).run();
   }

   private IntegrationProject getProject(List<IntegrationEntity> var1) {
      for (IntegrationEntity var3 : var1) {
         IntegrationProject var4 = ProjectUtils.getProject(var3);
         if (var4 != null) {
            return var4;
         }
      }

      return null;
   }
}
