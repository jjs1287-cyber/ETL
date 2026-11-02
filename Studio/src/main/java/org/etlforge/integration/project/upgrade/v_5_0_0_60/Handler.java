package org.etlforge.integration.project.upgrade.v_5_0_0_60;

import org.etlforge.integration.client.project.ProjectFile;
import org.etlforge.integration.client.project.ProjectVersionHandler;
import org.etlforge.integration.client.project.UpgradeStrategy;
import org.etlforge.integration.data.entity.EmailMessage;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.project.upgrade.DefaultPreLoader;

final class Handler implements ProjectVersionHandler {
   private final boolean automatic;

   public Handler(boolean var1) {
      this.automatic = var1;
   }

   @Override
   public ProjectVersionHandler.PreLoadingResult preLoading(String var1, ProjectFile var2, UpgradeStrategy var3) {
      if (this.automatic) {
         return ProjectVersionHandler.PreLoadingResult.AUTOMATIC;
      }

      DefaultPreLoader var4 = new DefaultPreLoader(var1, var2, var3);
      var4.run();
      return var4.getResult();
   }

   @Override
   public boolean postLoading(IntegrationProject var1, ProjectFile var2) {
      LocatableUpgrade.run(var1);
      SalesforceOrgUpgrade.run(var1);
      GeneralUpgrade var3 = GeneralUpgrade.create(EmailMessage.class, "account_password");
      var3.run(var1);
      GeneralUpgrade var4 = GeneralUpgrade.create(WebServiceCall.class, "httppassword");
      var4.run(var1);
      return true;
   }
}
