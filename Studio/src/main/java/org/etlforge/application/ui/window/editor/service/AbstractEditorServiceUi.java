package org.etlforge.application.ui.window.editor.service;

import org.etlforge.application.ui.window.editor.QuickSwitchEditorWindow;

public abstract class AbstractEditorServiceUi implements EditorServiceUi {
   private final DefaultEditorService editorService;

   protected AbstractEditorServiceUi(DefaultEditorService var1) {
      this.editorService = var1;
   }

   protected final DefaultEditorService getEditorService() {
      return this.editorService;
   }

   @Override
   public void openQuickSelector() {
      QuickSwitchEditorWindow.open(this.editorService.getAppWin());
   }

   @Override
   public void prepareForUiSwitch() {
   }
}
