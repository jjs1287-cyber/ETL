package com.etlforge.integration.client.ui.sfdc.ucu;

import javax.swing.border.Border;
import org.etlforge.integration.data.entity.sfdc.SfdcDebugMode;
import org.etlforge.integration.data.entity.sfdc.UCURequestHeader;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.CheckBoxInputField;
import org.etlforge.ui.input.ComboBoxInputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.widget.button.KongaCheckBox;
import org.etlforge.ui.widget.combobox.EnumComboBox;
import org.etlforge.ui.widget.panel.InvisiblePanel;

public final class RequestHeaderUi implements UiProvider {
   private final KongaCheckBox truncateFieldsChoice = new KongaCheckBox("&Truncate fields if needed");
   private final KongaCheckBox triggerEmailsChoice = new KongaCheckBox("Trigger &emails");
   private final EnumComboBox<SfdcDebugMode> debugModeSelector = EnumComboBox.of(SfdcDebugMode.class);
   private Border border;

   public RequestHeaderUi() {
      this(null);
   }

   public RequestHeaderUi(InputPanel var1) {
      this.debugModeSelector.setLabel("&Debug mode");
      this.setRequestHeader(new UCURequestHeader());
      if (var1 != null) {
         this.addInputFields(var1);
      }
   }

   private void addInputFields(InputPanel var1) {
      var1.addInputField(new CheckBoxInputField("Truncate", this.truncateFieldsChoice));
      var1.addInputField(new CheckBoxInputField("Email", this.triggerEmailsChoice));
      var1.addInputField(new ComboBoxInputField("Debug mode", this.debugModeSelector));
   }

   public void setBorder(Border var1) {
      this.border = var1;
   }

   public UCURequestHeader getDefinedHeader() {
      return new UCURequestHeader(this.truncateFieldsChoice.isSelected(), this.triggerEmailsChoice.isSelected(), this.debugModeSelector.getSelectedItem());
   }

   public void setRequestHeader(UCURequestHeader var1) {
      this.truncateFieldsChoice.setSelected(var1.isTruncateFieldsEnabled());
      this.triggerEmailsChoice.setSelected(var1.isEmailTriggerEnabled());
      this.debugModeSelector.setSelectedItem(var1.getDebugMode());
   }

   @Override
   public void decorate(UiCanvas var1) {
      OneColumnPanel var2 = new OneColumnPanel()
         .nonOpaque()
         .withInsets(0, 0, 10, 0)
         .withBorder(this.border)
         .of(this.truncateFieldsChoice, this.triggerEmailsChoice, InvisiblePanel.of(this.debugModeSelector.asUiProvider()));
      var2.setWeightX(1.0);
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.truncateFieldsChoice.requestFocusInWindow();
   }
}
