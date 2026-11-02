package org.etlforge.application.ui.window.action;

import com.google.common.base.Strings;
import java.awt.event.ActionEvent;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorServiceEvent;
import org.etlforge.application.ui.window.editor.UndoService;
import org.etlforge.application.ui.window.editor.UndoServiceListener;
import org.etlforge.system.Disposable;

abstract class AbstractUndoRedoAction extends ApplicationEditorAction {
   private final String baseName;
   private AbstractUndoRedoAction.Handler handler;

   protected AbstractUndoRedoAction(ApplicationWindow var1, String var2, String var3) {
      super(var1, var2, WindowActionDictionary.DICTIONARY);
      this.baseName = var3;
   }

   @Override
   public final void execute(ActionEvent var1) {
      if (this.handler != null) {
         this.handler.run();
      }
   }

   @Override
   public final void activeEditorChanged(EditorServiceEvent var1) {
      this.disposeCurrentHandler();
      this.installNewHandler();
   }

   private void disposeCurrentHandler() {
      if (this.handler != null) {
         this.handler.dispose();
         this.handler = null;
      }
   }

   private void installNewHandler() {
      Editor var1 = this.getActiveEditor();
      UndoService var2 = var1.getUndoService();
      UndoService.Activity var3 = this.getActivity(var2);
      this.handler = new AbstractUndoRedoAction.Handler(var2, var3);
      this.handler.install();
   }

   protected abstract UndoService.Activity getActivity(UndoService var1);

   @Override
   public final void editorClosed(EditorServiceEvent var1) {
      boolean var2 = this.getEditorService().getNumberOfEditors() == 0;
      if (var2) {
         this.disposeCurrentHandler();
      }
   }

   private class Handler implements UndoServiceListener, Disposable {
      private final UndoService undoService;
      private final UndoService.Activity activity;

      public Handler(UndoService var2, UndoService.Activity var3) {
         this.undoService = var2;
         this.activity = var3;
      }

      public void install() {
         this.undoService.addListener(this);
         this.updateActionState();
      }

      public void updateActionState() {
         String var1 = AbstractUndoRedoAction.this.baseName;
         if (this.activity.isAvailable()) {
            AbstractUndoRedoAction.this.setEnabled(true);
            String var2 = this.activity.getRepresentationName();
            if (!Strings.isNullOrEmpty(var2)) {
               var1 = var2;
            }
         } else {
            AbstractUndoRedoAction.this.setEnabled(false);
         }

         AbstractUndoRedoAction.this.putValue("Name", var1);
      }

      public void run() {
         if (this.activity.isAvailable()) {
            this.activity.invoke();
         }
      }

      @Override
      public void dispose() {
         AbstractUndoRedoAction.this.setEnabled(false);
         AbstractUndoRedoAction.this.putValue("Name", AbstractUndoRedoAction.this.baseName);
         this.undoService.removeListener(this);
      }

      @Override
      public void undoStateChanged() {
         this.updateActionState();
      }
   }
}
