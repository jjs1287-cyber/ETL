package org.etlforge.integration.client.ui.interchange.entity.document.page;

import javax.swing.Action;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.integration.client.info.ClientInfo;
import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.widget.panel.InvisiblePanel;

class NewDocumentPanel extends TextDocumentDefiner {
   private final TypeAndFormatDefinitionPanel typeAndFormatPanel;

   public NewDocumentPanel(NewTextDocumentModel var1, Action var2, Action var3) {
      this.typeAndFormatPanel = new TypeAndFormatDefinitionPanel(var1);
      this.doLayout(var2, var3);
      if (!ClientInfo.isDataLoader()) {
         this.installQuickSelector(var1, var2);
      }
   }

   private void doLayout(Action var1, Action var2) {
      UiProvider var3 = this.createUi(var1, var2);
      this.wrap(var3);
   }

   private UiProvider createUi(Action var1, Action var2) {
      OneColumnPanel var3 = new OneColumnPanel().stretchHorizontally().withInsets(0, 0, 24, 0);
      UiProvider var4 = this.layoutCreateManuallySection(var1);
      UiProvider var5 = this.layoutCreateFromFileSection(var2);
      if (ClientInfo.isDataLoader()) {
         var3.addAll(var5, var4);
      } else {
         var3.addAll(var4, var5);
      }

      return var3;
   }

   private static UiProvider layoutSection(String var0, Object var1) {
      OneColumnPanel var2 = new OneColumnPanel().stretchHorizontally();
      var2.add(WizardStyleSheet.CAPTION_STYLE.makeLabel(var0));
      return var2.withInsets(10, 32, 0, 0).add(var1);
   }

   private UiProvider layoutCreateManuallySection(Action var1) {
      OneColumnPanel var2 = new OneColumnPanel();
      var2.withInsets(0, 0, 15, 0);
      var2.fillOut(this.typeAndFormatPanel.getUi());
      var2.noFill(InvisiblePanel.of(var1));
      return layoutSection("Create Manually", var2);
   }

   private UiProvider layoutCreateFromFileSection(Action var1) {
      return layoutSection("From Sample File", InvisiblePanel.of(var1));
   }

   private void installQuickSelector(NewTextDocumentModel var1, Action var2) {
      NewDocumentTypeQuickSelector var3 = new NewDocumentTypeQuickSelector(var1, this.typeAndFormatPanel.getUi(), var2);
      ActionKeyBinding.install(var3, this.typeAndFormatPanel.getUi(), 2);
   }

   @Override
   public void applyToTextDocument(TextDocument var1) {
      this.typeAndFormatPanel.applyToTextDocument(var1);
   }

   @Override
   public void stopEditing() {
      this.typeAndFormatPanel.stopEditing();
   }
}
