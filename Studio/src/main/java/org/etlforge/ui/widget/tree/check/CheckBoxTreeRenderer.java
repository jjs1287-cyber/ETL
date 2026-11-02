package org.etlforge.ui.widget.tree.check;

import com.google.common.base.Preconditions;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import org.etlforge.ui.Empty;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.util.UiUtils;

public final class CheckBoxTreeRenderer implements TreeCellRenderer {
   private static final Color SELECTED_FOREGROUND_FOCUS = Color.WHITE;
   private static final Color SELECTED_FOREGROUND_NOT_FOCUS = Color.BLACK;
   private static final Color SELECTED_BACKGROUND_FOCUS = new Color(49, 106, 197);
   private static final Color SELECTED_BACKGROUND_NOT_FOCUS = new Color(236, 233, 216);
   private final TreeCellRenderer delegate;
   private final CheckBoxTreeRenderer.RendererComponent renderer;

   public CheckBoxTreeRenderer() {
      this(new DefaultTreeCellRenderer());
   }

   public CheckBoxTreeRenderer(TreeCellRenderer var1) {
      Preconditions.checkNotNull(var1, "delegate");
      this.delegate = var1;
      this.renderer = new CheckBoxTreeRenderer.RendererComponent();
   }

   @Override
   public Component getTreeCellRendererComponent(JTree var1, Object var2, boolean var3, boolean var4, boolean var5, int var6, boolean var7) {
      this.renderer.setEnabled(var1.isEnabled());
      Component var8 = this.delegate.getTreeCellRendererComponent(var1, var2, var3, var4, var5, var6, var7);
      this.renderer.decorate(var8);
      if (var2 instanceof CheckBoxNode) {
         this.renderer.setSelected(((CheckBoxNode)var2).isSelected());
         if (this.delegate instanceof DefaultTreeCellRenderer) {
            DefaultTreeCellRenderer var9 = (DefaultTreeCellRenderer)this.delegate;
            this.renderer.setForeground(var9.getForeground());
            this.renderer.setBackground(var9.getBackground());
         } else if (var3) {
            this.decorateSelected(var1);
         } else {
            this.decorateUnselected();
         }

         return this.renderer;
      } else {
         return var8;
      }
   }

   public boolean isInCheckBox(int var1, int var2) {
      return this.renderer.isInCheckBox(var1, var2);
   }

   private void decorateSelected(JTree var1) {
      this.renderer.setOpaque(true);
      boolean var2 = var1.hasFocus();
      this.renderer.setBackground(var2 ? SELECTED_BACKGROUND_FOCUS : SELECTED_BACKGROUND_NOT_FOCUS);
      this.renderer.setForeground(var2 ? SELECTED_FOREGROUND_FOCUS : SELECTED_FOREGROUND_NOT_FOCUS);
   }

   private void decorateUnselected() {
      this.renderer.setOpaque(false);
   }

   private static class RendererComponent extends JPanel {
      private final JCheckBox checkBox;
      private final JLabel label;

      public RendererComponent() {
         this.setOpaque(false);
         this.checkBox = new JCheckBox();
         this.checkBox.setOpaque(false);
         this.label = new JLabel("", 10);
         this.label.setBorder(Empty.border(2, 2, 2, 6));
         BoxBuilder.horizontal(this).addAllWithSpace(5, this.checkBox, this.label);
      }

      public boolean isInCheckBox(int var1, int var2) {
         return this.checkBox.contains(var1, var2);
      }

      public void setSelected(boolean var1) {
         this.checkBox.setSelected(var1);
      }

      public void decorate(Component var1) {
         this.label.setForeground(var1.getForeground());
         this.label.setBackground(var1.getBackground());
         this.label.setFont(var1.getFont());
         if (var1 instanceof JLabel) {
            this.label.setText(((JLabel)var1).getText());
            this.label.setIcon(((JLabel)var1).getIcon());
            this.setToolTipText(((JLabel)var1).getToolTipText());
         } else {
            this.label.setText(null);
            this.label.setIcon(null);
            this.label.setToolTipText(null);
         }

         this.setEnabled(var1.isEnabled());
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
