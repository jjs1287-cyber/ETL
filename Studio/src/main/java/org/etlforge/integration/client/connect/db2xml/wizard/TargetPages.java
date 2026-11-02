package org.etlforge.integration.client.connect.db2xml.wizard;

import org.etlforge.application.filestore.FileStore;
import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.application.ui.wizard.WizardPageChain;
import org.etlforge.integration.client.connect.wizard.xml.XmlPageChain;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.structure.editor.xml.wizard.XmlStructureOptionsPage;
import org.etlforge.integration.client.ui.structure.editor.xml.wizard.XmlStructureWizardPage;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.data.structure.XmlStructure;
import org.etlforge.integration.filetransfer.client.upload.xsd.XsdFileStore;

public final class TargetPages implements WizardPageChain {
   private final DatabaseToXmlWizard wizard;
   private final GenerateXsdPage generateXsdPage;
   private XmlStructureWizardPage validationOptionsPage;
   private final XmlPageChain<Target> standardChain;

   public TargetPages(DatabaseToXmlWizard var1, EntityExplorerSupport var2) {
      this.wizard = var1;
      this.generateXsdPage = new GenerateXsdPage(var1, this.getXsdFiles(var2));
      this.standardChain = new XmlPageChain<>(var1, var2, EntityType.Target, Target.class);
   }

   private XsdFileStore getXsdFiles(EntityExplorerSupport var1) {
      IntegrationProject var2 = var1.getProject();
      ManagedProject var3 = var2.getExtensionObject(ManagedProject.class);
      FileStore var4 = var3.getFileStore();
      return new XsdFileStore(var4);
   }

   public void setDatabaseStructure(DatabaseStructure var1) {
      this.generateXsdPage.setDatabaseStructure(var1);
   }

   @Override
   public WizardPage getFirstPage() {
      return this.generateXsdPage;
   }

   @Override
   public WizardPage getLastPage() {
      return this.standardChain.getLastPage();
   }

   @Override
   public boolean hasNextPage(WizardPage var1) {
      return var1 == this.generateXsdPage ? true : this.standardChain.hasNextPage(var1);
   }

   @Override
   public WizardPage getNextPage(WizardPage var1) {
      if (var1 == this.generateXsdPage) {
         if (this.generateXsdPage.isYesSelected()) {
            if (this.validationOptionsPage == null) {
               this.validationOptionsPage = new XmlStructureOptionsPage(this.wizard, SourceTarget.Target, "");
            }

            return this.validationOptionsPage;
         } else {
            return this.standardChain.getFirstPage();
         }
      } else {
         return var1 == this.validationOptionsPage ? this.standardChain.getLocationSelectionPage() : this.standardChain.getNextPage(var1);
      }
   }

   @Override
   public boolean hasPreviousPage(WizardPage var1) {
      if (var1 == this.generateXsdPage) {
         return false;
      } else if (var1 == this.validationOptionsPage) {
         return true;
      } else {
         return var1 == this.standardChain.getFirstPage() ? true : this.standardChain.hasPreviousPage(var1);
      }
   }

   @Override
   public WizardPage getPreviousPage(WizardPage var1) {
      if (var1 == this.generateXsdPage) {
         return null;
      }

      if (var1 == this.validationOptionsPage) {
         return this.generateXsdPage;
      }

      if (var1 == this.standardChain.getLocationSelectionPage() && this.generateXsdPage.isYesSelected()) {
         return this.validationOptionsPage;
      }

      WizardPage var2 = this.standardChain.getPreviousPage(var1);
      if (var2 == null) {
         var2 = this.generateXsdPage;
      }

      return var2;
   }

   @Override
   public boolean canFinish(WizardPage var1) {
      return var1 == this.standardChain.getLocationSelectionPage();
   }

   public XmlStructure getXmlStructure() {
      if (this.generateXsdPage.isYesSelected()) {
         XmlStructure var1 = new XmlStructure();
         this.generateXsdPage.applyTo(var1);
         this.validationOptionsPage.applyTo(var1);
         return var1;
      } else {
         return this.standardChain.getXmlStructure();
      }
   }

   public Target getLocation() {
      return this.standardChain.getLocation();
   }
}
