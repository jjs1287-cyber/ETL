package org.etlforge.integration.client.ui.structure.editor.ldap.tree;

import java.awt.Color;
import org.etlforge.integration.ldap.structure.AttributeUse;
import org.etlforge.integration.ldap.structure.LdapStructureAttribute;
import org.etlforge.ui.util.decorator.TreeNodeDecor;
import org.etlforge.ui.widget.tree.KongaTreeNode;

public final class TargetAttributeSelectionNode extends AttributeSelectionNode {
   public TargetAttributeSelectionNode(LdapStructureAttribute var1, KongaTreeNode var2) {
      super(var1, var2);
      this.setEnabled(!var1.isReadOnly());
   }

   @Override
   protected Color getColor() {
      return this.getUserObject().getUse() == AttributeUse.REQUIRED ? REQUIRED : Color.BLACK;
   }

   @Override
   public void decorate(TreeNodeDecor var1) {
      super.decorate(var1);
      var1.setEnabled(this.isEnabled());
   }
}
