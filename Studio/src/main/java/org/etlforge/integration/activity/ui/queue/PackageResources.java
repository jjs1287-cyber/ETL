package org.etlforge.integration.activity.ui.queue;

import java.text.MessageFormat;
import javax.swing.Icon;
import org.etlforge.integration.activity.ActivitySubject;
import org.etlforge.integration.activity.client.queue.QueueStatus;
import org.etlforge.integration.activity.ui.resources.ActivityResources;

final class PackageResources {
   static final String TITLE = getString("Queue.Title");
   static final String FILTER_TITLE = getString("Queue.Filter.Title");
   static final String STATUS_COMBO_ALL = getString("Queue.StatusCombo.All");
   static final Icon PENDING_ICON = getIcon("Queue.Pending");
   static final Icon RUNNING_ICON = getIcon("Queue.Running");
   static final String DELETE_TOOLTIP = getString("Queue.Displayer.Action.Delete.ToolTip");

   private PackageResources() {
   }

   private static String getString(String var0) {
      return ActivityResources.STRINGS.getString(var0);
   }

   private static String getFormattedMessage(String var0, Object[] var1) {
      return MessageFormat.format(getString(var0), var1);
   }

   private static Icon getIcon(String var0) {
      return ActivityResources.ICONS.getIcon(var0);
   }

   static class Deleter {
      static final String ERROR_TITLE = PackageResources.getString("Queue.Deleter.ErrorTitle");
   }

   static class Job {
      static final String STARTED = PackageResources.getString("Queue.Job.Started");
      static final String ERROR_TITLE = PackageResources.getString("Queue.Job.ErrorTitle");

      static String getName(ActivitySubject var0) {
         return PackageResources.getFormattedMessage("Queue.Job.Name", new Object[]{var0.getJobName()});
      }
   }

   static class Page {
      static final Icon ICON = PackageResources.getIcon("Queue.Page");

      static String getEmptyPageTitle(String var0) {
         return PackageResources.getFormattedMessage("Queue.Page.EmptyPageTitle", new Object[]{var0});
      }
   }

   static class QueueStatusFilter {
      static final String LABEL = PackageResources.getString("Queue.QueueStatusFilter.Label");
      static final String ALL_STATII_FILTER_STRING = PackageResources.getString("Queue.QueueStatusFilter.AllStatii");

      static String getFilterString(QueueStatus var0) {
         return var0 == null ? ALL_STATII_FILTER_STRING : PackageResources.getFormattedMessage("Queue.QueueStatusFilter.String", new Object[]{var0});
      }
   }
}
