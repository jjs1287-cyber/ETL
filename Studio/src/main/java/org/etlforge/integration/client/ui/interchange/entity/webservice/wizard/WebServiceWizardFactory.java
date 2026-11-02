package org.etlforge.integration.client.ui.interchange.entity.webservice.wizard;

import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.ui.entity.wizard.EntityWizardContext;
import org.etlforge.integration.client.ui.entity.wizard.EntityWizardFactory;
import org.etlforge.integration.client.ui.interchange.entity.EntityWizardCompleter;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.WebServiceCall;

public final class WebServiceWizardFactory extends EntityWizardFactory {
   @Override
   public Wizard<?> createWizard(EntityWizardContext var1, IntegrationEntity var2, Folder var3) {
      WebServiceWizard var4 = this.createWizardImpl(var1, (WebServiceCall)var2, var3);
      this.createWizardDisplayer(var4, var1, EntityType.WebServiceCall);
      return var4;
   }

   private WebServiceWizard createWizardImpl(EntityWizardContext var1, WebServiceCall var2, Folder var3) {
      ManagedProject var4 = var1.getProject().getExtensionObject(ManagedProject.class);
      WsdlFileStore var5 = new WsdlFileStore(var4.getFileStore());
      WebServiceWizard var6 = new WebServiceWizard(var1.getWindow(), var2, var3, var5, var4.getWebServiceMappingStructureCache());
      var6.addWizardListener(new EntityWizardCompleter<>(var6, var1.getProject(), var1.getProjectPersistor(), var3));
      return var6;
   }

   @Override
   protected String[] getSections() {
      return WizardSections.all();
   }
}
