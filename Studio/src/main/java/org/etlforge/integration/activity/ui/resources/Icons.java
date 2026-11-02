package org.etlforge.integration.activity.ui.resources;

import java.util.ListResourceBundle;
import javax.swing.Icon;
import org.etlforge.ui.util.ImageLoader;

public final class Icons extends ListResourceBundle {
   private static final String IMAGE_PACKAGE = "org.etlforge.integration.activity.ui.resources.icons";
   private static final Object[][] CONTENTS = new Object[][]{
      {"History.OperationPage", ActivityIcons.HISTORY_16},
      {"Log.Page", ActivityIcons.LOG_16},
      {"Queue.Page", ActivityIcons.QUEUE_16},
      {"Queue.Pending", getIcon("QueueStatus_Pending.gif")},
      {"Queue.Running", getIcon("QueueStatus_Running.gif")}
   };

   @Override
   protected Object[][] getContents() {
      return CONTENTS;
   }

   private static Icon getIcon(String var0) {
      return ImageLoader.getImageIcon("org.etlforge.integration.activity.ui.resources.icons", var0);
   }
}
