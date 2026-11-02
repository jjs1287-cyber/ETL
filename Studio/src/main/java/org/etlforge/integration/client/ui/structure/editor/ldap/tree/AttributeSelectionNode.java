package org.etlforge.integration.client.ui.structure.editor.ldap.tree;

import java.awt.Color;
import java.awt.Font;
import javax.swing.Icon;
import org.etlforge.integration.client.ldap.ui.AttributeToolTip;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.ldap.structure.AttributeCardinality;
import org.etlforge.integration.ldap.structure.AttributeUse;
import org.etlforge.integration.ldap.structure.LdapStructureAttribute;
import org.etlforge.ui.util.decorator.TreeNodeDecor;
import org.etlforge.ui.util.decorator.TreeNodeDecorator;
import org.etlforge.ui.widget.text.TextDecorator;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.ui.widget.tree.NodeStatus;
import org.etlforge.ui.widget.tree.check.DefaultCheckBoxNode;

abstract class AttributeSelectionNode extends DefaultCheckBoxNode implements TreeNodeDecorator {
   static final Color REQUIRED = new Color(205, 0, 0);
   static final String MANY_VALUED = "*";
   private final String displayName;
   private final String tooltip;
   private final TextDecorator textStyle;

   protected AttributeSelectionNode(LdapStructureAttribute var1, KongaTreeNode var2) {
      super(var2, var1, false);
      this.displayName = this.createDisplayName(var1);
      this.tooltip = new AttributeToolTip(var1).get();
      final Color var3 = this.getColor();
      this.textStyle = new TextDecorator() {
         @Override
         public Font getFont() {
            return null;
         }

         @Override
         public Color getTextColor() {
            return var3;
         }
      };
      this.setSelected(false);
   }

   protected abstract Color getColor();

   public static AttributeSelectionNode create(LdapStructureAttribute var0, SourceTarget var1, ClassNameNode var2) {
      return var1 == SourceTarget.Source ? new SourceAttributeSelectionNode(var0, var2) : new TargetAttributeSelectionNode(var0, var2);
   }

   public ClassNameNode getParent() {
      return (ClassNameNode)super.getParent();
   }

   @Override
   public void decorate(TreeNodeDecor var1) {
      var1.setText(this.displayName);
      var1.setToolTip(this.tooltip);
      var1.setTextStyle(this.textStyle);
      var1.setIcon(null);
   }

   public boolean isRequired() {
      return this.getUserObject().getUse() == AttributeUse.REQUIRED;
   }

   public LdapStructureAttribute getUserObject() {
      return (LdapStructureAttribute)super.getUserObject();
   }

   @Override
   public String getDisplayName() {
      return this.displayName;
   }

   @Override
   public Icon getNodeIcon(NodeStatus var1) {
      return null;
   }

   private String createDisplayName(LdapStructureAttribute var1) {
      String var2 = this.getUserObject().getName();
      if (var1.getCardinality() == AttributeCardinality.MANY) {
         var2 = var2 + " *";
      }

      return "[ A ] " + var2;
   }
}
