package org.etlforge.ui.widget.tree.check;

import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTreeNode;

public class DefaultCheckBoxNode extends DefaultKongaTreeNode implements CheckBoxNode {
   private boolean selected;
   private boolean enabled = true;

   public DefaultCheckBoxNode(KongaTreeNode var1, Object var2, boolean var3) {
      super(var1, var2, var3);
   }

   public DefaultCheckBoxNode(Object var1, boolean var2) {
      super(var1, var2);
   }

   @Override
   public final boolean isSelected() {
      return this.selected;
   }

   @Override
   public final void setSelected(boolean var1) {
      if (this.enabled) {
         this.selected = var1;
      }
   }

   @Override
   public final void toggle() {
      if (this.enabled) {
         this.selected = !this.selected;
      }
   }

   public final void setEnabled(boolean var1) {
      this.enabled = var1;
   }

   public final boolean isEnabled() {
      return this.enabled;
   }
}
