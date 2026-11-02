package org.etlforge.integration.client.ldap.ui;

import java.awt.Color;
import java.awt.Font;
import org.etlforge.integration.ldap.structure.LdapStructureAttribute;
import org.etlforge.integration.ldap.structure.LdapUtils;
import org.etlforge.ui.util.decorator.TreeNodeDecor;
import org.etlforge.ui.util.decorator.TreeNodeDecorator;
import org.etlforge.ui.widget.text.TextDecorator;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTreeNode;

public final class AttributeNode extends DefaultKongaTreeNode implements TreeNodeDecorator {
   private final String display;
   private final String tooltip;
   private TextDecorator textStyle;
   private boolean enabled;

   private AttributeNode(KongaTreeNode var1, LdapStructureAttribute var2) {
      super(var1, var2, false);
      this.display = "[ A ] " + LdapUtils.getRepresentation(var2);
      this.tooltip = new AttributeToolTip(var2).get();
      this.enabled = true;
   }

   public static AttributeNode create(KongaTreeNode var0, LdapStructureAttribute var1) {
      return new AttributeNode(var0, var1);
   }

   public LdapStructureAttribute getUserObject() {
      return (LdapStructureAttribute)super.getUserObject();
   }

   @Override
   public void decorate(TreeNodeDecor var1) {
      var1.setText(this.display);
      var1.setToolTip(this.tooltip);
      if (this.textStyle == null) {
         this.textStyle = getDefaultStyle(var1);
      }

      var1.setTextStyle(this.textStyle);
      var1.setIcon(null);
      var1.setEnabled(this.enabled);
   }

   @Override
   public String getDisplayName() {
      return this.display;
   }

   public void setTextStyle(TextDecorator var1) {
      this.textStyle = var1;
   }

   public void setEnabled(boolean var1) {
      this.enabled = var1;
   }

   private static TextDecorator getDefaultStyle(final TreeNodeDecor var0) {
      return new TextDecorator() {
         @Override
         public Font getFont() {
            return var0.getTree().getFont();
         }

         @Override
         public Color getTextColor() {
            return Color.BLACK;
         }
      };
   }
}
