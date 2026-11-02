package org.etlforge.application.ui.widget;

import javax.swing.JButton;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.widget.Label;
import org.etlforge.ui.widget.LabelDecorator;
import org.etlforge.ui.widget.button.ButtonUtils;

public final class LabelDecorators {
   public static final LabelDecorator BROWSE_BUTTON = new LabelDecorator() {
      @Override
      public void decorate(Label var1) {
         var1.setIcon(null);
         String var2 = ApplicationResources.STRINGS.getString("Browse") + "...";
         MnemonicString var3 = ApplicationResources.MNEMONIC_FACTORY.fromString(var2);
         var1.setText(var3);
      }
   };
   public static final LabelDecorator REFRESH_BUTTON = new LabelDecorator() {
      @Override
      public void decorate(Label var1) {
         String var2 = ApplicationResources.STRINGS.getString("Refresh");
         MnemonicString var3 = ApplicationResources.MNEMONIC_FACTORY.fromString(var2);
         var1.setText(var3);
         var1.setIcon(CommonIcons.REFRESH_16);
      }
   };

   public static void decorate(JButton var0, LabelDecorator var1) {
      Label var2 = ButtonUtils.asLabel(var0);
      var1.decorate(var2);
   }

   private LabelDecorators() {
   }
}
