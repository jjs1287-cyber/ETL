package org.etlforge.integration.client.ui.interchange;

import java.text.MessageFormat;
import org.etlforge.integration.client.ui.interchange.resources.InterchangeResources;

final class PackageResources {
   static final String NO_MODEL_WINDOW_TITLE_APPENDIX = getString("NoModelTitleAppendix");

   private static final String getString(String var0) {
      return InterchangeResources.STRINGS.getString(var0);
   }

   private static final String getFormattedMessage(String var0, Object... var1) {
      return MessageFormat.format(getString(var0), var1);
   }

   private PackageResources() {
   }

   static class PageManager {
      static final String OPEN_PAGES_FAILED = PackageResources.getString("PageManager.Error.OpenPages");
   }

   static class ProjectEventLogSwitch {
      static final String ON = PackageResources.getString("ProjectEventLogSwitch.On");
      static final String OFF = PackageResources.getString("ProjectEventLogSwitch.Off");
   }

   static class SaveWorker {
      static final String SAVE_FAILED = PackageResources.getString("SaveWorker.Error.Save");
   }

   static class UiState {
      static String getSaveFailed(Throwable var0) {
         return PackageResources.getFormattedMessage("UiState.Error.Save", var0.getMessage());
      }
   }
}
