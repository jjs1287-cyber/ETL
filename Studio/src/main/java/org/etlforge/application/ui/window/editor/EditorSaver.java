package org.etlforge.application.ui.window.editor;

import com.google.common.collect.Lists;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.ui.NonBlockingAlert;
import org.etlforge.ui.widget.text.undo.UndoableEditTimer;

public final class EditorSaver {
   private final EditorService editorService;

   public EditorSaver(EditorService var1) {
      this.editorService = var1;
   }

   public EditorSaverResult saveActiveEditor() {
      return this.saveActivePageImpl();
   }

   private EditorSaverResult saveActivePageImpl() {
      EditorSaverResult var1 = new EditorSaverResult();
      Editor var2 = this.editorService.getActiveEditor();
      if (var2 != null) {
         new SingleEditorSaver(var2, var1).run();
      }

      return var1;
   }

   public EditorSaverResult saveEditor(Editor var1) {
      return this.savePageImpl(var1);
   }

   private EditorSaverResult savePageImpl(Editor var1) {
      EditorSaverResult var2 = new EditorSaverResult();

      for (Editor var6 : this.editorService.getOpenEditors()) {
         if (var6 == var1) {
            new SingleEditorSaver(var1, var2).run();
         }
      }

      return var2;
   }

   public EditorSaverResult saveAllEditors() {
      return this.saveAllPagesImpl();
   }

   private EditorSaverResult saveAllPagesImpl() {
      EditorSaverResult var1 = new EditorSaverResult();
      if (this.editorService.getNumberOfEditors() > 0) {
         new EditorSaver.AllEditorsSaver(this.editorService, var1).run();
      }

      return var1;
   }

   private static class AllEditorsSaver implements Runnable {
      private final EditorService editorService;
      private final EditorSaverResult result;
      private List<SaveEditorException> errors;

      public AllEditorsSaver(EditorService var1, EditorSaverResult var2) {
         this.editorService = var1;
         this.result = var2;
      }

      @Override
      public void run() {
         UndoableEditTimer.getTimer().sendNow();
         this.saveEditors();
         this.handleErrors();
      }

      private void saveEditors() {
         for (Editor var4 : this.editorService.getOpenEditors()) {
            this.saveEditor(var4);
         }
      }

      private void saveEditor(Editor var1) {
         try {
            var1.save();
            this.result.saved(var1);
         } catch (SaveEditorException var3) {
            this.result.failed(var3.getEditor(), var3);
            this.logError(var3);
         }
      }

      private void logError(SaveEditorException var1) {
         if (this.errors == null) {
            this.errors = Lists.newArrayList();
         }

         this.errors.add(var1);
      }

      private void handleErrors() {
         if (this.errors != null) {
            ArrayList var1 = Lists.newArrayList();
            StringBuilder var2 = new StringBuilder("Save failed for the following editors:\n");

            for (SaveEditorException var4 : this.errors) {
               Editor var5 = var4.getEditor();
               var2.append("\n").append(this.getTitle(var5)).append(": ").append(var4.getMessage());
               var1.add(var5);
            }

            Editor var6 = (Editor)var1.get(0);
            this.switchToEditor(var6);
            NonBlockingAlert.error(var6.getWindow().getFrame(), var2.toString(), "Cannot Save");
         }
      }

      private String getTitle(Editor var1) {
         String var2 = var1.getTitle();
         return StringUtils.removeEnd(var2, "*");
      }

      private void switchToEditor(final Editor var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               AllEditorsSaver.this.editorService.openEditor(var1);
            }
         });
      }
   }
}
