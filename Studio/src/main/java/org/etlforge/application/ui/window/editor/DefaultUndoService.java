package org.etlforge.application.ui.window.editor;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import org.etlforge.application.ui.action.ActionDictionary;
import org.etlforge.application.ui.window.action.WindowActionDictionary;

public class DefaultUndoService implements UndoService {
   private final UndoManager delegate;
   private final List<UndoServiceListener> listeners = Lists.newCopyOnWriteArrayList();
   private final DefaultUndoService.UndoActivity undoActivity;
   private final DefaultUndoService.RedoActivity redoActivity;

   public DefaultUndoService(UndoManager var1) {
      this.delegate = Preconditions.checkNotNull(var1, "delegate");
      this.undoActivity = new DefaultUndoService.UndoActivity();
      this.redoActivity = new DefaultUndoService.RedoActivity();
   }

   @Override
   public void discardAllEdits() {
      this.delegate.discardAllEdits();
   }

   @Override
   public UndoService.Activity getUndoActivity() {
      return this.undoActivity;
   }

   @Override
   public UndoService.Activity getRedoActivity() {
      return this.redoActivity;
   }

   public boolean canRedo() {
      return this.delegate.canRedo();
   }

   public boolean canUndo() {
      return this.delegate.canUndo();
   }

   public String getRedoRepresentationName() {
      return this.delegate.getRedoPresentationName();
   }

   public String getUndoRepresentationName() {
      return this.delegate.getUndoPresentationName();
   }

   public void redoEdit() throws CannotRedoException {
      if (this.canRedo()) {
         this.delegate.redo();
         this.fireStateChanged();
      }
   }

   public void undoEdit() throws CannotUndoException {
      if (this.canUndo()) {
         this.delegate.undo();
         this.fireStateChanged();
      }
   }

   @Override
   public void addListener(UndoServiceListener var1) {
      this.listeners.add(var1);
   }

   @Override
   public void removeListener(UndoServiceListener var1) {
      this.listeners.remove(var1);
   }

   protected final void fireStateChanged() {
      this.undoActivity.update();
      this.redoActivity.update();

      for (UndoServiceListener var2 : this.listeners) {
         var2.undoStateChanged();
      }
   }

   private abstract class ActionBase extends AbstractAction {
      private final String id;

      public ActionBase(String var2) {
         this.id = var2;
         ActionDictionary var3 = WindowActionDictionary.DICTIONARY;
         this.putValue("AcceleratorKey", var3.getAccelerator(var2));
         this.putValue("SmallIcon", var3.getSmallIcon(var2));
         this.putValue("KongaMediumIconKey", var3.getMediumIcon(var2));
         this.putValue("SwingLargeIconKey", var3.getLargeIcon(var2));
      }

      protected final void updateNameAndToolTip(String var1) {
         String var2;
         String var3;
         if (var1 == null) {
            ActionDictionary var4 = WindowActionDictionary.DICTIONARY;
            var2 = var4.getName(this.id).getText();
            var3 = var4.getToolTip(this.id);
         } else {
            var2 = var1;
            var3 = var1;
         }

         this.putValue("Name", var2);
         this.putValue("ShortDescription", var3);
      }
   }

   private class RedoAction extends DefaultUndoService.ActionBase {
      public RedoAction() {
         super("action.Redo");
         this.update();
      }

      public void update() {
         this.setEnabled(DefaultUndoService.this.canRedo());
         String var1 = DefaultUndoService.this.getRedoRepresentationName();
         this.updateNameAndToolTip(var1);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (DefaultUndoService.this.canRedo()) {
            DefaultUndoService.this.redoEdit();
         }
      }
   }

   private class RedoActivity implements UndoService.Activity {
      private final DefaultUndoService.RedoAction action = DefaultUndoService.this.new RedoAction();

      private RedoActivity() {
      }

      @Override
      public boolean isAvailable() {
         return DefaultUndoService.this.canRedo();
      }

      @Override
      public void invoke() {
         DefaultUndoService.this.redoEdit();
      }

      @Override
      public String getRepresentationName() {
         return DefaultUndoService.this.delegate.getRedoPresentationName();
      }

      @Override
      public Action asAction() {
         return this.action;
      }

      public void update() {
         this.action.update();
      }
   }

   private class UndoAction extends DefaultUndoService.ActionBase {
      public UndoAction() {
         super("action.Undo");
         this.update();
      }

      public void update() {
         this.setEnabled(DefaultUndoService.this.canUndo());
         String var1 = DefaultUndoService.this.getUndoRepresentationName();
         this.updateNameAndToolTip(var1);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (DefaultUndoService.this.canUndo()) {
            DefaultUndoService.this.undoEdit();
         }
      }
   }

   private class UndoActivity implements UndoService.Activity {
      private final DefaultUndoService.UndoAction action = DefaultUndoService.this.new UndoAction();

      private UndoActivity() {
      }

      @Override
      public boolean isAvailable() {
         return DefaultUndoService.this.canUndo();
      }

      @Override
      public void invoke() {
         DefaultUndoService.this.undoEdit();
      }

      @Override
      public String getRepresentationName() {
         return DefaultUndoService.this.delegate.getUndoPresentationName();
      }

      @Override
      public Action asAction() {
         return this.action;
      }

      public void update() {
         this.action.update();
      }
   }
}
