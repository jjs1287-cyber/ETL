package org.etlforge.integration.client.ui.structure.editor.text;

import org.etlforge.application.ui.wizard.ApplicationWindowWizard;
import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.application.ui.wizard.WizardPageList;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.entity.wizard.EntitySelectionPage;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.TextDocument;

public final class DocumentSelectionPageChain extends WizardPageList {
   private final EntitySelectionPage<TextDocument> page;

   public DocumentSelectionPageChain(ApplicationWindowWizard var1, EntityExplorerSupport var2) {
      this(var1, null, var2);
   }

   public DocumentSelectionPageChain(ApplicationWindowWizard var1, TextDocument var2, EntityExplorerSupport var3) {
      this.page = new EntitySelectionPage<>(var1, PackageResources.DocumentSelectionWizard.TITLE, var3, EntityType.Document, TextDocument.class, null);
      if (var2 != null) {
         this.page.setSelectedEntity(var2);
      }

      this.page.setMainCaption(PackageResources.DocumentSelectionWizard.MAIN_CAPTION);
      this.page.setSubCaption(PackageResources.DocumentSelectionWizard.SUB_CAPTION);
      this.page.setSelectorLabel(PackageResources.DocumentSelectionWizard.COMBO_LABEL);
      this.addPage(this.page);
   }

   @Override
   public boolean canFinish(WizardPage var1) {
      return var1 == this.page && var1.canContinue();
   }

   public TextDocument getSelectedDocument() {
      return this.page.getSelectedEntity();
   }

   public void setMainCaption(String var1) {
      this.page.setMainCaption(var1);
   }

   public void setSubCaption(String var1) {
      this.page.setSubCaption(var1);
   }

   public void setTitle(String var1) {
      this.page.setTitle(var1);
   }
}
