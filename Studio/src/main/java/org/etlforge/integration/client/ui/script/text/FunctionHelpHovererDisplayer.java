package org.etlforge.integration.client.ui.script.text;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import org.etlforge.application.ui.widget.HtmlDisplayer;
import org.etlforge.integration.data.script.FunctionRegistry;
import org.etlforge.system.Disposable;
import org.etlforge.ui.widget.window.PopupWindow;
import org.etlforge.util.Interval;

public final class FunctionHelpHovererDisplayer implements TokenHovererDisplayer {
   private JTextComponent target;

   @Override
   public Disposable display(JTextComponent var1, String var2, Interval var3, MouseEvent var4) {
      this.target = var1;
      FunctionRegistry var5 = FunctionRegistry.getRegistry();
      String var6 = var5.getFunctionInfo(var2);
      org.etlforge.integration.data.script.Function js = org.etlforge.integration.data.script.JavaScriptFunctions.find(var2);
      if (js != null && org.etlforge.integration.data.script.JavaScriptFunctions.isInside(var1.getText(),var3.startsAt()))
         var6 = "<html><b>" + js.getDisplayName() + "</b><p>" + js.getDocumentation() + "</p></html>";
      if (var6 == null) {
         return null;
      }

      JComponent var7 = this.createHtmlDisplayer(var6);
      PopupWindow var8 = this.createPopupWindow(var7, var3, var4);
      var8.setPreferredSize(new Dimension(440, 320));
      var8.setResizable(true);
      var8.show();
      return var8;
   }

   private JComponent createHtmlDisplayer(String var1) {
      HtmlDisplayer var2 = new HtmlDisplayer();
      var2.setText(var1);
      var2.setCaretPosition(0);
      var2.setBackground(new Color(255, 255, 204));
      return var2;
   }

   private PopupWindow createPopupWindow(JComponent var1, Interval var2, MouseEvent var3) {
      JScrollPane var4 = new JScrollPane(var1);
      Point var5 = this.getLocationOnScreenForPopup(var2, var3);
      PopupWindow var6 = PopupWindow.newWindow(this.target, var4, var5);
      var6.setGiveFocusToOwnerWhenClosed(false);
      var6.setFocusable(false);
      return var6;
   }

   private Point getLocationOnScreenForPopup(Interval var1, MouseEvent var2) {
      Object var3 = null;

      try {
         Rectangle var4 = this.target.modelToView(var1.startsAt());
         var3 = new Point(var4.x + 2, var4.y + var4.height / 2);
      } catch (BadLocationException var5) {
         var3 = var2.getPoint();
      }

      Point var7 = (Point)var3;
      SwingUtilities.convertPointToScreen(var7, this.target);
      var7.y += 10;
      return var7;
   }
}
