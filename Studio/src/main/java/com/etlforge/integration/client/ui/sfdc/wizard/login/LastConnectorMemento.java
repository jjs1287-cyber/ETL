package com.etlforge.integration.client.ui.sfdc.wizard.login;

import org.etlforge.integration.client.entity.settings.EntitySettings;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectUtils;
import org.etlforge.integration.data.entity.SalesforceConnector;
import org.etlforge.integration.data.entity.id.SalesforceConnectorId;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.util.persist.Persistor;

final class LastConnectorMemento {
   private static final String LAST_CONNECTOR_ID = "SfdcWizard.SelectedConnectorId";
   private final Persistor projectSettings;

   public LastConnectorMemento(IntegrationProject var1) {
      this.projectSettings = this.getProjectSettings(var1);
   }

   private Persistor getProjectSettings(IntegrationProject var1) {
      ManagedProject var2 = ProjectUtils.getManagedProject(var1);
      if (var2 == null) {
         return null;
      }

      EntitySettings var3 = var2.getEntityResources().getSettings();
      return var3.getEntitySettings(var1);
   }

   public SalesforceConnectorId getLast() {
      if (this.projectSettings == null) {
         return null;
      }

      String var1 = this.projectSettings.getString("SfdcWizard.SelectedConnectorId");
      if (var1 != null) {
         try {
            return new SalesforceConnectorId(var1);
         } catch (Exception var3) {
            var3.printStackTrace();
         }
      }

      return null;
   }

   public void store(SalesforceConnector var1) {
      if (this.projectSettings != null) {
         this.projectSettings.putString("SfdcWizard.SelectedConnectorId", var1.getID().toString());
      }
   }
}
