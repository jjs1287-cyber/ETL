package org.etlforge.integration.activity.ui.core.action.resources;

import java.util.ListResourceBundle;
import org.etlforge.integration.activity.ui.resources.ActivityIcons;

public final class Icons extends ListResourceBundle {
   private static final Object[][] CONTENTS = new Object[][]{
      {smallIconKey("Log"), ActivityIcons.LOG_16},
      {mediumIconKey("Log"), ActivityIcons.LOG_24},
      {largeIconKey("Log"), ActivityIcons.LOG_32},
      {smallIconKey("Queue"), ActivityIcons.QUEUE_16},
      {mediumIconKey("Queue"), ActivityIcons.QUEUE_24},
      {largeIconKey("Queue"), ActivityIcons.QUEUE_32},
      {smallIconKey("History"), ActivityIcons.HISTORY_16},
      {mediumIconKey("History"), ActivityIcons.HISTORY_24},
      {largeIconKey("History"), ActivityIcons.HISTORY_32}
   };

   private static final String smallIconKey(String var0) {
      return "activity.action." + var0 + ".Icon.Small";
   }

   private static final String mediumIconKey(String var0) {
      return "activity.action." + var0 + ".Icon.Medium";
   }

   private static final String largeIconKey(String var0) {
      return "activity.action." + var0 + ".Icon.Large";
   }

   @Override
   protected Object[][] getContents() {
      return CONTENTS;
   }
}
