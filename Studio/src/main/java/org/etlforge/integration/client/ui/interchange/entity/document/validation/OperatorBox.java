package org.etlforge.integration.client.ui.interchange.entity.document.validation;

import com.google.common.base.Optional;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.ui.Empty;
import org.etlforge.ui.input.ComboBoxInputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.property.Bindings;
import org.etlforge.ui.property.ComboBoxBinding;
import org.etlforge.ui.widget.combobox.KongaComboBox;
import org.etlforge.ui.widget.label.KongaLabel;

public final class OperatorBox {
   private static final String NONE = "(None)";
   private final JLabel label = KongaLabel.withMnemonic("Content Vali&dation");
   private final FieldValidationModel model;
   private final ComboBoxInputField operatorDropdown = new ComboBoxInputField("Validation", new KongaComboBox());
   private final BorderLayoutBuilder argumentCapsule = new BorderLayoutBuilder();
   private ArgumentUi argUi;
   private InputPanel inputPanel;
   private final Optional<EntityExplorerSupport> projectExplorer;

   public OperatorBox(FieldValidationModel var1) {
      this(var1, Optional.absent());
   }

   public OperatorBox(FieldValidationModel var1, Optional<EntityExplorerSupport> var2) {
      this.model = var1;
      this.projectExplorer = var2;
      this.operatorDropdown.setMaximumRowCount(16);
      this.operatorDropdown.setRenderer(new DefaultListCellRenderer() {
         @Override
         public Component getListCellRendererComponent(JList var1, Object var2x, int var3, boolean var4, boolean var5) {
            super.getListCellRendererComponent(var1, var2x, var3, var4, var5);
            this.setEnabled(var2x != null && OperatorBox.this.operatorDropdown.isEnabled());
            if (var2x == null) {
               this.setText("(None)");
            }

            return this;
         }
      });
      this.label.setLabelFor(this.operatorDropdown.getInputComponent());
      this.bindWithModel();
   }

   private void bindWithModel() {
      Bindings var1 = new Bindings(new OperatorBox.ArgTypeBinding(), this.createOperatorBinding());
      var1.syncUi();
   }

   private ComboBoxBinding createOperatorBinding() {
      ComboBoxBinding var1 = new ComboBoxBinding(
         this.operatorDropdown.getInputComponent(), this.model.getProperty("operators"), this.model.getProperty("operator")
      );
      var1.setNullSelectionMessage("(None)");
      var1.setSelectionPolicyOnContentChange(ComboBoxBinding.SelectionPolicy.RESTORE);
      return var1;
   }

   public void setEnabled(boolean var1) {
      this.label.setEnabled(var1);
      this.operatorDropdown.setEnabled(var1);
      this.argUi.setEnabled(var1);
   }

   public void registerAsChangeSource(InputPanel var1) {
      this.inputPanel = var1;
      var1.addInputField(this.operatorDropdown);
      if (this.argUi != null) {
         this.argUi.registerAsChangeSource(var1);
      }
   }

   public void layoutIn(TwoColumnPanel var1) {
      this.label.setBorder(Empty.border(8, 0, 0, 0));
      var1.withAnchor(GridBagLayoutPanel.Anchor.NORTHWEST)
         .withRightFill(GridBagLayoutPanel.Fill.BOTH)
         .withRightWeights(1.0, 1.0)
         .addRow(this.label, this.createUi());
   }

   private JComponent createUi() {
      return new TwoColumnPanel()
         .withAnchor(GridBagLayoutPanel.Anchor.NORTHWEST)
         .withLeftFill(GridBagLayoutPanel.Fill.NONE)
         .withLeftWeights(0.0, 0.0)
         .withLeftInsets(4, 0, 0, 10)
         .withRightFill(GridBagLayoutPanel.Fill.BOTH)
         .withRightWeights(1.0, 1.0)
         .addRow(this.operatorDropdown.getInputComponent(), this.argumentCapsule.container())
         .container();
   }

   private class ArgTypeBinding extends AbstractBinding<Class> {
      public ArgTypeBinding() {
         super(OperatorBox.this.model, "argumentType");
      }

      protected void updateUiWithValue(Class var1) {
         if (OperatorBox.this.argUi != null) {
            OperatorBox.this.argUi.dispose();
         }

         OperatorBox.this.argumentCapsule.removeAll();
         ArgumentUi var2 = ArgumentUi.forSelectedOperator(OperatorBox.this.model, OperatorBox.this.projectExplorer);
         OperatorBox.this.argumentCapsule.north(var2);
         OperatorBox.this.argumentCapsule.refreshLayout();
         OperatorBox.this.argumentCapsule.deeplyNonOpaque();
         if (OperatorBox.this.inputPanel != null) {
            var2.registerAsChangeSource(OperatorBox.this.inputPanel);
         }

         var2.requestFocus();
         OperatorBox.this.argUi = var2;
      }
   }
}
