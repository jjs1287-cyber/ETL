package org.etlforge.ui.widget.button;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPopupMenu;
import org.etlforge.ui.widget.menu.PopupMenuActionPopulator;
import org.etlforge.ui.widget.popup.KongaPopupMenu;

public final class PopupMenuButton extends JButton implements ActionListener {
   public static final int SHOW_POPUP_ON_MOUSE_OVER = 0;
   public static final int SHOW_POPUP_ON_CLICK = 1;
   private JPopupMenu popupMenu;
   private int popupWidth;
   private int invokeMode = 1;
   private PopupMenuActionPopulator popupMenuPopulator;

   public PopupMenuButton(Action[] var1) {
      this("", null, var1);
   }

   public PopupMenuButton(Icon var1, Action[] var2) {
      this("", var1, var2);
   }

   public PopupMenuButton(String var1, Action[] var2) {
      this(var1, null, var2);
   }

   public PopupMenuButton(String var1, Icon var2, Action[] var3) {
      this(var1, var2, var3, new PopupMenuButton.DefaultPopupMenuPopulator());
   }

   public PopupMenuButton(String var1, Icon var2, JPopupMenu var3) {
      super(var1, var2);
      this.popupMenu = var3;
      this.init(null);
   }

   public PopupMenuButton(String var1, Icon var2, Action[] var3, PopupMenuActionPopulator var4) {
      super(var1, var2);
      this.popupMenuPopulator = var4;
      this.init(var3);
   }

   private void init(Action[] var1) {
      if (var1 != null) {
         this.createPopupMenu(var1);
      }

      this.addActionListener(this);
      PopupMenuButton.MouseHandler var2 = new PopupMenuButton.MouseHandler();
      this.addMouseListener(var2);
      this.popupMenu.addMouseListener(var2);
   }

   private void createPopupMenu(Action[] var1) {
      this.popupMenu = new KongaPopupMenu();

      for (Action var5 : var1) {
         this.popupMenuPopulator.addMenuItem(this.popupMenu, var5);
      }

      this.setEnabled(var1.length > 0);
      this.popupWidth = this.popupMenu.getPreferredSize().width;
   }

   public void setActions(Action[] var1) {
      this.init(var1);
   }

   public void setPopupInvokeMode(int var1) {
      switch (var1) {
         case 0:
         case 1:
            this.invokeMode = var1;
            return;
         default:
            throw new IllegalArgumentException("mode must be one of SHOW_POPUP_ON_MOUSE_OVER or SHOW_POPUP_ON_CLICK.");
      }
   }

   @Override
   public void setEnabled(boolean var1) {
      boolean var2 = var1 && this.popupMenu != null && this.popupMenu.getComponentCount() > 0;
      super.setEnabled(var2);
   }

   private void showPopup() {
      this.showPopup(0, this.getHeight());
   }

   private void showPopup(int var1, int var2) {
      if (!this.popupMenu.isVisible()) {
         this.popupMenu.show(this, var1, var2);
      }
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      this.showPopup();
   }

   public void dispose() {
      if (this.popupMenu != null) {
         this.popupMenu.removeAll();

         for (MouseListener var4 : this.popupMenu.getMouseListeners()) {
            this.popupMenu.removeMouseListener(var4);
         }
      }
   }

   private static class DefaultPopupMenuPopulator implements PopupMenuActionPopulator {
      private DefaultPopupMenuPopulator() {
      }

      @Override
      public void addMenuItem(JPopupMenu var1, Action var2) {
         if (var2 == null) {
            var1.addSeparator();
         } else {
            var1.add(var2);
         }
      }
   }

   private class MouseHandler extends MouseAdapter {
      private Object currentSource;

      private MouseHandler() {
      }

      @Override
      public void mouseEntered(MouseEvent var1) {
         this.currentSource = var1.getSource();
         if (this.currentSource == PopupMenuButton.this && PopupMenuButton.this.invokeMode == 0 && !PopupMenuButton.this.popupMenu.isVisible()) {
            PopupMenuButton.this.showPopup(var1.getX() - PopupMenuButton.this.popupWidth, var1.getY());
         }
      }
   }
}
