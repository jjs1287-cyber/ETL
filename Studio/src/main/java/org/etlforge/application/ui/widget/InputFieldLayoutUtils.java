package org.etlforge.application.ui.widget;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.Scrollable;
import org.etlforge.ui.input.InputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.widget.KongaScrollPane;

public final class InputFieldLayoutUtils {
   public static void layoutOneColumn(InputPanel var0, InputField[] var1, boolean var2, boolean var3) {
      int var4 = var1.length;
      var0.setLayout(new GridBagLayout());
      GridBagConstraints var5 = getLeftColumnLabelConstraints();
      var5.anchor = 17;
      GridBagConstraints var6 = getLeftInputConstraints(var2);
      var6.anchor = 17;

      for (int var7 = 0; var7 < var4; var7++) {
         if (var1[var7] != null) {
            JLabel var8 = var1[var7].getLabel();
            if (var8 == null) {
               var8 = new JLabel(" ");
            }

            var0.add(var8, var5);
            if (var3 && var7 == var4 - 1) {
               var6.gridheight = 0;
            }

            var0.add(getInputComponent(var1[var7]), var6);
         }
      }
   }

   private static Component getInputComponent(InputField var0) {
      JComponent var1 = var0.getInputComponent();
      return useScroll(var1) ? new KongaScrollPane(var1) : var1;
   }

   private static boolean useScroll(JComponent var0) {
      return var0 instanceof Scrollable ? !(var0 instanceof JTextField) : false;
   }

   private static GridBagConstraints getLeftColumnLabelConstraints() {
      GridBagConstraints var0 = new GridBagConstraints();
      var0.insets = new Insets(5, 5, 5, 5);
      var0.anchor = 18;
      var0.gridx = 0;
      return var0;
   }

   private static GridBagConstraints getLeftInputConstraints(boolean var0) {
      GridBagConstraints var1 = new GridBagConstraints();
      var1.insets = new Insets(5, 5, 5, 5);
      var1.anchor = 18;
      if (var0) {
         var1.fill = 2;
      }

      var1.gridx = 1;
      return var1;
   }

   private InputFieldLayoutUtils() {
   }
}
