package org.etlforge.integration.activity.ui.component.filter;

import javax.swing.JTextField;
import org.etlforge.application.ui.widget.InputFieldFactory;
import org.etlforge.integration.activity.client.ActivityEntry;
import org.etlforge.integration.activity.client.filter.OperationNameFilter;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.widget.text.KongaTextField;

public final class OperationNameFilterUi<T extends ActivityEntry> extends AbstractActivityEntryFilterUi<T> {
   private final JTextField inputField = new KongaTextField(20);
   private OperationNameFilter filter;
   private String value = "";

   @Override
   public void apply() {
      this.value = this.inputField.getText().trim();
      this.filter = new OperationNameFilter(this.value);
   }

   public OperationNameFilter getFilter() {
      if (this.filter == null) {
         this.apply();
      }

      return this.filter;
   }

   @Override
   public String getFilterString() {
      return this.value.length() > 0 ? PackageResources.NameFilter.getFilterString(this.value) : "";
   }

   @Override
   public boolean isActive() {
      return this.inputField.getDocument().getLength() > 0;
   }

   @Override
   public void reset() {
      this.inputField.setText(this.value);
      this.filter = new OperationNameFilter(this.value);
   }

   @Override
   public void decorate(UiCanvas var1) {
      String var2 = PackageResources.NameFilter.LABEL;
      var1.display(InputFieldFactory.getLabeledComponent(this.inputField, var2, this.getCapsuleOrientation()));
   }

   @Override
   public void requestFocus() {
      this.inputField.requestFocus();
   }
}
