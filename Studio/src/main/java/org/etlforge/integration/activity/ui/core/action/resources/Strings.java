package org.etlforge.integration.activity.ui.core.action.resources;

import java.util.ListResourceBundle;

public final class Strings extends ListResourceBundle {
   private static final Object[][] CONTENTS = new Object[][]{
      {getNameKey("Log"), "Operation &Log"},
      {getToolTipKey("Log"), "<html>Show the log for the selected operation,<br>or for all operations under the selected folder or project</html>"},
      {getNameKey("Queue"), "Operation &Queue"},
      {getToolTipKey("Queue"), "<html>Show the queue for the selected operation,<br>or for all operations under the selected folder or project</html>"},
      {getNameKey("History"), "Operation &History"},
      {getToolTipKey("History"), "<html>Show the history for the selected operation,<br>or for all operations under the selected folder or project</html>"}
   };

   private static final String getNameKey(String var0) {
      return "activity.action." + var0 + ".Name";
   }

   private static final String getToolTipKey(String var0) {
      return "activity.action." + var0 + ".ToolTip";
   }

   @Override
   protected Object[][] getContents() {
      return CONTENTS;
   }
}
