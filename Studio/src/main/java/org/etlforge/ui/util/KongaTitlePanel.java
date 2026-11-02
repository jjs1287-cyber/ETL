package org.etlforge.ui.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.etlforge.ui.UiLook;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.util.focus.FocusUtils;

public class KongaTitlePanel extends JPanel implements FocusListener {
   private JLabel titleLabel;
   private int userDefinedHeight = -1;
   private int heightToUse = -1;

   public KongaTitlePanel(String var1) {
      this(var1, -1);
   }

   public KongaTitlePanel(String var1, int var2) {
      this.titleLabel = new JLabel(var1);
      this.titleLabel.setFont(this.titleLabel.getFont());
      this.userDefinedHeight = var2;
      this.setBorder(new BorderBuilder().line(new Color(110, 110, 110)).empty(5));
      this.setBackground(UiLook.getTitlePanelBackground(false));
      this.titleLabel.setForeground(UiLook.getTitlePanelForeground(false));
      BoxBuilder.horizontal(this).add(this.titleLabel).glue();
      FocusUtils.makeComponentMouseFocusable(this);
   }

   public void setTitleFont(Font var1) {
      this.titleLabel.setFont(var1);
   }

   public void setTitleFont(int var1, int var2) {
      this.titleLabel.setFont(UiLook.getLabelFont(var1, var2));
   }

   public void setTitle(String var1) {
      this.titleLabel.setText(var1);
   }

   public void setIcon(Icon var1) {
      this.titleLabel.setIcon(var1);
   }

   @Override
   public Dimension getMaximumSize() {
      Dimension var1 = null;
      if (this.userDefinedHeight <= 0) {
         var1 = super.getMaximumSize();
      } else {
         Dimension var2 = super.getMaximumSize();
         var1 = new Dimension(var2.width, this.getHeightToUse());
      }

      return var1;
   }

   @Override
   public Dimension getMinimumSize() {
      Dimension var1 = null;
      if (this.userDefinedHeight <= 0) {
         var1 = super.getMinimumSize();
      } else {
         Dimension var2 = super.getMinimumSize();
         var1 = new Dimension(var2.width, this.getHeightToUse());
      }

      return var1;
   }

   @Override
   public Dimension getPreferredSize() {
      Dimension var1 = null;
      if (this.userDefinedHeight <= 0) {
         var1 = super.getPreferredSize();
      } else {
         Dimension var2 = super.getPreferredSize();
         var1 = new Dimension(var2.width, this.getHeightToUse());
      }

      return var1;
   }

   private int getHeightToUse() {
      if (this.heightToUse == -1 && this.userDefinedHeight > 0) {
         int var1 = this.titleLabel.getPreferredSize().height + 10;
         this.heightToUse = this.userDefinedHeight > var1 ? this.userDefinedHeight : var1;
      }

      return this.heightToUse;
   }

   @Override
   public void focusGained(FocusEvent var1) {
      this.setBackground(UiLook.getTitlePanelBackground(true));
      this.titleLabel.setForeground(UiLook.getTitlePanelForeground(true));
   }

   @Override
   public void focusLost(FocusEvent var1) {
      this.setBackground(UiLook.getTitlePanelBackground(false));
      this.titleLabel.setForeground(UiLook.getTitlePanelForeground(false));
   }
}
