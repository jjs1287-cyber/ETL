package com.etlforge.integration.client.sfdc.project.upgrade.v29;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.google.common.io.Resources;
import com.etlforge.integration.client.sfdc.project.upgrade.CromHeaderInserter;
import com.etlforge.integration.client.sfdc.project.upgrade.QueryRequestReplacer;
import java.net.URL;
import java.util.logging.Logger;
import org.etlforge.application.filestore.FileCollisionResolver;
import org.etlforge.application.filestore.FileStore;
import org.etlforge.integration.client.project.DataFolder;
import org.etlforge.integration.client.project.EntityDataDirectory;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectFile;
import org.etlforge.integration.client.project.ProjectUtils;
import org.etlforge.integration.client.project.ProjectVersionHandler;
import org.etlforge.integration.client.project.UpgradeStrategy;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.SalesforceConnector;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.integration.data.entity.transformation.SalesforceWizardRole;
import org.etlforge.integration.data.entity.webservicecall.WsdlFile;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.project.upgrade.DefaultPreLoader;
import org.etlforge.integration.salesforce.SalesforceVersion;
import org.etlforge.integration.salesforce.soap.header.OwnerChangeOptionsHeader;
import org.etlforge.integration.salesforce.wsdl.partner.crom.LimitInfoCromHeader;
import org.etlforge.integration.salesforce.wsdl.partner.crom.OwnerChangeOptionsCromHeader;
import org.etlforge.io.Folder;

final class V29Upgrade implements ProjectVersionHandler {
   private boolean requiresUpgrade;
   private Logger logger = Logger.getAnonymousLogger();

   public void setLogger(Logger var1) {
      this.logger = var1;
   }

   @Override
   public ProjectVersionHandler.PreLoadingResult preLoading(String var1, ProjectFile var2, UpgradeStrategy var3) {
      this.requiresUpgrade = this.isUpgradeRequired(Folder.parentOf(var2.getFile()));
      if (!this.requiresUpgrade) {
         return ProjectVersionHandler.PreLoadingResult.NOT_APPLICABLE;
      }

      DefaultPreLoader var4 = new DefaultPreLoader(var1, var2, var3);
      var4.run();
      return var4.getResult();
   }

   private boolean isUpgradeRequired(Folder var1) {
      DataFolder var2 = DataFolder.in(var1);
      EntityDataDirectory var3 = var2.getEntityTypeFolder(EntityType.SalesforceConnector);
      return !var3.isEmpty();
   }

   @Override
   public boolean postLoading(IntegrationProject var1, ProjectFile var2) {
      if (!this.requiresUpgrade) {
         return false;
      }

      this.logger.info("Upgrading the project " + var1.getName() + " to use version 29 of the Salesforce API.");
      this.updateOrgVersionNumber(var1);
      this.updateWsdlLocators(var1);
      this.removeOldQueryRequestTransformations(var1);
      this.updateRequestTransformations(var1);
      this.updateResponseTransformations(var1);
      return true;
   }

   private void updateOrgVersionNumber(IntegrationProject var1) {
      this.logger.info("Updating Salesforce Orgs to use new version");

      for (SalesforceConnector var3 : var1.getAllEntitiesOfClass(SalesforceConnector.class)) {
         this.logger.info("Updating " + var3);
         var3.setSalesforceVersion(SalesforceVersion.V_29);
         var3.setDeployDirty(true);
      }
   }

   private void updateWsdlLocators(IntegrationProject var1) {
      this.logger.info("Updating Web Service Calls to use new WSDL locator: etlforge.salesforce.partner.29.wsdl");
      boolean var2 = false;

      for (WebServiceCall var4 : var1.getAllEntitiesOfClass(WebServiceCall.class)) {
         if (var4.isPartOfSalesforcePartnerWizard()) {
            this.logger.info("Upgrading: " + var4);
            WsdlFile var5 = new WsdlFile("etlforge.salesforce.partner.29.wsdl");
            var4.setWsdlFile(var5);
            var4.setDeployDirty(true);
            var2 = true;
         }
      }

      if (var2) {
         this.copyNewPartnerWsdlIntoFileStore(var1);
      }
   }

   private void copyNewPartnerWsdlIntoFileStore(IntegrationProject var1) {
      try {
         this.logger.info("Now copying the new partner WSDL into the local filestore.");
         ManagedProject var2 = ProjectUtils.getManagedProject(var1);
         FileStore var7 = var2.getFileStore();
         URL var4 = Resources.getResource("etlforge.salesforce.partner.29.wsdl");
         byte[] var5 = Resources.toByteArray(var4);
         var7.addFile("etlforge.salesforce.partner.29.wsdl", var5, FileCollisionResolver.REPLACE_EXISTING);
      } catch (Exception var6) {
         this.logger.severe(Throwables.getStackTraceAsString(var6));
         String var3 = String.format(
            "Failed to copy the new partner WSDL (%s) into the local filestore. Reason: %s", "etlforge.salesforce.partner.29.wsdl", var6.getMessage()
         );
         throw new RuntimeException(var3, var6);
      }
   }

   private void removeOldQueryRequestTransformations(IntegrationProject var1) {
      QueryRequestReplacer var2 = new QueryRequestReplacer(var1);
      var2.replaceRequests();
   }

   private void updateRequestTransformations(IntegrationProject var1) {
      CromHeaderInserter var2 = new CromHeaderInserter(
         SalesforceWizardRole.Position.REQUEST,
         Sets.immutableEnumSet(EntityType.SalesforceUpdate, EntityType.SalesforceUpsert),
         ImmutableList.of(new OwnerChangeOptionsHeader()),
         ImmutableList.of(new OwnerChangeOptionsCromHeader()),
         "EmailHeader"
      );
      var2.setLogger(this.logger);
      var2.updateTransformations(var1);
   }

   private void updateResponseTransformations(IntegrationProject var1) {
      this.updateQueryResponseTransformations(var1);
      this.updateOtherResponseTransformations(var1);
   }

   private void updateQueryResponseTransformations(IntegrationProject var1) {
      CromHeaderInserter var2 = new CromHeaderInserter(
         SalesforceWizardRole.Position.RESPONSE,
         Sets.immutableEnumSet(EntityType.SalesforceQuery),
         ImmutableList.of(),
         ImmutableList.of(new LimitInfoCromHeader()),
         null
      );
      var2.setLogger(this.logger);
      var2.updateTransformations(var1);
   }

   private void updateOtherResponseTransformations(IntegrationProject var1) {
      CromHeaderInserter var2 = new CromHeaderInserter(
         SalesforceWizardRole.Position.RESPONSE,
         Sets.immutableEnumSet(EntityType.SalesforceUpdate, EntityType.SalesforceUpsert, EntityType.SalesforceCreate, EntityType.SalesforceDelete),
         ImmutableList.of(),
         ImmutableList.of(new LimitInfoCromHeader()),
         "DebuggingInfo"
      );
      var2.setLogger(this.logger);
      var2.updateTransformations(var1);
   }
}
