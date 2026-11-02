package org.etlforge.integration.client.ui.admin.resources;

import java.util.ListResourceBundle;
import javax.swing.Icon;
import org.etlforge.ui.util.ImageLoader;

public final class Icons extends ListResourceBundle {
   private static final Object[][] CONTENTS = new Object[][]{
      {"ProjectConsole.DownloadAction", getIcon("DownloadOperationTree16.gif")},
      {"UserConsole.DownloadAction", getIcon("DownloadOperationTree16.gif")},
      {"Jms.Console", AdminIcons.JMS_CONSOLE_16},
      {"Jms.ActionPanel", AdminIcons.ACTIONS_32},
      {"Jms.ActionPanel.Start", getIcon("StartEngine16.gif")},
      {"Jms.ActionPanel.Stop", getIcon("StopEngine16.gif")},
      {"Jms.StatusPanel", AdminIcons.STATUS_32},
      {"Scheduler.ActionPanel.Start", getIcon("Scheduler.Start16.png")},
      {"Scheduler.ActionPanel.Stop", getIcon("Scheduler.Stop16.png")},
      {"Scheduler.ActionPanel", AdminIcons.ACTIONS_32},
      {"Scheduler.StatusPanel", AdminIcons.STATUS_32},
      {"System.Engine", getIcon("ProcessEngine16.png")},
      {"System.ActionPanel", AdminIcons.ACTIONS_32},
      {"System.ActionPanel.Start", getIcon("ProcessEngine.Start16.png")},
      {"System.ActionPanel.Stop", getIcon("ProcessEngine.Stop16.png")},
      {"System.StatusPanel", AdminIcons.STATUS_32},
      {"StatusLight.None", getIcon("StatusLight_None.gif")},
      {"StatusLight.Green", getIcon("StatusLight_Green.gif")},
      {"StatusLight.Red", getIcon("StatusLight_Red.gif")},
      {"StatusLight.Yellow", getIcon("StatusLight_Yellow.gif")},
      {"DownloadProjects.16", getIcon("DownloadProjects16.gif")},
      {"DownloadProjects.32", getIcon("DownloadProjects32.gif")}
   };
   private static final String IMAGE_PACKAGE = "org.etlforge.integration.client.ui.admin.resources.icons";

   @Override
   protected Object[][] getContents() {
      return CONTENTS;
   }

   private static Icon getIcon(String var0) {
      return ImageLoader.getImageIcon("org.etlforge.integration.client.ui.admin.resources.icons", var0);
   }
}
