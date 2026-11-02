package org.etlforge.ui.widget.tree;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.Icon;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.resources.UiResourceBundle;

public final class ActionNode extends DefaultKongaTreeNode {
   private String name;
   private boolean renderItalic;
   private boolean useIcon = true;

   public ActionNode(KongaTreeNode var1, Action var2) {
      super(var1, var2, false);
      MnemonicString var3 = UiResourceBundle.MNEMONIC_FACTORY.fromString((String)var2.getValue("Name"));
      this.name = var3.getText();
   }

   public Action getAction() {
      return this.getUserObject();
   }

   public Action getUserObject() {
      return (Action)super.getUserObject();
   }

   @Override
   public String getDisplayName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   @Override
   public Icon getNodeIcon(NodeStatus var1) {
      if (!this.useIcon) {
         return null;
      }

      Action var2 = this.getAction();
      return (Icon)var2.getValue("SmallIcon");
   }

   public void triggerActionIfEnabled(ActionEvent var1) {
      Action var2 = this.getAction();
      if (var2.isEnabled()) {
         var2.actionPerformed(var1);
      }
   }

   public String getToolTip() {
      Action var1 = this.getAction();
      return (String)var1.getValue("ShortDescription");
   }

   public void setUseIcon(boolean var1) {
      this.useIcon = var1;
   }

   public boolean isRenderItalic() {
      return this.renderItalic;
   }

   public void setRenderItalic(boolean var1) {
      this.renderItalic = var1;
   }
}
