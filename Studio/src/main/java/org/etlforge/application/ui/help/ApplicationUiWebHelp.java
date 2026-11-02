package org.etlforge.application.ui.help;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import javax.swing.AbstractButton;
import javax.swing.RootPaneContainer;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.Alert;
import org.etlforge.system.ArgCheck;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.util.KeyUtils;

final class ApplicationUiWebHelp extends ApplicationUiHelp {
   private final ApplicationUiWebHelp.Links links;

   public ApplicationUiWebHelp(ApplicationUiWebHelp.Links var1) {
      this.links = Preconditions.checkNotNull(var1, "links");
   }

   @Override
   public void enableHelpOnButton(AbstractButton var1, String var2) {
      ArgCheck.checkNotEmpty(var2, "helpID");
      removeActionListener(var1);
      ApplicationUiWebHelp.LinkOpener var3 = this.createLinkOpener(var2);
      var1.addActionListener(var3);
   }

   private static void removeActionListener(AbstractButton var0) {
      for (ActionListener var4 : var0.getActionListeners()) {
         if (var4 instanceof ApplicationUiWebHelp.LinkOpener) {
            var0.removeActionListener(var4);
         }
      }
   }

   private ApplicationUiWebHelp.LinkOpener createLinkOpener(String var1) {
      URI var2 = this.links.getLink(var1).or(this.links.getHomePage());
      return new ApplicationUiWebHelp.LinkOpener(var2);
   }

   @Override
   public void disableHelpOnButton(AbstractButton var1) {
      if (var1 != null) {
         removeActionListener(var1);
      }
   }

   @Override
   public void enableWindowHelpKey(RootPaneContainer var1, String var2) {
      ApplicationUiWebHelp.LinkOpener var3 = this.createLinkOpener(var2);
      ActionKeyBinding.install(var1.getRootPane(), KeyUtils.HELP, var3);
   }

   private static class LinkOpener implements ActionListener {
      private final URI link;

      public LinkOpener(URI var1) {
         this.link = var1;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (Desktop.isDesktopSupported()) {
            Desktop var2 = Desktop.getDesktop();
            if (var2.isSupported(Action.BROWSE)) {
               this.openLink(var2);
               return;
            }
         }

         Alert.error("Your platform does not support opening a web browser.", "Not Supported");
      }

      private void openLink(Desktop var1) {
         try {
            var1.browse(this.link);
         } catch (IOException var3) {
            ErrorLog.attention(ApplicationUiWebHelp.class, null, var3);
         }
      }
   }

   public interface Links {
      URI getHomePage();

      Optional<URI> getLink(String var1);
   }
}
