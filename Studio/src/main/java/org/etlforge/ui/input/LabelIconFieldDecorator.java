package org.etlforge.ui.input;

import javax.swing.Icon;
import org.etlforge.ui.Empty;
import org.etlforge.ui.util.ImageLoader;

public class LabelIconFieldDecorator implements InputFieldDecorator {
   private static final Icon DEFAULT_VALID_FIELD_ICON = ImageLoader.getImageIcon("org.etlforge.ui.input", "valid_field.gif");
   private static final Icon DEFAULT_INVALID_FIELD_ICON = ImageLoader.getImageIcon("org.etlforge.ui.input", "invalid_field.gif");
   private Icon validFieldIcon = DEFAULT_VALID_FIELD_ICON;
   private Icon invalidFieldIcon = DEFAULT_INVALID_FIELD_ICON;
   private int horizontalTextPosition = 2;
   private int verticalTextPosition = 0;
   private boolean active = true;
   private boolean decorateValidState = true;

   public LabelIconFieldDecorator() {
   }

   public LabelIconFieldDecorator(Icon var1, Icon var2) {
      this.validFieldIcon = var1;
      this.invalidFieldIcon = var2;
   }

   public void setHorizontalTextPosition(int var1) {
      this.horizontalTextPosition = var1;
   }

   public void setDecorateValidState(boolean var1) {
      this.decorateValidState = var1;
   }

   public void setVerticalTextPosition(int var1) {
      this.verticalTextPosition = var1;
   }

   @Override
   public void setActive(boolean var1) {
      this.active = var1;
   }

   @Override
   public boolean isActive() {
      return this.active;
   }

   @Override
   public void decorateField(InputField var1, boolean var2) {
      if (var1.getLabel() != null) {
         if (this.active) {
            Icon var3 = this.getIcon(var2);
            if (var3 != null) {
               var1.getLabel().setHorizontalTextPosition(this.horizontalTextPosition);
               var1.getLabel().setVerticalTextPosition(this.verticalTextPosition);
            }

            var1.getLabel().setIcon(var3);
         } else {
            var1.getLabel().setIcon(null);
         }
      }
   }

   private Icon getIcon(boolean var1) {
      if (var1) {
         return this.decorateValidState ? this.validFieldIcon : Empty.iconOfSameSize(this.validFieldIcon);
      } else {
         return this.invalidFieldIcon;
      }
   }
}
