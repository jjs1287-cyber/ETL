package org.etlforge.integration.client.ui.structure.editor.xml.wizard;

import java.util.Collection;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardPageLayout;
import org.etlforge.integration.data.structure.XmlStructure;

public final class XmlStructureWizardRootNodePage extends AbstractWizardPage implements XmlStructureWizardPage {
   private final RootNodePageUi ui;

   public XmlStructureWizardRootNodePage(Wizard var1, Collection<String> var2, String var3) {
      super(var1, var3);
      this.ui = new RootNodePageUi(this, var2);
      this.ui.installUndoRedo(this.getUndoRedoInstaller());
   }

   XmlStructureWizardRootNodePage(Wizard var1, Collection<String> var2) {
      this(var1, var2, PackageResources.RootNodePage.DEFAULT_SUB_TITLE);
   }

   void setStructureToEdit(XmlStructure var1) {
      this.ui.setSelectedRoot(var1);
   }

   @Override
   public void requestFocus() {
      this.ui.requestFocus();
   }

   @Override
   public boolean prepareForForwardNavigation() {
      return this.ui.isInputValid();
   }

   @Override
   public void applyTo(XmlStructure var1) {
      String var2 = this.ui.getSelectedRoot();
      if (var2.length() > 0) {
         var1.setRootNodeName(var2);
      }
   }

   @Override
   protected void dispose() {
      this.ui.dispose();
   }

   @Override
   protected JPanel layoutPage() {
      WizardPageLayout var1 = WizardStyleSheet.emptyPageLayout(PackageResources.RootNodePage.MAIN_CAPTION);
      var1.add(this.ui, true, true);
      return var1.asCenterBorderLayout();
   }
}
