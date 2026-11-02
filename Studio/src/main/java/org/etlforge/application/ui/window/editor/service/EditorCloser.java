package org.etlforge.application.ui.window.editor.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.SaveEditorException;

final class EditorCloser {
   private final DefaultEditorService editorService;
   private int editorsThatWereClosed;
   private Set<Editor> cleanEditors;
   private Set<Editor> dirtyEditors;

   EditorCloser(DefaultEditorService var1) {
      this.editorService = var1;
   }

   void closeEditors(Collection<Editor> var1, Editor var2) {
      this.collectDirtyAndCleanEditors(var1, var2);
      if (this.handleDirtyEditors()) {
         this.handleCleanEditors();
      }
   }

   int getNumberOfEditorsThatWereClosed() {
      return this.editorsThatWereClosed;
   }

   private void collectDirtyAndCleanEditors(Collection<Editor> var1, Editor var2) {
      this.cleanEditors = new HashSet<>();
      this.dirtyEditors = new HashSet<>();

      for (Editor var4 : var1) {
         if (var4 != var2) {
            if (var4.isDirty()) {
               this.dirtyEditors.add(var4);
            } else {
               this.cleanEditors.add(var4);
            }
         }
      }
   }

   private Editor[] getEditorsToSave() {
      EditorsToSaveCollector var1 = this.getEditorsToSaveCollector();
      Editor[] var2 = var1.collect();
      return var1.wasCancelled() ? null : var2;
   }

   private EditorsToSaveCollector getEditorsToSaveCollector() {
      return new SaveDirtyEditorsPrompter(this.editorService.getAppWin(), this.dirtyEditors);
   }

   private boolean handleDirtyEditors() {
      if (this.dirtyEditors.isEmpty()) {
         return true;
      }

      Editor[] var1 = this.getEditorsToSave();
      if (var1 == null) {
         this.editorsThatWereClosed = -1;
         return false;
      }

      if (!this.saveEditors(var1)) {
         return false;
      }

      this.processRemainingDirtyEditors();
      return true;
   }

   private boolean saveEditors(Editor[] var1) {
      for (Editor var5 : var1) {
         this.dirtyEditors.remove(var5);
         if (!this.saveEditor(var5)) {
            return false;
         }

         if (this.editorService.closeEditorImpl(var5, true)) {
            this.editorsThatWereClosed++;
         }
      }

      return true;
   }

   private boolean saveEditor(Editor var1) {
      try {
         var1.save();
         return true;
      } catch (SaveEditorException var3) {
         ErrorLog.attention(EditorCloser.class, var3.getMessage(), var3);
         return false;
      }
   }

   private void processRemainingDirtyEditors() {
      for (Editor var2 : this.dirtyEditors) {
         this.editorService.openEditor(var2);
         if (this.editorService.closeEditorImpl(var2, true)) {
            this.editorsThatWereClosed++;
         }
      }
   }

   private void handleCleanEditors() {
      for (Editor var2 : this.cleanEditors) {
         if (!this.editorService.closeEditor(var2)) {
            this.editorService.openEditor(var2);
            break;
         }

         this.editorsThatWereClosed++;
      }
   }
}
