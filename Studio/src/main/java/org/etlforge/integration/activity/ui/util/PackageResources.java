package org.etlforge.integration.activity.ui.util;

import javax.swing.Icon;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.integration.activity.ui.resources.ActivityResources;

final class PackageResources {
   private static final String getString(String var0) {
      return ActivityResources.STRINGS.getString(var0);
   }

   private PackageResources() {
   }

   static class Actions {
      static final String SELECT_ALL = ApplicationResources.STRINGS.getString("SelectAll");
      static final String UNSELET_ALL = ApplicationResources.STRINGS.getString("UnselectAll");
   }

   static class TimeWindowControl {
      static final Icon CALENDAR_ICON = ApplicationResources.ICONS.getIcon("Calendar.24");
      static final String LABEL_START = PackageResources.getString("TimeWindowControl.Label.Start");
      static final String LABEL_END = PackageResources.getString("TimeWindowControl.Label.End");
   }
}
