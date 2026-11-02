package org.etlforge.ui.widget.text;

import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPasswordField;
import javax.swing.text.Document;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.widget.ComponentPainter;
import org.etlforge.ui.widget.KongaWidget;

public class KongaPasswordField extends JPasswordField implements KongaTextComponentWithIconSupport, KongaWidget {
   private final KongaTextComponentSupport support;
   private ComponentPainter painter;
   private TextOverlay overlay;
   private char lastEchoChar = this.getEchoChar();
   private boolean showPassword;

   public KongaPasswordField() {
      this.support = new KongaTextComponentSupport(this);
   }

   public KongaPasswordField(String var1) {
      super(var1);
      this.support = new KongaTextComponentSupport(this);
   }

   public KongaPasswordField(int var1) {
      super(var1);
      this.support = new KongaTextComponentSupport(this);
   }

   public KongaPasswordField(String var1, int var2) {
      super(var1, var2);
      this.support = new KongaTextComponentSupport(this);
   }

   public KongaPasswordField(Document var1, String var2, int var3) {
      super(var1, var2, var3);
      this.support = new KongaTextComponentSupport(this);
   }

   public void setShowPassword(boolean var1) {
      if (var1) {
         this.setEchoChar('\u0000');
      } else {
         this.setEchoChar(this.lastEchoChar);
      }

      this.putClientProperty("JPasswordField.cutCopyAllowed", var1);
      this.showPassword = var1;
   }

   public boolean isShowPassword() {
      return this.showPassword;
   }

   @Override
   public void setEchoChar(char var1) {
      if (var1 != 0) {
         this.lastEchoChar = var1;
      }

      super.setEchoChar(var1);
   }

   @Override
   public void disableContextMenu() {
      this.support.disableContextMenu();
   }

   @Override
   public void addActionToContextMenu(Action var1) {
      this.support.addAdditionalAction(var1);
   }

   @Override
   public void addToContextMenu(JMenuItem var1) {
      this.support.addAdditionalMenuItem(var1);
   }

   @Override
   public void setSelectAllWhenFocused(boolean var1) {
      this.support.setSelectAllWhenFocused(var1);
   }

   @Override
   public boolean isEmpty() {
      return this.getDocument().getLength() == 0;
   }

   @Override
   public void setIcon(Icon var1) {
      Icon var2 = this.getIcon();
      if (var1 != var2) {
         if (var1 != null || this.overlay != null) {
            if (this.overlay == null) {
               this.overlay = new TextOverlay(this);
            }

            this.overlay.setIcon(var1);
            if (var2 != var1) {
               this.firePropertyChange("icon", var2, var1);
            }
         }
      }
   }

   @Override
   public Icon getIcon() {
      return this.overlay != null ? this.overlay.getIcon() : null;
   }

   @Override
   public UiProvider asUiProvider() {
      return this.support.asUiProvider();
   }

   @Override
   public void setPainter(ComponentPainter var1) {
      boolean var2 = var1 != this.painter;
      this.painter = var1;
      if (var2) {
         this.repaint();
      }
   }

   @Override
   protected void paintComponent(Graphics var1) {
      if (this.painter == null) {
         super.paintComponent(var1);
      } else {
         this.applyPainter((Graphics2D)var1);
      }
   }

   private void applyPainter(Graphics2D var1) {
      assert this.painter != null;
      if (!this.painter.preRendering(this, var1)) {
         super.paintComponent(var1);
         this.painter.postRendering(this, var1);
      }
   }

   public void setLabel(String var1) {
      this.support.setLabel(var1);
   }

   public void setLabel(JLabel var1) {
      this.support.setLabel(var1);
   }

   public JLabel getLabel() {
      return this.support.getLabel();
   }
}
