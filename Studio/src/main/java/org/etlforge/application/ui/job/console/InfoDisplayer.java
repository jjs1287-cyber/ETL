package org.etlforge.application.ui.job.console;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Date;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.ui.layout.DialogLayout;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.widget.LabelDecorator;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.window.PopupWindow;
import org.etlforge.util.text.ThreadSafeDateFormat;

final class InfoDisplayer {
   private static final String UNKNOWN = Strings.get("JobConsole.InfoDisplayer.Label.Unknown");
   private JComponent displayer;
   private PopupWindow popup;
   private static final ThreadSafeDateFormat timeFormat = new ThreadSafeDateFormat(Strings.get("JobConsole.InfoDisplayer.TimeFormat"));

   InfoDisplayer(long var1, long var3) {
      this.createComponents(var1, var3);
   }

   void show(JComponent var1) {
      if (this.popup == null) {
         this.popup = this.createPopupWindow(var1);
      }

      Point var2 = this.getPopupLocation(var1);
      this.popup.show(var2);
   }

   private Point getPopupLocation(JComponent var1) {
      Dimension var2 = var1.getSize();
      Point var3 = new Point(var2.width / 2, var2.height / 2);
      SwingUtilities.convertPointToScreen(var3, var1);
      return var3;
   }

   private void createComponents(long var1, long var3) {
      JLabel var5 = createLabel("JobConsole.InfoDisplayer.Label.Started");
      JLabel var6 = createLabel("JobConsole.InfoDisplayer.Label.Ended");
      JLabel var7 = createLabel("JobConsole.InfoDisplayer.Label.Duration");
      TextStyle var8 = TextStyles.DefaultTextSmall;
      KongaLabel var9 = var8.makeLabel(getTime(var1));
      KongaLabel var10 = var8.makeLabel(getTime(var3));
      KongaLabel var11 = var8.makeLabel(getDuration(var1, var3));
      this.displayer = new JPanel(new DialogLayout());
      this.displayer.add(var5);
      this.displayer.add(var9);
      this.displayer.add(var6);
      this.displayer.add(var10);
      this.displayer.add(var7);
      this.displayer.add(var11);
      this.displayer.setBackground(Colors.NEUTRAL_BACKGROUND);
      this.displayer.setBorder(new BorderBuilder().line().empty(5, 5, 10, 5));
   }

   private static JLabel createLabel(String var0) {
      return TextStyles.DefaultTextSmall.makeLabel(Strings.get(var0));
   }

   private PopupWindow createPopupWindow(JComponent var1) {
      PopupWindow var2 = PopupWindow.newWindow(var1, this.displayer, null);
      var2.setResizable(true);
      LabelDecorator var3 = this.createTitle();
      var2.setTitle(var3, true);
      return var2;
   }

   private LabelDecorator createTitle() {
      return StyleSheet.createPopupWindowTitle(Strings.get("JobConsole.InfoDisplayer.Title"));
   }

   private static String getTime(long var0) {
      return var0 < 0L ? UNKNOWN : timeFormat.format(new Date(var0));
   }

   private static String getDuration(long var0, long var2) {
      if (var0 >= 0L && var2 >= 0L) {
         long var4 = var2 - var0;
         String var6 = getThreeDigits((int)(var4 % 1000L));
         if (var4 < 1000L) {
            return "00:00:00." + var6;
         }

         int var7 = (int)(var4 / 1000L);
         if (var7 < 60) {
            return "00:00:" + getTwoDigits(var7) + "." + var6;
         }

         var7 %= 60;
         int var8 = (int)(var4 / 60000L);
         if (var8 < 60) {
            return "00:" + getTwoDigits(var8) + ":" + getTwoDigits(var7) + "." + var6;
         }

         var8 %= 60;
         int var9 = (int)(var4 / 3600000L);
         return var9 + ":" + getTwoDigits(var8) + ":" + getTwoDigits(var7) + "." + var6;
      } else {
         return UNKNOWN;
      }
   }

   private static String getTwoDigits(int var0) {
      return var0 < 10 ? "0" + var0 : String.valueOf(var0);
   }

   private static String getThreeDigits(int var0) {
      return var0 < 10 ? "00" + var0 : (var0 < 100 ? "0" + var0 : String.valueOf(var0));
   }
}
