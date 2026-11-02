package org.etlforge.application.ui.help;

import com.google.common.io.InputSupplier;
import com.google.common.io.Resources;
import java.net.URL;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.RootPaneContainer;
import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.config.FatalConfigurationError;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.ui.widget.button.KongaButton;

public abstract class ApplicationUiHelp {
   public static final boolean ENABLED = ApplicationConfiguration.getBoolean("Help.Enabled", true);
   private static final ApplicationUiHelp instance = createInstance();

   public static ApplicationUiHelp getHelp() {
      return instance;
   }

   private static ApplicationUiHelp createInstance() {
      HelpLocation var0 = ApplicationConfiguration.getEnum("Help.Location", HelpLocation.class, HelpLocation.WEB);
      switch (var0) {
         case WEB:
            return createWebHelp();
         case JAVA_HELP:
            return new ApplicationUiJavaHelp();
         default:
            throw new RuntimeException("Unexpected HelpLocation: " + var0);
      }
   }

   private static ApplicationUiHelp createWebHelp() {
      URL var0 = Resources.getResource("WebHelp.xml");
      if (var0 != null) {
         InputSupplier var1 = Resources.newInputStreamSupplier(var0);
         XmlBasedWebLinks var2 = new XmlBasedWebLinks(var1);
         return new ApplicationUiWebHelp(var2);
      } else {
         FatalConfigurationError.throwError(
            5, ApplicationUiHelp.class, new Exception("The WebHelp.xml configuration file is missing."), "Failed to load the application help system."
         );
         return null;
      }
   }

   public abstract void enableHelpOnButton(AbstractButton var1, String var2);

   public abstract void disableHelpOnButton(AbstractButton var1);

   public abstract void enableWindowHelpKey(RootPaneContainer var1, String var2);

   public final JButton createAndConfigureHelpButton(String var1, boolean var2) {
      JButton var3 = createHelpButton(var2);
      this.enableHelpOnButton(var3, var1);
      return var3;
   }

   static JButton createHelpButton(boolean var0) {
      String var1 = var0 ? "&Help" : null;
      return new KongaButton(var1, CommonIcons.HELP_16);
   }
}
