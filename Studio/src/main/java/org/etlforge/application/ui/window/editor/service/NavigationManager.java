package org.etlforge.application.ui.window.editor.service;

import java.util.Iterator;
import java.util.LinkedList;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorServiceAdapter;
import org.etlforge.application.ui.window.editor.EditorServiceEvent;

class NavigationManager extends EditorServiceAdapter {
   private final DefaultEditorService editorService;
   private final LinkedList<Editor> visitedOrder;

   NavigationManager(DefaultEditorService var1) {
      var1.addEditorServiceListener(this);
      this.editorService = var1;
      this.visitedOrder = new LinkedList<>();
   }

   @Override
   public void activeEditorChanged(EditorServiceEvent var1) {
      if (this.editorService.getNumberOfEditors() == 0) {
         this.visitedOrder.clear();
      } else {
         Editor var2 = this.editorService.getActiveEditor();
         if (var2 == null) {
            return;
         }

         Iterator var3 = this.visitedOrder.iterator();

         while (var3.hasNext()) {
            if (var3.next() == var2) {
               var3.remove();
               break;
            }
         }

         this.visitedOrder.addFirst(var2);
      }
   }

   @Override
   public void editorClosed(EditorServiceEvent var1) {
      this.visitedOrder.remove(var1.getEditor());
   }

   @Override
   public void editorOpened(EditorServiceEvent var1) {
   }

   Editor[] getCurrentOrder() {
      return this.visitedOrder.toArray(new Editor[this.visitedOrder.size()]);
   }

   Editor getEditorToSelectWhenActivePageClosed(Editor var1) {
      for (Editor var3 : this.visitedOrder) {
         if (var3 != var1) {
            return var3;
         }
      }

      return null;
   }
}
