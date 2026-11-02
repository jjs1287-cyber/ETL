package org.etlforge.application.ui.window.editor;

import java.awt.event.ActionEvent;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.AbstractAction;
import javax.swing.JPopupMenu;

public abstract class AbstractEditorSelector {
   private final List<EditorSelectorListener> listeners = new CopyOnWriteArrayList<>();

   public final void addSelectorListener(EditorSelectorListener var1) {
      this.listeners.add(var1);
   }

   public final void removeSelectorListener(EditorSelectorListener var1) {
      this.listeners.remove(var1);
   }

   protected final void fireFinalSelection(Editor var1) {
      for (EditorSelectorListener var3 : this.listeners) {
         var3.finalSelection(var1);
      }
   }

   protected final void fireTemporarySelection(Editor var1) {
      for (EditorSelectorListener var3 : this.listeners) {
         var3.temporarySelection(var1);
      }
   }

   protected final JPopupMenu createContextMenu(final Editor var1, boolean var2) {
      JPopupMenu var3 = new JPopupMenu();
      if (var2) {
         AbstractAction var4 = new AbstractAction("Close") {
            @Override
            public void actionPerformed(ActionEvent var1x) {
               for (EditorSelectorListener var3x : AbstractEditorSelector.this.listeners) {
                  var3x.closeRequested(var1);
               }
            }
         };
         var3.add(var4);
      }

      AbstractAction var5 = new AbstractAction("Close Others") {
         @Override
         public void actionPerformed(ActionEvent var1x) {
            for (EditorSelectorListener var3 : AbstractEditorSelector.this.listeners) {
               var3.closeOthersRequested(var1);
            }
         }
      };
      var3.add(var5);
      return var3;
   }
}
