package org.etlforge.ui.widget.text;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.LayoutManager;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import org.etlforge.system.ArgCheck;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.widget.Label;

@Deprecated
public class IconTextField implements UiProvider, Label {
   private final JTextField textField;
   private final JLabel label;
   private final JPanel ui;
   private int iconGap = 3;

   public IconTextField(JTextField var1, Icon var2) {
      if (var1 == null) {
         throw new NullPointerException("textField");
      }

      this.textField = var1;
      this.label = new JLabel(var2);
      this.ui = this.createUi(var1);
      this.prepareTextField(var1);
      this.layoutUi();
   }

   @Override
   public void setIcon(Icon var1) {
      this.label.setIcon(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.ui);
   }

   @Override
   public void requestFocus() {
      this.textField.requestFocusInWindow();
   }

   @Override
   public UiProvider asUiProvider() {
      return this;
   }

   @Override
   public void setBackgroundColor(Color var1) {
      this.ui.setBackground(var1);
   }

   @Override
   public void setFont(Font var1) {
      this.textField.setFont(var1);
   }

   @Override
   public void setText(String var1) {
      this.textField.setText(var1);
      this.textField.setCaretPosition(0);
   }

   @Override
   public void setText(MnemonicString var1) {
      this.setText(var1.getText());
   }

   @Override
   public void setTextColor(Color var1) {
      this.textField.setForeground(var1);
   }

   @Override
   public void setToolTip(String var1) {
      this.textField.setToolTipText(var1);
   }

   @Override
   public void setBorder(Border var1) {
   }

   public void setIconGap(int var1) {
      ArgCheck.checkNotNegative(var1, "gap");
      this.iconGap = var1;
   }

   private JPanel createUi(JTextField var1) {
      JPanel var2 = new JPanel(new IconTextField.Layout()) {
         @Override
         public void setOpaque(boolean var1) {
            super.setOpaque(true);
         }
      };
      var2.setBorder(var1.getBorder());
      var2.setBackground(var1.getBackground());
      return var2;
   }

   private void prepareTextField(JTextField var1) {
      var1.setBorder(null);
      var1.setOpaque(false);
   }

   private void layoutUi() {
      this.ui.add(this.label);
      this.ui.add(this.textField);
   }

   private class Layout implements LayoutManager {
      private Layout() {
      }

      @Override
      public void addLayoutComponent(String var1, Component var2) {
      }

      @Override
      public void removeLayoutComponent(Component var1) {
      }

      @Override
      public void layoutContainer(Container var1) {
         synchronized (var1.getTreeLock()) {
            Insets var3 = var1.getInsets();
            int var4 = var1.getWidth() - var3.left - var3.right;
            int var5 = var3.left;
            int var6 = var3.top;
            Icon var7 = IconTextField.this.label.getIcon();
            if (var7 != null) {
               int var8 = var7.getIconWidth();
               IconTextField.this.label.setBounds(var5, var6, var7.getIconHeight(), var8);
               var5 += var8 + IconTextField.this.iconGap;
               var4 -= var8 + IconTextField.this.iconGap;
               IconTextField.this.textField.setBounds(var5, var6, var4, IconTextField.this.textField.getPreferredSize().height);
            } else {
               IconTextField.this.label.setBounds(var5, var6, 0, 0);
               IconTextField.this.textField.setBounds(var5, var6, var4, IconTextField.this.textField.getPreferredSize().height);
            }
         }
      }

      @Override
      public Dimension minimumLayoutSize(Container var1) {
         synchronized (var1.getTreeLock()) {
            Dimension var3 = this.getIconSize();
            Dimension var4 = IconTextField.this.textField.getMinimumSize();
            Insets var5 = var1.getInsets();
            int var6 = var3.width + var4.width + var5.left + var5.right;
            int var7 = Math.max(var3.height, var4.height) + var5.top + var5.bottom;
            return new Dimension(var6, var7);
         }
      }

      @Override
      public Dimension preferredLayoutSize(Container var1) {
         synchronized (var1.getTreeLock()) {
            Dimension var3 = this.getIconSize();
            Dimension var4 = IconTextField.this.textField.getPreferredSize();
            Insets var5 = var1.getInsets();
            int var6 = var4.width + var5.left + var5.right;
            int var7 = Math.max(var3.height, var4.height) + var5.top + var5.bottom;
            return new Dimension(var6, var7);
         }
      }

      private Dimension getIconSize() {
         Icon var1 = IconTextField.this.label.getIcon();
         int var2 = var1 != null ? var1.getIconWidth() : 0;
         int var3 = var1 != null ? var1.getIconHeight() : 0;
         return new Dimension(var2, var3);
      }
   }
}
