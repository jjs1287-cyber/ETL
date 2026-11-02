package org.etlforge.application.ui.window.editor.service;

import org.etlforge.application.ui.resources.ApplicationResources;

final class PackageResources {
   private static String getString(String var0) {
      return ApplicationResources.STRINGS.getString(var0);
   }

   private PackageResources() {
   }

   static class Editor {
      static final String ATTACH = PackageResources.getString("EditorService.Attach");
   }

   static class Popup {
      static final String TITLE = PackageResources.getString("EditorService.EditorPopup.Title");
   }

   static class SavePrompt {
      static final String TITLE = PackageResources.getString("EditorService.SavePrompt.Title");
      static final String CAPTION = PackageResources.getString("EditorService.SavePrompt.Caption");
      static final String SAVE_ALL = PackageResources.getString("EditorService.SavePrompt.SaveAll");
      static final String SAVE_NONE = PackageResources.getString("EditorService.SavePrompt.SaveNone");
      static final String CANCEL = PackageResources.getString("EditorService.SavePrompt.Cancel");
   }
}
