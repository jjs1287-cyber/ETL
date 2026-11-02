package org.etlforge.integration.client.ui.structure.editor.ldap.tree;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Icon;
import org.etlforge.integration.client.ldap.ui.ClassDefinitionToolTip;
import org.etlforge.integration.ldap.structure.LdapUtils;
import org.etlforge.integration.ldap.structure.NumericOid;
import org.etlforge.integration.ldap.structure.ObjectClassDefinition;
import org.etlforge.ui.util.decorator.TreeNodeDecor;
import org.etlforge.ui.util.decorator.TreeNodeDecorator;
import org.etlforge.ui.widget.text.TextDecorator;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.ui.widget.tree.NodeStatus;

final class ClassNameNode extends DefaultKongaTreeNode implements TreeNodeDecorator {
   private final String displayBase;
   private final String tooltip;

   public ClassNameNode(ObjectClassDefinition var1, KongaTreeNode var2) {
      super(var2, var1, true);
      this.displayBase = PackageResources.TreeModel.CLASS_NODE_PREFIX + LdapUtils.getRepresentation(var1);
      this.tooltip = new ClassDefinitionToolTip(var1).get();
   }

   public NumericOid getOid() {
      return this.getUserObject().getOid();
   }

   public ObjectClassDefinition getUserObject() {
      return (ObjectClassDefinition)super.getUserObject();
   }

   @Override
   public String getDisplayName() {
      return this.displayBase + this.getSelectionCountString();
   }

   @Override
   public void decorate(TreeNodeDecor var1) {
      var1.setText(this.getDisplayName());
      var1.setTextStyle(new TextDecorator() {
         @Override
         public Font getFont() {
            return null;
         }

         @Override
         public Color getTextColor() {
            return Color.BLUE;
         }
      });
      var1.setIcon(PackageResources.TreeModel.ENTRY_ICON);
      var1.setToolTip(this.tooltip);
   }

   @Override
   public Icon getNodeIcon(NodeStatus var1) {
      return PackageResources.TreeModel.ENTRY_ICON;
   }

   @Override
   public String toString() {
      return this.displayBase;
   }

   public AttributeSelectionNode[] getRequiredAttributes() {
      ArrayList var1 = Lists.newArrayList();
      int var2 = 0;

      for (int var3 = this.getChildCount(); var2 < var3; var2++) {
         KongaTreeNode var4 = this.getChildAt(var2);
         if (var4 instanceof AttributeSelectionNode) {
            AttributeSelectionNode var5 = (AttributeSelectionNode)var4;
            if (var5.isRequired()) {
               var1.add(var5);
            }
         }
      }

      return (AttributeSelectionNode[]) var1.toArray(new AttributeSelectionNode[var1.size()]);
   }

   public Map<NumericOid, AttributeSelectionNode> getAttributeNodeMap() {
      HashMap var1 = Maps.newHashMap();

      for (KongaTreeNode var3 : this.children()) {
         if (var3 instanceof AttributeSelectionNode) {
            AttributeSelectionNode var4 = (AttributeSelectionNode)var3;
            var1.put(var4.getUserObject().getOid(), var4);
         }
      }

      return var1;
   }

   public Iterable<AttributeSelectionNode> getAttributeNodes() {
      ArrayList var1 = Lists.newArrayList();

      for (KongaTreeNode var3 : this.children()) {
         if (var3 instanceof AttributeSelectionNode) {
            var1.add((AttributeSelectionNode)var3);
         }
      }

      return var1;
   }

   private String getSelectionCountString() {
      int var1 = 0;
      int var2 = 0;
      int var3 = 0;

      for (int var4 = this.getChildCount(); var3 < var4; var3++) {
         KongaTreeNode var5 = this.getChildAt(var3);
         if (var5 instanceof AttributeSelectionNode) {
            var1++;
            if (((AttributeSelectionNode)var5).isSelected()) {
               var2++;
            }
         }
      }

      return var1 == 0 ? "" : " (" + var2 + "/" + var1 + ")     ";
   }
}
