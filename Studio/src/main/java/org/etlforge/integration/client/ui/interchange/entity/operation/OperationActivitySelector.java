package org.etlforge.integration.client.ui.interchange.entity.operation;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JRadioButton;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.integration.client.ui.operation.OperationTypeIcons;
import org.etlforge.integration.client.ui.operation.OperationTypeMnemonics;
import org.etlforge.integration.data.entity.operation.OperationType;
import org.etlforge.integration.data.entity.operation.TransformationOperationType;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.RadioButtonGroupPanel;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.CaptionLayout;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.widget.label.KongaLabel;

final class OperationActivitySelector implements UiProvider {
   private final OperationActivitySelector.OptionsPanel buttons = createButtons();
   private static OperationActivitySelector.OptionsPanel createButtons() {
      OperationActivitySelector.OptionsPanel var0 = new OperationActivitySelector.OptionsPanel();

      for (OperationType var2 : OperationType.getSelectableTypes()) {
         var0.addOption(var2, null);
      }

      var0.setSelectedValue(TransformationOperationType.TYPE);
      return var0;
   }

   public OperationType getSelection() {
      return this.buttons.getSelection();
   }

   @Override
   public void decorate(UiCanvas var1) {
      JComponent var2 = this.doLayout();
      var1.display(var2);
   }

   private JComponent doLayout() {
      UiProvider var1 = this.layoutCaption();
      UiProvider var2 = this.buttons.getUi(null, RadioButtonGroupPanel.Layout.ONE_COLUMN);
      return CaptionLayout.of(var1, var2).container();
   }

   private UiProvider layoutCaption() {
      return Vertical.column()
         .setWeightX(1.0)
         .withInsets(0, 0, 5, 0)
         .of(WizardStyleSheet.caption("Operation Activity"));
   }

   @Override
   public void requestFocus() {
      this.buttons.requestFocus();
   }

   private static class OptionsPanel extends RadioButtonGroupPanel<OperationType> {
      private OptionsPanel() {
      }

      protected JComponent getButtonLayout(JRadioButton var1, OperationType var2) {
         MnemonicString var3 = OperationTypeMnemonics.get(var2);
         Icon var4 = OperationTypeIcons.smallIcon(var2);
         KongaLabel var5 = new KongaLabel(var3);
         var5.setIcon(var4);
         var5.setLabelForButton(var1);
         return BoxBuilder.horizontal().nonOpaque().addAllWithSpace(5, var1, var5).container();
      }
   }
}



