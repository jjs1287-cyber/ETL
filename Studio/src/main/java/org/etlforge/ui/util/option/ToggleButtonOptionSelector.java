package org.etlforge.ui.util.option;

import javax.swing.JToggleButton;
import org.apache.commons.lang.StringUtils;

public final class ToggleButtonOptionSelector<T extends Option> implements OptionSelector<T> {
   private final T option;
   private final JToggleButton button;

   public ToggleButtonOptionSelector(T var1, JToggleButton var2) {
      this.option = (T)var1;
      this.button = var2;
      var1.decorate(this);
   }

   @Override
   public T getOption() {
      return this.option;
   }

   @Override
   public boolean isSelected() {
      return this.button.isSelected();
   }

   @Override
   public void setEnabled(boolean var1) {
      this.button.setEnabled(var1);
   }

   @Override
   public void setSelected(boolean var1) {
      this.button.setSelected(var1);
   }

   @Override
   public void setText(String var1) {
      this.button.setText(var1);
   }

   @Override
   public void setDescription(String var1) {
      this.button.setToolTipText(StringUtils.isEmpty(var1) ? null : var1);
   }
}
