package org.etlforge.integration.client.ui.interchange.entity.document.wizard;

import java.awt.Component;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardDialogDisplayer;
import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactory;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.ui.wait.WaitService;

public final class TreeMapperWizardDialogContainer extends AbstractTreeMapperWizardContainer {
   private final WizardDialogDisplayer adaptee = new WizardDialogDisplayer();

   public TreeMapperWizardDialogContainer(UiEntityFactory var1, ProjectPersistor var2) {
      super(var1, var2);
      this.adaptee.setModal(true);
   }

   @Override
   public WizardPage getCurrentPage() {
      return this.adaptee.getCurrentPage();
   }

   @Override
   public void handleContextMenuRequest(Component var1, int var2, int var3) {
      this.adaptee.handleContextMenuRequest(var1, var2, var3);
   }

   @Override
   public void updateTitle() {
      this.adaptee.updateTitle();
   }

   @Override
   public void showPage(WizardPage var1) {
      this.adaptee.showPage(var1);
   }

   @Override
   public void updateNavigationControls() {
      this.adaptee.updateNavigationControls();
   }

   @Override
   public void setWizard(Wizard var1) {
      super.setWizard(var1);
      this.adaptee.setWizard(var1);
   }

   @Override
   public WaitService getWaitService() {
      return this.adaptee.getWaitService();
   }

   @Override
   public void setSection(String var1) {
   }

   @Override
   public String getSection() {
      return null;
   }

   @Override
   public void setSections(String[] var1) {
   }
}
