package org.etlforge.integration.client.ui.deploy.option;

import org.etlforge.integration.client.entity.settings.EntitySettings;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.util.persist.Persistor;

public class DeployOptionPreference {
   private static final String KEY = "DeployOption";
   private final ManagedProject project;

   public DeployOptionPreference(ManagedProject var1) {
      this.project = var1;
   }

   public void write(DeployOption var1) {
      Persistor var2 = this.getStorage();
      var2.putString("DeployOption", var1.getId());
   }

   public DeployOption read(DeployOption[] var1) {
      Persistor var2 = this.getStorage();
      if (var2 != null) {
         String var3 = var2.getString("DeployOption");
         if (var3 != null) {
            return this.findCandidateWithId(var1, var3);
         }
      }

      return null;
   }

   private DeployOption findCandidateWithId(DeployOption[] var1, String var2) {
      for (DeployOption var6 : var1) {
         if (var6.getId().equals(var2)) {
            return var6;
         }
      }

      return null;
   }

   private Persistor getStorage() {
      EntitySettings var1 = this.project.getEntityResources().getSettings();
      return var1.getClassSettings(EntityType.Project);
   }
}
