package com.etlforge.integration.client.ui.sfdc.bulkload.mapping;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.etlforge.integration.salesforce.wsdl.RelatedObjectExternalIdFieldName;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.ui.widget.tree.KongaTreeNode;

final class TargetFieldRenderer extends DefaultTreeCellRenderer {
   @Override
   public Component getTreeCellRendererComponent(JTree var1, Object var2, boolean var3, boolean var4, boolean var5, int var6, boolean var7) {
      super.getTreeCellRendererComponent(var1, var2, var3, var4, var5, var6, var7);
      KongaTreeNode var8 = (KongaTreeNode)var2;
      Object var9 = var8.getUserObject();
      if (var9 instanceof SObjectSchema.Field) {
         String var10 = this.getFieldName((SObjectSchema.Field)var9);
         this.setText(var10);
      }

      return this;
   }

   private String getFieldName(SObjectSchema.Field var1) {
      return RelatedObjectExternalIdFieldName.isRelationshipField(var1) ? RelatedObjectExternalIdFieldName.displayName(var1) : var1.getName();
   }

   @Override
   public Icon getLeafIcon() {
      return null;
   }
}
