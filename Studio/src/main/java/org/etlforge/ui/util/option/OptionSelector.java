package org.etlforge.ui.util.option;

public interface OptionSelector<T extends Option> {
   T getOption();

   void setText(String var1);

   void setDescription(String var1);

   void setEnabled(boolean var1);

   boolean isSelected();

   void setSelected(boolean var1);
}
