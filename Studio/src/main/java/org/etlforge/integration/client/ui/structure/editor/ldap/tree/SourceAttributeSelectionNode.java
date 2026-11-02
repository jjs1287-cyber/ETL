package org.etlforge.integration.client.ui.structure.editor.ldap.tree;

import java.awt.Color;
import org.etlforge.integration.ldap.structure.AttributeUse;
import org.etlforge.integration.ldap.structure.LdapStructureAttribute;
import org.etlforge.ui.widget.tree.KongaTreeNode;

final class SourceAttributeSelectionNode extends AttributeSelectionNode {
   public SourceAttributeSelectionNode(LdapStructureAttribute var1, KongaTreeNode var2) {
      super(var1, var2);
   }

   @Override
   protected Color getColor() {
      return this.getUserObject().getUse() == AttributeUse.REQUIRED ? REQUIRED : Color.BLACK;
   }
}
