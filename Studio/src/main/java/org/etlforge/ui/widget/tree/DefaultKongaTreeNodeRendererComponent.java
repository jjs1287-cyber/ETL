package org.etlforge.ui.widget.tree;

import java.awt.Color;
import java.awt.Font;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.apache.commons.lang.StringUtils;
import org.etlforge.ui.Empty;

public class DefaultKongaTreeNodeRendererComponent extends JPanel implements KongaTreeNodeRendererComponent {
   private static final int ICON_TEXT_GAP = 4;
   private final JLabel iconLabel;
   private final JLabel textLabel;

   public DefaultKongaTreeNodeRendererComponent(KongaTree var1, KongaTreeNode var2, NodeStatus var3) {
      this.setLayout(new BoxLayout(this, 2));
      super.setOpaque(false);
      Icon var4 = var2.getNodeIcon(var3);
      this.iconLabel = var4 != null ? this.createAndAddIconLabel(var4) : null;
      this.textLabel = this.createAndAddTextLabel(var1, var2, var4);
   }

   private JLabel createAndAddIconLabel(Icon var1) {
      JLabel var2 = new JLabel(var1);
      var2.setOpaque(false);
      this.add(var2);
      return var2;
   }

   private JLabel createAndAddTextLabel(KongaTree var1, KongaTreeNode var2, Icon var3) {
      String var4 = var2.getDisplayName();
      if (StringUtils.isNotBlank(var4)) {
         if (var3 != null) {
            this.add(Box.createHorizontalStrut(4));
         }

         JLabel var5 = this.createTextLabel(var1, var2);
         this.add(var5);
         return var5;
      } else {
         return null;
      }
   }

   @Override
   public void setOpaque(boolean var1) {
      if (this.textLabel != null) {
         this.textLabel.setOpaque(var1);
      }
   }

   @Override
   public void setForeground(Color var1) {
      if (this.textLabel != null) {
         this.textLabel.setForeground(var1);
      }
   }

   @Override
   public void setBackground(Color var1) {
      if (this.textLabel != null) {
         this.textLabel.setBackground(var1);
      }

      if (this.iconLabel != null) {
         this.iconLabel.setBackground(var1);
      }
   }

   @Override
   public Font getFont() {
      return this.textLabel != null ? this.textLabel.getFont() : super.getFont();
   }

   @Override
   public void setFont(Font var1) {
      if (this.textLabel != null) {
         this.textLabel.setFont(var1);
      }
   }

   private JLabel createTextLabel(KongaTree var1, KongaTreeNode var2) {
      JLabel var3 = new JLabel(this.getNodeText(var1, var2));
      var3.setOpaque(false);
      var3.setBorder(Empty.border(2, 2, 2, 6));
      return var3;
   }

   protected String getNodeText(KongaTree var1, KongaTreeNode var2) {
      return var2.getDisplayName();
   }

   @Override
   public JComponent getComponent() {
      return this;
   }
}
