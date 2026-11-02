package org.etlforge.application.ui.window.editor;

import org.etlforge.util.event.AbstractListenerManager;
import org.etlforge.util.event.ListenerManager;

public class EditorServiceListeners extends AbstractListenerManager<EditorServiceListener> implements ListenerManager<EditorServiceListener> {
   protected EditorServiceListener createNotifier() {
      return new EditorServiceListener() {
         @Override
         public void activeEditorChanged(EditorServiceEvent var1) {
            for (EditorServiceListener var3 : EditorServiceListeners.this.getListeners()) {
               var3.activeEditorChanged(var1);
            }
         }

         @Override
         public void editorClosed(EditorServiceEvent var1) {
            for (EditorServiceListener var3 : EditorServiceListeners.this.getListeners()) {
               var3.editorClosed(var1);
            }
         }

         @Override
         public void editorOpened(EditorServiceEvent var1) {
            for (EditorServiceListener var3 : EditorServiceListeners.this.getListeners()) {
               var3.editorOpened(var1);
            }
         }

         @Override
         public void dirtyStateChanged(EditorServiceEvent var1) {
            for (EditorServiceListener var3 : EditorServiceListeners.this.getListeners()) {
               var3.dirtyStateChanged(var1);
            }
         }
      };
   }
}
