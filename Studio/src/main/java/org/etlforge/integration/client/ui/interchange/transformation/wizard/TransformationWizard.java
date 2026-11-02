package org.etlforge.integration.client.ui.interchange.transformation.wizard;

import com.google.common.base.Preconditions;
import org.etlforge.application.filestore.FileStore;
import org.etlforge.application.ui.wizard.WizardPageChain;
import org.etlforge.application.ui.wizard.WizardPageException;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.entity.wizard.AbstractEntityWizard;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Transformation;

public final class TransformationWizard extends AbstractEntityWizard<Transformation> {
   private final WizardConfiguration configuration;
   private final EntityExplorerSupport projectBrowser;
   private final FileStore fileStore;

   public TransformationWizard(WizardConfiguration var1, Transformation var2, Folder var3, EntityExplorerSupport var4, FileStore var5) {
      super(var4.getWindow(), var2, var3);
      this.configuration = Preconditions.checkNotNull(var1, "configuration");
      this.projectBrowser = Preconditions.checkNotNull(var4, "projectBrowser");
      this.fileStore = Preconditions.checkNotNull(var5, "fileStore");
   }

   public IntegrationEntityLookup getItemLookup() {
      return this.projectBrowser.getProject().getItemLookup();
   }

   public EntityExplorerSupport getEntityExplorerSupport() {
      return this.projectBrowser;
   }

   public FileStore getFileStore() {
      return this.fileStore;
   }

   public WizardConfiguration getConfiguration() {
      return this.configuration;
   }

   public Transformation getTransformation() {
      return this.getEntityBeingCreated();
   }

   protected WizardPageChain createPageChain(Transformation var1, Folder var2) {
      return new TransformationWizardPageChain(this, var1, var2);
   }

   @Override
   protected String getStartSection() {
      return "Name";
   }

   protected void applyPageInformation(Transformation var1) throws WizardPageException {
      try {
         ((TransformationWizardPageChain)this.getPageChain()).applyTo(var1);
      } catch (WizardPageException var3) {
         this.handlePageException(var3.getMessage(), var3);
         this.flagAsSaved();
         throw var3;
      }
   }
}
