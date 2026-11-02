package org.etlforge.application.ui.window.editor;

import javax.swing.Action;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

public final class NoUndoService implements UndoService {
   @Override
   public UndoService.Activity getUndoActivity() {
      return new NoUndoService.NoActivity() {
         @Override
         public void invoke() {
            throw new CannotUndoException();
         }
      };
   }

   @Override
   public UndoService.Activity getRedoActivity() {
      return new NoUndoService.NoActivity() {
         @Override
         public void invoke() {
            throw new CannotRedoException();
         }
      };
   }

   @Override
   public void discardAllEdits() {
   }

   @Override
   public void addListener(UndoServiceListener var1) {
   }

   @Override
   public void removeListener(UndoServiceListener var1) {
   }

   private abstract static class NoActivity implements UndoService.Activity {
      private NoActivity() {
      }

      @Override
      public boolean isAvailable() {
         return false;
      }

      @Override
      public String getRepresentationName() {
         return null;
      }

      @Override
      public Action asAction() {
         return null;
      }
   }
}
