package org.etlforge.integration.jitterpack.impl;

import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.system.callback.Receiver;

final class PostUnpackProcessor {
   public static void processUnpack(IntegrationProject var0, boolean var1) {
      boolean var2 = !var1;
      var0.setDeployable(var2);
      if (var2) {
         makeProjectDeployDirty(var0);
      }
   }

   private static void makeProjectDeployDirty(IntegrationProject var0) {
      var0.visitAllEntities(new Receiver<IntegrationEntity>() {
         public void handle(IntegrationEntity var1) {
            var1.setDeployDirty(true);
         }
      });
   }

   private PostUnpackProcessor() {
   }
}
