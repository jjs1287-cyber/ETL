package org.etlforge.application.ui.help;

import java.awt.event.ActionListener;
import java.net.URL;
import javax.help.CSH;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.help.HelpSetException;
import javax.help.CSH.DisplayHelpFromSource;
import javax.swing.AbstractButton;
import javax.swing.RootPaneContainer;
import org.etlforge.application.config.ApplicationConfiguration;

final class ApplicationUiJavaHelp extends ApplicationUiHelp {
   private final HelpSet helpSet = this.createHelpSet();
   private final HelpBroker helpBroker = this.helpSet != null ? this.helpSet.createHelpBroker() : null;

   @Override
   public void enableHelpOnButton(AbstractButton var1, String var2) {
      removeActionListener(var1);
      CSH.setHelpIDString(var1, var2);
      var1.addActionListener(new DisplayHelpFromSource(this.helpBroker));
   }

   private static void removeActionListener(AbstractButton var0) {
      for (ActionListener var4 : var0.getActionListeners()) {
         if (var4 instanceof DisplayHelpFromSource) {
            var0.removeActionListener(var4);
         }
      }
   }

   @Override
   public void disableHelpOnButton(AbstractButton var1) {
      if (var1 != null) {
         removeActionListener(var1);
         CSH.setHelpIDString(var1, null);
      }
   }

   @Override
   public void enableWindowHelpKey(RootPaneContainer var1, String var2) {
      this.helpBroker.enableHelpKey(var1.getRootPane(), var2, this.helpSet);
   }

   private HelpSet createHelpSet() {
      String var1 = ApplicationConfiguration.getHelpSetName();
      if (var1 == null) {
         return null;
      }

      ClassLoader var2 = ApplicationUiJavaHelp.class.getClassLoader();
      URL var3 = HelpSet.findHelpSet(var2, var1);

      try {
         return new HelpSet(null, var3);
      } catch (HelpSetException var5) {
         throw new AssertionError(var5);
      }
   }
}
