package org.etlforge.integration.client.connect.wizard.xml;

import org.etlforge.application.filestore.FileStore;
import org.etlforge.application.ui.wizard.ApplicationWindowWizard;
import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.application.ui.wizard.WizardPageChain;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.entity.wizard.EntitySelectionPage;
import org.etlforge.integration.client.ui.entity.wizard.NewFileLocationCallback;
import org.etlforge.integration.client.ui.structure.editor.xml.wizard.XmlStructureWizardPageChain;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.functors.FileBasedLocationFilter;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.structure.XmlStructure;
import org.etlforge.integration.filetransfer.client.upload.xsd.XsdFileStore;

public final class XmlPageChain<T extends IntegrationEntity> implements WizardPageChain {
   private final XmlStructureWizardPageChain xmlPages;
   private final EntitySelectionPage<T> locationSelectionPage;

   public XmlPageChain(ApplicationWindowWizard<?> var1, EntityExplorerSupport var2, EntityType var3, Class<T> var4) {
      XsdFileStore var5 = this.getXsdFiles(var2);
      this.xmlPages = new XmlStructureWizardPageChain(var1, var3 == EntityType.Source ? SourceTarget.Source : SourceTarget.Target, var5);
      this.locationSelectionPage = this.createLocationSelectionPage(var1, var2, var3, var4);
   }

   private XsdFileStore getXsdFiles(EntityExplorerSupport var1) {
      IntegrationProject var2 = var1.getProject();
      ManagedProject var3 = var2.getExtensionObject(ManagedProject.class);
      FileStore var4 = var3.getFileStore();
      return new XsdFileStore(var4);
   }

   private EntitySelectionPage<T> createLocationSelectionPage(ApplicationWindowWizard<?> var1, EntityExplorerSupport var2, EntityType var3, Class<T> var4) {
      String var5 = String.format("Select a %s", var3 == EntityType.Source ? "Source" : "Target");
      EntitySelectionPage var6 = new EntitySelectionPage(var1, var5, var2, var3, var4, new FileBasedLocationFilter(true));
      var6.setNewEntityCallback(new NewFileLocationCallback());
      String var7 = String.format("Select a File %s", var3 == EntityType.Source ? "Source" : "Target");
      var6.setMainCaption(var7);
      String var8 = String.format(
         "Select the location of the %s file that will be used in this connect operation.", var3 == EntityType.Source ? "source" : "target"
      );
      var6.setSubCaption(var8);
      return var6;
   }

   public EntitySelectionPage<T> getLocationSelectionPage() {
      return this.locationSelectionPage;
   }

   public XmlStructure getXmlStructure() {
      return this.xmlPages.getXmlStructure();
   }

   public T getLocation() {
      return this.locationSelectionPage.getSelectedEntity();
   }

   @Override
   public WizardPage getFirstPage() {
      return this.xmlPages.getFirstPage();
   }

   @Override
   public WizardPage getLastPage() {
      return this.locationSelectionPage;
   }

   @Override
   public boolean hasNextPage(WizardPage var1) {
      return var1 != this.locationSelectionPage;
   }

   @Override
   public WizardPage getNextPage(WizardPage var1) {
      if (var1 == this.locationSelectionPage) {
         return null;
      }

      WizardPage var2 = this.xmlPages.getNextPage(var1);
      if (var2 == null) {
         var2 = this.locationSelectionPage;
      }

      return var2;
   }

   @Override
   public boolean hasPreviousPage(WizardPage var1) {
      return var1 != this.xmlPages.getFirstPage();
   }

   @Override
   public WizardPage getPreviousPage(WizardPage var1) {
      if (var1 == this.getFirstPage()) {
         return null;
      } else {
         return var1 == this.locationSelectionPage ? this.xmlPages.getLastPage() : this.xmlPages.getPreviousPage(var1);
      }
   }

   @Override
   public boolean canFinish(WizardPage var1) {
      return false;
   }
}
