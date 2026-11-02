package org.etlforge.integration.client.ui.structure.editor.ldap.tree;

import java.awt.Color;
import java.awt.Font;
import org.etlforge.ui.util.Fonts;
import org.etlforge.ui.util.decorator.TreeNodeDecor;
import org.etlforge.ui.util.decorator.TreeNodeDecorator;
import org.etlforge.ui.widget.text.TextDecorator;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;

final class UnknownStructureNode extends DefaultKongaTreeNode implements TreeNodeDecorator {
   public UnknownStructureNode(ClassNameNode var1) {
      super(var1, PackageResources.TreeModel.NOT_IN_CACHE, false);
   }

   @Override
   public void decorate(TreeNodeDecor var1) {
      var1.setText(this.getUserObject().toString());
      final Font var2 = Fonts.italic(var1.getTree());
      var1.setTextStyle(new TextDecorator() {
         @Override
         public Font getFont() {
            return var2;
         }

         @Override
         public Color getTextColor() {
            return Color.BLACK;
         }
      });
   }
}
