package org.etlforge.integration.activity.ui.log;

import java.text.MessageFormat;
import java.util.EnumSet;
import javax.swing.Icon;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.collections.Collections2;
import org.etlforge.integration.activity.ActivitySubject;
import org.etlforge.integration.activity.client.log.LogStatus;
import org.etlforge.integration.activity.ui.resources.ActivityResources;
import org.etlforge.ui.MnemonicString;

final class PackageResources {
   static final String TITLE = getString("Log.Title");
   static final String FILTER_TITLE = getString("Log.Filter.Title");
   static final String STATUS_COMBO_ALL = getString("Log.StatusCombo.All");

   static Icon getStatusIcon(LogStatus var0) {
      return LogStatusIcons.getIcon(var0);
   }

   private static String getString(String var0) {
      return ActivityResources.STRINGS.getString(var0);
   }

   private static String getFormattedMessage(String var0, Object... var1) {
      return MessageFormat.format(getString(var0), var1);
   }

   private static Icon getIcon(String var0) {
      return ActivityResources.ICONS.getIcon(var0);
   }

   private static MnemonicString getMnemonicString(String var0) {
      return ApplicationResources.MNEMONIC_FACTORY.fromString(getString(var0));
   }

   static class Displayer {
      static final String DETAILS_LABEL = PackageResources.getString("Log.Displayer.Details.Label");
      static final String FIND_TOOLTIP = PackageResources.getString("Log.Displayer.Details.Find.ToolTip");
      static final String FIND_TITLE = PackageResources.getString("Log.Displayer.Details.Find.Title");
   }

   static class LogJob {
      static final String STARTED = PackageResources.getString("Log.LogJob.Started");
      static final String ERROR_TITLE = PackageResources.getString("Log.LogJob.ErrorTitle");

      static String getName(ActivitySubject var0) {
         return PackageResources.getFormattedMessage("Log.LogJob.Name", var0.getJobName());
      }
   }

   static class LogStatusFilter {
      static final String LABEL = PackageResources.getString("Log.LogStatusFilter.Label");

      static String getFilterString(EnumSet<LogStatus> var0) {
         String var1;
         if (var0.size() == LogStatus.values().length) {
            var1 = PackageResources.getString("Log.LogStatusFilter.AllStatii");
         } else if (var0.equals(LogStatus.completed())) {
            var1 = PackageResources.getString("Log.LogStatusFilter.Completed");
         } else if (var0.equals(LogStatus.stillInQueue())) {
            var1 = PackageResources.getString("Log.LogStatusFilter.StillRunning");
         } else {
            var1 = Collections2.join(var0, " / ");
         }

         return PackageResources.getFormattedMessage("Log.LogStatusFilter.String", var1);
      }
   }

   static class MessageFilter {
      static final String LABEL = PackageResources.getString("Log.MessageFilter.Label");

      static String getFilterString(String var0) {
         return PackageResources.getFormattedMessage("Log.MessageFilter.String", var0);
      }
   }

   static class Page {
      static final Icon ICON = PackageResources.getIcon("Log.Page");

      static String getEmptyPageTitle(String var0) {
         return PackageResources.getFormattedMessage("Log.Page.EmptyPageTitle", var0);
      }
   }

   static class TableController {
      static final MnemonicString AS_PLAIN_TEXT = PackageResources.getMnemonicString("Log.TableController.AsPlainText");
      static final MnemonicString AS_XML = PackageResources.getMnemonicString("Log.TableController.AsXml");

      static class Copy {
         static final String NAME = PackageResources.getString("Log.TableController.Copy.Name");
         static final String TOOLTIP = PackageResources.getString("Log.TableController.Copy.ToolTip");
         static final Icon ICON = null;
      }

      static class Save {
         static final String NAME = PackageResources.getString("Log.TableController.Save.Name");
         static final String TOOLTIP = PackageResources.getString("Log.TableController.Save.ToolTip");
         static final Icon ICON = ApplicationResources.ICONS.getIcon("Save.16");
         static final String ERROR = PackageResources.getString("Log.TableController.Save.Error");
      }
   }
}
