package org.etlforge.application.ui.window.action;

import org.etlforge.application.ui.action.AbstractApplicationAction;
import org.etlforge.application.ui.action.ActionDictionary;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorService;
import org.etlforge.application.ui.window.editor.EditorServiceEvent;
import org.etlforge.application.ui.window.editor.EditorServiceListener;

public abstract class ApplicationEditorAction extends AbstractApplicationAction implements EditorServiceListener {
   private final EditorService editorService;

   public ApplicationEditorAction(ApplicationWindow var1, String var2, ActionDictionary var3) {
      super(var2, var3);
      this.editorService = var1.getEditorService();
      this.putValue("ShortDescription", var3.getToolTip(var2));
      var1.getEditorService().addEditorServiceListener(this);
      this.setWaitService(var1);
   }

   @Override
   public final void dispose() {
      this.editorService.removeEditorServiceListener(this);
   }

   protected final EditorService getEditorService() {
      return this.editorService;
   }

   protected final Editor getActiveEditor() {
      return this.editorService.getActiveEditor();
   }

   @Override
   public void activeEditorChanged(EditorServiceEvent var1) {
   }

   @Override
   public void editorClosed(EditorServiceEvent var1) {
   }

   @Override
   public void editorOpened(EditorServiceEvent var1) {
   }

   @Override
   public void dirtyStateChanged(EditorServiceEvent var1) {
   }
}
