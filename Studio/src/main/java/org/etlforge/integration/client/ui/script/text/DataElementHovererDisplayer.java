package org.etlforge.integration.client.ui.script.text;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;
import org.etlforge.integration.client.ui.script.builder.dataelement.ScriptDataElementsModel;
import org.etlforge.integration.dataelement.GlobalDE;
import org.etlforge.integration.debug.client.de.DataElement;
import org.etlforge.integration.debug.client.de.DataElementScope;
import org.etlforge.system.Disposable;
import org.etlforge.ui.Empty;
import org.etlforge.ui.widget.window.PopupWindow;
import org.etlforge.util.Interval;
import org.etlforge.util.string.StringLimiter;

public final class DataElementHovererDisplayer implements TokenHovererDisplayer {
   private JTextComponent target;
   private final ScriptDataElementsModel dataElements;

   public DataElementHovererDisplayer(ScriptDataElementsModel var1) {
      this.dataElements = var1;
   }

   @Override
   public Disposable display(JTextComponent var1, String var2, Interval var3, MouseEvent var4) {
      this.target = var1;
      String var5 = var2;
      DataElementScope var6 = DataElementScope.SOURCE;
      if (GlobalDE.isGlobalDE(var2)) {
         var5 = GlobalDE.nameOf(var2);
         var6 = DataElementScope.GLOBAL;
      }

      DataElement var7 = this.lookupDataElement(var5, var6);
      if (var7 == null) {
         return null;
      }

      JComponent var8 = this.createDataElementValueDisplayer(var7);
      PopupWindow var9 = this.createPopupWindow(var8, var4);
      var9.show();
      return var9;
   }

   private DataElement lookupDataElement(String var1, DataElementScope var2) {
      return this.dataElements != null ? this.dataElements.lookup(var1, var2) : null;
   }

   private JComponent createDataElementValueDisplayer(DataElement var1) {
      String var2 = this.createLabelText(var1);
      JLabel var3 = new JLabel(var2);
      var3.setEnabled(var1.getValue() != null);
      var3.setOpaque(true);
      var3.setBackground(new Color(255, 255, 204));
      var3.setBorder(Empty.border(2));
      return var3;
   }

   private String createLabelText(DataElement var1) {
      String var2 = var1.getValue();
      String var3 = var2 != null ? new StringLimiter(var2).getLimitedString(100) : "null";
      String var4 = var2 != null ? var1.getTypeName() : "[unknown]";
      StringBuilder var5 = new StringBuilder("<html><table><tr>");
      var5.append("<td><b>Type:</b></td><td>").append(var4).append("</td></tr>");
      var5.append("<td><b>Value:</b></td><td>").append(var3).append("</td></tr>");
      var5.append("</table></html>");
      return var5.toString();
   }

   private PopupWindow createPopupWindow(JComponent var1, MouseEvent var2) {
      JScrollPane var3 = new JScrollPane(var1);
      Point var4 = this.getLocationOnScreenForPopup(var2);
      PopupWindow var5 = PopupWindow.newWindow(this.target, var3, var4);
      var5.setGiveFocusToOwnerWhenClosed(false);
      var5.setFocusable(false);
      return var5;
   }

   private Point getLocationOnScreenForPopup(MouseEvent var1) {
      Point var2 = var1.getPoint();
      SwingUtilities.convertPointToScreen(var2, this.target);
      return var2;
   }
}
