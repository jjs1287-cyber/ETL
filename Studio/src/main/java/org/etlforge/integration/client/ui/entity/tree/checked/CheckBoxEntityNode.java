package org.etlforge.integration.client.ui.entity.tree.checked;

import javax.swing.Icon;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.uidelegate.entity.IconStore;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.NodeStatus;
import org.etlforge.ui.widget.tree.check.CheckBoxNode;

public class CheckBoxEntityNode extends DefaultKongaTreeNode implements CheckBoxNode {
   private boolean selected;
   private boolean valid = true;

   public CheckBoxEntityNode(CheckBoxEntityNode var1, Object var2, boolean var3) {
      super(var1, var2, var3);
   }

   @Override
   public boolean isSelected() {
      return this.selected;
   }

   @Override
   public void setSelected(boolean var1) {
      this.selected = var1;
   }

   @Override
   public void toggle() {
      this.selected = !this.selected;
   }

   public boolean isValid() {
      return this.valid;
   }

   public void setValid(boolean var1) {
      this.valid = var1;
   }

   boolean allChildrenAreValid() {
      for (int var1 = 0; var1 < this.getChildCount(); var1++) {
         CheckBoxEntityNode var2 = (CheckBoxEntityNode)this.getChildAt(var1);
         if (!var2.isValid() || !var2.allChildrenAreValid()) {
            return false;
         }
      }

      return true;
   }

   @Override
   public Icon getNodeIcon(NodeStatus var1) {
      Icon var2 = null;
      Object var3 = this.getUserObject();
      if (var3 instanceof IntegrationEntity) {
         var2 = IconStore.validationDecoratedIcon((IntegrationEntity)var3);
      }

      return var2;
   }

   @Override
   public String getDisplayName() {
      String var1 = null;
      Object var2 = this.getUserObject();
      if (var2 instanceof IntegrationEntity) {
         var1 = ((IntegrationEntity)var2).getName();
      } else {
         var1 = var2 == null ? "" : var2.toString();
      }

      return var1;
   }
}
