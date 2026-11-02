package com.etlforge.integration.salesforce.ui.renderer;

import com.etlforge.integration.salesforce.ui.NameDisplayModes;
import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.integration.salesforce.wsdl.SchemaEntity;
import org.etlforge.ui.widget.tree.KongaTreeNode;

public final class SchemaEntityTreeRenderer extends DefaultTreeCellRenderer {
   private final NameDisplayModes nameModes;

   public SchemaEntityTreeRenderer(NameDisplayModes var1) {
      this.nameModes = var1;
   }

   @Override
   public Icon getLeafIcon() {
      return null;
   }

   @Override
   public Component getTreeCellRendererComponent(JTree var1, Object var2, boolean var3, boolean var4, boolean var5, int var6, boolean var7) {
      super.getTreeCellRendererComponent(var1, var2, var3, var4, var5, var6, var7);
      SchemaEntity var8 = null;
      if (var2 instanceof KongaTreeNode) {
         Object var9 = ((KongaTreeNode)var2).getUserObject();
         if (var9 instanceof SchemaEntity) {
            var8 = (SchemaEntity)var9;
         }
      }

      if (var8 instanceof SObjectSchema) {
         SchemaEntityRenderingUtils.decorate(this, var8, this.nameModes.object);
      } else if (var8 instanceof SObjectSchema.Field) {
         SchemaEntityRenderingUtils.decorate(this, var8, this.nameModes.field);
      } else {
         this.setText(var2.toString());
         this.setToolTipText(null);
      }

      return this;
   }
}
