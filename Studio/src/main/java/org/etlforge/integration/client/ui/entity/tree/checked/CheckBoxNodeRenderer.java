package org.etlforge.integration.client.ui.entity.tree.checked;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.Empty;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.button.KongaCheckBox;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.ui.widget.tree.NodeStatus;
import org.etlforge.ui.widget.tree.check.CheckBoxNode;

public class CheckBoxNodeRenderer extends DefaultTreeCellRenderer {
   private final CheckBoxNodeRenderer.RendererComponent renderer = new CheckBoxNodeRenderer.RendererComponent();

   @Override
   public Component getTreeCellRendererComponent(JTree var1, Object var2, boolean var3, boolean var4, boolean var5, int var6, boolean var7) {
      super.getTreeCellRendererComponent(var1, var2, var3, var4, var5, var6, var7);
      if (var2 instanceof CheckBoxNode) {
         this.renderer.checkBox.setSelected(((CheckBoxNode)var2).isSelected());
      } else {
         this.renderer.checkBox.setSelected(false);
      }

      this.renderer.setText(this.getText(var2));
      this.renderer.setIcon(this.getIcon(var2));
      this.applyStyleSheet(var2);
      this.renderer.setBackground(this.getBackground());
      this.renderer.setForeground(this.getForeground());
      this.renderer.setEnabled(var1.isEnabled());
      return this.renderer;
   }

   protected String getText(Object var1) {
      if (var1 instanceof CheckBoxNode) {
         return ((CheckBoxNode)var1).getDisplayName();
      } else {
         return var1 == null ? "" : var1.toString();
      }
   }

   protected Icon getIcon(Object var1) {
      return var1 instanceof KongaTreeNode ? ((KongaTreeNode)var1).getNodeIcon(NodeStatus.CLOSED) : null;
   }

   protected void applyStyleSheet(Object var1) {
      Font var2 = null;
      if (var1 instanceof KongaTreeNode && this.needsRootFolderStyle((KongaTreeNode)var1)) {
         var2 = TextStyles.TreeRootFolderText.getFont();
      }

      if (var2 == null) {
         var2 = TextStyles.TreeLeafText.getFont();
      }

      this.renderer.label.setFont(var2);
   }

   private boolean needsRootFolderStyle(KongaTreeNode var1) {
      Object var2 = var1.getUserObject();
      return var2 instanceof RootFolder || var2 instanceof IntegrationProject || var1.getLevel() == 1;
   }

   public boolean isInCheckBox(int var1, int var2) {
      return this.renderer.checkBox.contains(var1, var2);
   }

   private static final class RendererComponent extends JPanel {
      private final JCheckBox checkBox;
      private final JLabel label;

      private RendererComponent() {
         this.setOpaque(false);
         this.checkBox = new KongaCheckBox();
         this.checkBox.setOpaque(false);
         this.label = new JLabel("", 10);
         TextStyles.TreeLeafText.makeOver(this.label);
         this.label.setBorder(Empty.border(2, 2, 2, 6));
         BoxBuilder.horizontal(this).addAllWithSpace(5, this.checkBox, this.label);
      }

      private void setText(String var1) {
         this.label.setText(var1);
      }

      private void setIcon(Icon var1) {
         this.label.setIcon(var1);
      }

      @Override
      public void setOpaque(boolean var1) {
         if (this.label != null) {
            this.label.setOpaque(var1);
         }
      }

      @Override
      public void setForeground(Color var1) {
         if (this.label != null) {
            this.label.setForeground(var1);
         }
      }

      @Override
      public void setBackground(Color var1) {
         if (this.label != null) {
            this.label.setBackground(var1);
         }
      }

      @Override
      public void setEnabled(boolean var1) {
         UiUtils.setEnabled(var1, this.label, this.checkBox);
      }
   }
}
