package org.etlforge.application.ui.window.editor.service;

import java.awt.Component;
import java.lang.ref.WeakReference;
import org.etlforge.application.ui.window.editor.Editor;

abstract class TabNotification {
   private final EditorServiceUi ui;
   private final WeakReference<Editor> editorRef;
   private ComponentNotification worker;

   public TabNotification(EditorServiceUi var1, Editor var2) {
      this.ui = var1;
      this.editorRef = new WeakReference<>(var2);
      Component var3 = this.getTabComponent(var2);
      if (var3 == null) {
         this.worker = null;
      } else {
         this.worker = new TabNotification.Worker(var3);
         this.worker.start();
      }
   }

   protected abstract Component getTabComponent(Editor var1);

   public final void stop() {
      if (this.worker != null) {
         this.worker.stop();
         this.worker = null;
      }
   }

   private final class Worker extends ComponentNotification {
      private Worker(Component var2) {
         super(var2);
      }

      @Override
      protected boolean isCanceled() {
         Editor var1 = TabNotification.this.editorRef.get();
         return var1 == null || TabNotification.this.ui.getActiveEditor() == var1 || !TabNotification.this.ui.isDisplayingEditor(var1);
      }
   }
}
