package org.etlforge.application.ui.window.editor.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorService;
import org.etlforge.application.ui.window.editor.EditorServiceAdapter;
import org.etlforge.application.ui.window.editor.EditorServiceEvent;

final class BackwardForwardNavigator {
   private final EditorService svc;
   private final ArrayList<Editor> editors;
   private int position;
   private final BackwardForwardNavigator.Listener listener;

   public BackwardForwardNavigator(EditorService var1) {
      this.svc = var1;
      this.editors = Lists.newArrayList();
      this.position = -1;
      this.listener = new BackwardForwardNavigator.Listener();
      var1.addEditorServiceListener(this.listener);
   }

   public void setCloseEditorInProgress(boolean var1) {
      this.listener.closeEditorInProgress = var1;
   }

   public BackwardForwardStack getStack() {
      if (this.editors.isEmpty()) {
         return BackwardForwardStack.EMPTY;
      }

      ArrayList var1 = Lists.newArrayList(this.editors);
      Collections.reverse(var1);
      int var2 = this.editors.size() - this.position - 1;
      return new BackwardForwardStack(this, (Editor[]) var1.toArray(new Editor[var1.size()]), var2);
   }

   public void moveToPosition(int var1) {
      Preconditions.checkArgument(var1 >= 0 && var1 < this.editors.size(), "position out-of-bounds: " + var1 + " (size = " + this.editors.size() + ")");
      this.position = var1;
      Editor var2 = this.editors.get(var1);
      this.listener.internalChange = true;
      this.svc.openEditor(var2);
      this.listener.internalChange = false;
   }

   private class Listener extends EditorServiceAdapter {
      public boolean internalChange;
      public boolean closeEditorInProgress;
      private boolean postCloseEventScheduled;

      private Listener() {
      }

      @Override
      public void editorClosed(EditorServiceEvent var1) {
         Editor var2 = var1.getEditor();
         this.forgetEditor(var2);
         this.postClose();
      }

      private void forgetEditor(Editor var1) {
         for (int var2 = BackwardForwardNavigator.this.editors.size() - 1; var2 >= 0; var2--) {
            if (BackwardForwardNavigator.this.editors.get(var2) == var1) {
               BackwardForwardNavigator.this.editors.remove(var2);
               if (BackwardForwardNavigator.this.position > 0 && var2 <= BackwardForwardNavigator.this.position) {
                  --BackwardForwardNavigator.this.position;
               }
            }
         }

         if (BackwardForwardNavigator.this.editors.isEmpty()) {
            BackwardForwardNavigator.this.position = -1;
         } else {
            this.removeNeighbouringDuplicates();
         }
      }

      private void removeNeighbouringDuplicates() {
         Editor var1 = null;
         int var2 = 0;
         Iterator var3 = BackwardForwardNavigator.this.editors.iterator();

         while (var3.hasNext()) {
            Editor var4 = (Editor)var3.next();
            if (var4 == var1) {
               var3.remove();
               if (BackwardForwardNavigator.this.position >= var2) {
                  --BackwardForwardNavigator.this.position;
               }
            } else {
               var1 = var4;
               var2++;
            }
         }
      }

      private void postClose() {
         if (!this.postCloseEventScheduled) {
            this.postCloseEventScheduled = true;
            EventQueue.invokeLater(new Runnable() {
               @Override
               public void run() {
                  if (!BackwardForwardNavigator.this.editors.isEmpty()) {
                     Listener.this.updatePositionAfterEditorClosed();
                  }

                  Listener.this.postCloseEventScheduled = false;
               }
            });
         }
      }

      private void updatePositionAfterEditorClosed() {
         Editor var1 = BackwardForwardNavigator.this.svc.getActiveEditor();
         if (BackwardForwardNavigator.this.editors.get(BackwardForwardNavigator.this.position) != var1) {
            int var2 = this.getForwardDistanceTo(var1);
            int var3 = this.getBackwardDistanceTo(var1);
            if (var2 < 0 && var3 < 0) {
               BackwardForwardNavigator.this.moveToPosition(BackwardForwardNavigator.this.editors.size() - 1);
            } else {
               if (var2 > 0 && var3 > 0) {
                  if (var2 <= var3) {
                     BackwardForwardNavigator.this.position = BackwardForwardNavigator.this.position + var2;
                  } else {
                     BackwardForwardNavigator.this.position = BackwardForwardNavigator.this.position - var3;
                  }
               } else if (var2 > 0) {
                  BackwardForwardNavigator.this.position = BackwardForwardNavigator.this.position + var2;
               } else if (var3 > 0) {
                  BackwardForwardNavigator.this.position = BackwardForwardNavigator.this.position - var3;
               }

               assert BackwardForwardNavigator.this.position >= 0 && BackwardForwardNavigator.this.position < BackwardForwardNavigator.this.editors.size() : "position out of range after editor closed";
            }
         }
      }

      private int getForwardDistanceTo(Editor var1) {
         for (int var2 = BackwardForwardNavigator.this.position + 1; var2 < BackwardForwardNavigator.this.editors.size(); var2++) {
            if (BackwardForwardNavigator.this.editors.get(var2) == var1) {
               return var2 - BackwardForwardNavigator.this.position;
            }
         }

         return -1;
      }

      private int getBackwardDistanceTo(Editor var1) {
         for (int var2 = BackwardForwardNavigator.this.position - 1; var2 >= 0; var2--) {
            if (BackwardForwardNavigator.this.editors.get(var2) == var1) {
               return BackwardForwardNavigator.this.position - var2;
            }
         }

         return -1;
      }

      @Override
      public void activeEditorChanged(EditorServiceEvent var1) {
         Editor var2 = var1.getEditor();
         if (!this.internalChange && !this.closeEditorInProgress && var2 != null && var2.getWindow() != null) {
            if (this.isNewBranch(var2)) {
               this.startNewBranch(var2);
            }

            ++BackwardForwardNavigator.this.position;
            this.removeNeighbouringDuplicates();
         }
      }

      private boolean isNewBranch(Editor var1) {
         if (BackwardForwardNavigator.this.position >= BackwardForwardNavigator.this.editors.size() - 1) {
            return true;
         }

         Editor var2 = BackwardForwardNavigator.this.editors.get(BackwardForwardNavigator.this.position + 1);
         return var2 != var1;
      }

      private void startNewBranch(Editor var1) {
         for (int var2 = BackwardForwardNavigator.this.editors.size() - 1; var2 > BackwardForwardNavigator.this.position; var2--) {
            BackwardForwardNavigator.this.editors.remove(var2);
         }

         BackwardForwardNavigator.this.editors.add(var1);
      }
   }
}
