package com.etlforge.integration.client.ui.sfdc.wizard;

import javax.swing.JComponent;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.AutoMapperAction;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.layout.NColumnPanel;
import org.etlforge.ui.widget.button.KongaButton;

final class FinishTransformationControlPanel {
   private final TransformationPage txPage;
   private final AutoMapperAction autoMapperAction;
   private final CloseMappingTransformationAction closeAction;

   public FinishTransformationControlPanel(TransformationPage var1, Editor var2) {
      this.txPage = var1;
      this.autoMapperAction = new AutoMapperAction(var1);
      this.autoMapperAction.putValue("Name", "&Auto Map");
      this.closeAction = new CloseMappingTransformationAction(var1, var2);
   }

   public void addToTransformationPage() {
      JComponent var1 = this.layoutButtons();
      this.txPage.layoutBelowMappingTree(var1);
   }

   private JComponent layoutButtons() {
      KongaButton var1 = this.createAutoMapButton();
      KongaButton var2 = this.createFinishButton();
      Grid var3 = Horizontal.grid().withHGap(16).of(var1, var2);
      NColumnPanel var4 = new NColumnPanel(3)
         .setAnchor(GridBagLayoutPanel.Anchor.CENTER)
         .setXWeights(0.5, 0.0, 0.5)
         .setFills(GridBagLayoutPanel.Fill.HORIZONTAL, GridBagLayoutPanel.Fill.NONE, GridBagLayoutPanel.Fill.HORIZONTAL);
      var4.addRow(" ", var3, " ");
      return var4.deeplyNonOpaque().container();
   }

   private KongaButton createAutoMapButton() {
      KongaButton var1 = new KongaButton(this.autoMapperAction).withMediumActionIcon();
      makeOverButton(var1);
      return var1;
   }

   private static void makeOverButton(KongaButton var0) {
      WizardStyleSheet.MEDIUM_CAPTION_STYLE.makeOver(var0);
   }

   private KongaButton createFinishButton() {
      KongaButton var1 = new KongaButton(this.closeAction);
      var1.setVerticalTextPosition(0);
      var1.setHorizontalTextPosition(10);
      makeOverButton(var1);
      return var1;
   }
}
