package org.etlforge.ui.util.time;

import org.etlforge.ui.resources.UiResourceBundle;

final class PackageResources {
   private PackageResources() {
      throw new UnsupportedOperationException();
   }

   private static String getString(String var0) {
      return UiResourceBundle.getString(var0);
   }

   static final class DateTimeChooser {
      static final String CHOOSE_DATE = PackageResources.getString("DateTimeChooser.ChooseDate");
      static final String CHOOSE_DATE_TIME = PackageResources.getString("DateTimeChooser.ChooseDateTime");
      static final String CHOOSE_TIME = PackageResources.getString("DateTimeChooser.ChooseTime");
   }
}
