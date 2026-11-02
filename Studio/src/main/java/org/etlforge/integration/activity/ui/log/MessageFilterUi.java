package org.etlforge.integration.activity.ui.log;

import javax.swing.JTextField;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.ui.widget.InputFieldFactory;
import org.etlforge.integration.activity.client.log.MessageFilter;
import org.etlforge.integration.activity.client.log.OperationLogEntry;
import org.etlforge.integration.activity.ui.component.filter.AbstractActivityEntryFilterUi;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.widget.text.KongaTextField;

final class MessageFilterUi extends AbstractActivityEntryFilterUi<OperationLogEntry> {
   private String value;
   private final JTextField inputField = new KongaTextField(20);

   public MessageFilterUi() {
   }

   public MessageFilter getFilter() {
      return new MessageFilter(this.value);
   }

   @Override
   public void apply() {
      this.value = this.inputField.getText();
   }

   @Override
   public void reset() {
      this.inputField.setText(this.value);
   }

   @Override
   public boolean isActive() {
      return StringUtils.isNotEmpty(this.value);
   }

   @Override
   public String getFilterString() {
      return this.isActive() ? PackageResources.MessageFilter.getFilterString(this.value) : "";
   }

   @Override
   public void decorate(UiCanvas var1) {
      String var2 = PackageResources.MessageFilter.LABEL;
      var1.display(InputFieldFactory.getLabeledComponent(this.inputField, var2, this.getCapsuleOrientation()));
   }

   @Override
   public void requestFocus() {
      this.inputField.requestFocusInWindow();
   }
}
