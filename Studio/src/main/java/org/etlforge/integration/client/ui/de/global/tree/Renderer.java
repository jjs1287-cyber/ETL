package org.etlforge.integration.client.ui.de.global.tree;

import java.awt.Component;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.client.ui.de.resources.DataElementResources;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.ui.widget.tree.NodeStatus;

final class Renderer extends DefaultTreeCellRenderer {
   private IntegrationEntity process;

   public void setProcess(IntegrationEntity var1) {
      this.process = var1;
   }

   @Override
   public Component getTreeCellRendererComponent(JTree var1, Object var2, boolean var3, boolean var4, boolean var5, int var6, boolean var7) {
      super.getTreeCellRendererComponent(var1, var2, var3, var4, var5, var6, var7);
      boolean var8 = var2 instanceof ProjectVariablesNode && this.process != null;
      if (var8) {
         this.setText(this.process.getName());
         this.setIcon(EntityIcons.forType(this.process.getEntityType()).getDefaultIcon(EntityIcons.Size.SMALL));
         this.setToolTipText(DataElementResources.getString("DataElement.Tree.Process.ToolTip"));
      } else {
         this.setIcon(((KongaTreeNode)var2).getNodeIcon(NodeStatus.CLOSED));
         this.setNodeToolTip(var2);
      }

      this.setStyle(var2);
      return this;
   }

   private void setNodeToolTip(Object var1) {
      String var2;
      if (var1 instanceof ProjectVariablesNode) {
         var2 = DataElementResources.getString("DataElement.Tree.Project.ToolTip");
      } else if (var1 instanceof ETLForgeVariablesNode) {
         var2 = DataElementResources.getString("DataElement.Tree.ETLForge.ToolTip");
      } else {
         var2 = var1.toString();
      }

      this.setToolTipText(var2);
   }

   private void setStyle(Object var1) {
      TextStyle var2;
      if (var1 instanceof DataElementNode) {
         var2 = TextStyles.DefaultText;
      } else if (var1 instanceof NoneNode) {
         var2 = TextStyles.DefaultItalic;
      } else {
         var2 = TextStyles.DefaultBold;
      }

      this.setFont(var2.getFont());
   }
}
