package org.etlforge.ui.text.contentassist;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import org.etlforge.ui.widget.window.PopupWindow;

class TokenInfoWindow {
   private static final int INFO_WIDTH = 400;
   private final Window parent;
   private final PopupWindow popup;
   private final JEditorPane textPane;

   TokenInfoWindow(Window var1, String var2) {
      this.parent = var1;
      this.textPane = this.createInfoTextDisplayer();
      this.popup = this.createWindow(this.textPane);
      this.setNewInfo(var2);
   }

   public void dispose() {
      this.popup.dispose();
   }

   public void show() {
      if (!this.popup.isVisible()) {
         Rectangle var1 = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
         Rectangle var2 = this.parent.getBounds();
         int var3 = var2.x + var2.width + 400 + 1;
         if (var3 <= var1.width) {
            this.displayInfoPopupToTheRight();
         } else {
            this.displayInfoPopupToTheLeft();
         }
      }
   }

   public void setNewInfo(String var1) {
      this.textPane.setText(var1);
      this.textPane.setCaretPosition(0);
   }

   public boolean contains(Point var1, int var2) {
      return this.popup.contains(var1, var2, var2);
   }

   private JEditorPane createInfoTextDisplayer() {
      JEditorPane var1 = new JEditorPane();
      var1.setContentType("text/html");
      var1.setEditable(false);
      var1.setFocusable(false);
      var1.setBackground(new Color(255, 255, 204));
      var1.setPreferredSize(new Dimension(400, 300));
      return var1;
   }

   private PopupWindow createWindow(JEditorPane var1) {
      JScrollPane var2 = new JScrollPane(var1);
      PopupWindow var3 = PopupWindow.newWindow(this.parent, var2, null);
      Dimension var4 = var2.getPreferredSize();
      var3.setPreferredSize(new Dimension(var4.width, Math.max(this.parent.getHeight(), 250)));
      var3.setResizable(true);
      var3.setFocusable(false);
      var3.setGiveFocusToOwnerWhenClosed(false);
      var3.setConsumeEscapeWhileVisible(false);
      return var3;
   }

   private void displayInfoPopupToTheRight() {
      Point var1 = new Point(this.parent.getWidth() + 1, 0);
      SwingUtilities.convertPointToScreen(var1, this.parent);
      this.popup.show(var1);
   }

   private void displayInfoPopupToTheLeft() {
      Point var1 = new Point(-410, 0);
      SwingUtilities.convertPointToScreen(var1, this.parent);
      this.popup.show(var1);
   }
}
