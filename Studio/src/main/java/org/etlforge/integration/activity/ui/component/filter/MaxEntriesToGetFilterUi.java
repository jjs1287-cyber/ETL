package org.etlforge.integration.activity.ui.component.filter;

import com.google.common.base.Preconditions;
import javax.swing.JComponent;
import org.etlforge.application.ui.widget.InputFieldFactory;
import org.etlforge.integration.activity.client.ActivityEntry;
import org.etlforge.integration.activity.client.filter.MaxEntriesToGetFilter;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.input.IntegerInputField;

public final class MaxEntriesToGetFilterUi<T extends ActivityEntry> extends AbstractActivityEntryFilterUi<T> {
   private int value;
   private final IntegerInputField inputField;

   public MaxEntriesToGetFilterUi(int var1) {
      Preconditions.checkArgument(var1 >= 0, "initialValue must be >= 0 (value was " + var1 + ").");
      this.value = var1;
      String var2 = PackageResources.MaxEntriesFilter.LABEL;
      this.inputField = InputFieldFactory.getIntegerInputField(var2, var2, 1, 10000, 8);
      this.inputField.setValue(var1);
   }

   public MaxEntriesToGetFilter getFilter() {
      return new MaxEntriesToGetFilter(this.value);
   }

   @Override
   public String getFilterString() {
      return PackageResources.MaxEntriesFilter.getFilterString(this.value);
   }

   @Override
   public void apply() {
      this.value = this.inputField.getIntValue(-1);
   }

   @Override
   public void reset() {
      if (this.value < 0) {
         this.inputField.setValue("");
      } else {
         this.inputField.setValue(this.value);
      }
   }

   @Override
   public boolean isActive() {
      return this.value >= 0;
   }

   @Override
   public void decorate(UiCanvas var1) {
      JComponent var2 = InputFieldFactory.getCapsule(this.inputField, this.getCapsuleOrientation());
      var1.display(var2);
   }

   @Override
   public void requestFocus() {
      this.inputField.requestFocus();
   }
}
