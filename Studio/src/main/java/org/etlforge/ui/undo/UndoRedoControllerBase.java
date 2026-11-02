package org.etlforge.ui.undo;

import com.google.common.collect.Lists;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;
import org.etlforge.ui.action.ActionContainer;
import org.etlforge.ui.action.ActionDecorator;
import org.etlforge.ui.resources.UiResourceBundle;
import org.etlforge.ui.util.KeyBindingInstaller;
import org.etlforge.ui.util.KeyUtils;

public final class UndoRedoControllerBase implements UndoRedoController {
   private static final String UNDO_ACTION_KEY = "undo";
   private static final String REDO_ACTION_KEY = "redo";
   private final UndoManager manager = new UndoManager();
   private final List<UndoableEditSender> managedComponents = Lists.newArrayList();
   private KeyBindingInstaller keyBindingInstaller;
   private InputMap inputMap;
   private ActionMap actionMap;
   private final Action undoAction = new UndoRedoControllerBase.UndoAction();
   private final Action redoAction = new UndoRedoControllerBase.RedoAction();
   private final List<UndoRequestFilter> undoFilters = Lists.newArrayList();
   private final List<RedoRequestFilter> redoFilters = Lists.newArrayList();
   private final UndoableEditListener internalEditListener = new UndoableEditListener() {
      @Override
      public void undoableEditHappened(UndoableEditEvent var1) {
         UndoRedoControllerBase.this.manager.undoableEditHappened(var1);
         UndoRedoControllerBase.this.updateActions();
      }
   };
   private boolean alwaysConsumeKeyboardTriggers;

   public UndoRedoControllerBase() {
      this.decorateUndoAction(UndoActionDecorator.INSTANCE);
      this.decorateRedoAction(RedoActionDecorator.INSTANCE);
      this.updateActions();
   }

   @Override
   public boolean canRedo() {
      return this.manager.canRedo();
   }

   @Override
   public boolean canUndo() {
      return this.manager.canUndo();
   }

   @Override
   public void dispose() {
      for (UndoableEditSender var2 : this.managedComponents) {
         var2.removeUndoableEditListener(this.internalEditListener);
         var2.dispose();
      }

      if (this.inputMap != null) {
         this.inputMap.remove(KeyUtils.UNDO);
         this.inputMap.remove(KeyUtils.REDO);
      }

      if (this.actionMap != null) {
         this.actionMap.remove("undo");
         this.actionMap.remove("redo");
      }

      if (this.keyBindingInstaller != null) {
         this.keyBindingInstaller.remove();
      }

      this.undoFilters.clear();
      this.redoFilters.clear();
   }

   @Override
   public void redo() {
      for (RedoRequestFilter var2 : this.redoFilters) {
         if (!var2.preRedo()) {
            return;
         }
      }

      if (this.manager.canRedo()) {
         this.manager.redo();
      }

      this.updateActions();
   }

   @Override
   public void undo() {
      for (UndoRequestFilter var2 : this.undoFilters) {
         if (!var2.preUndo()) {
            return;
         }
      }

      if (this.manager.canUndo()) {
         this.manager.undo();
      }

      this.updateActions();
   }

   public void addTo(ActionContainer var1) {
      var1.add(this.undoAction);
      var1.add(this.redoAction);
   }

   public void removeFrom(ActionContainer var1) {
      var1.remove(this.undoAction);
      var1.remove(this.redoAction);
   }

   public void decorateUndoAction(ActionDecorator var1) {
      var1.decorate(this.undoAction);
   }

   public void decorateRedoAction(ActionDecorator var1) {
      var1.decorate(this.redoAction);
   }

   public void addEditSender(UndoableEditSender var1) {
      var1.addUndoableEditListener(this.internalEditListener);
      this.managedComponents.add(var1);
   }

   @Override
   public Action getUndoAction() {
      return this.undoAction;
   }

   @Override
   public Action getRedoAction() {
      return this.redoAction;
   }

   @Override
   public void alwaysConsumeKeyboardTriggers() {
      this.alwaysConsumeKeyboardTriggers = true;
      this.updateActions();
   }

   public void updateActions() {
      this.undoAction.setEnabled(this.alwaysConsumeKeyboardTriggers || this.canUndo());
      this.redoAction.setEnabled(this.alwaysConsumeKeyboardTriggers || this.canRedo());
   }

   public void installKeyBindings(JComponent var1, int var2) {
      this.keyBindingInstaller = new KeyBindingInstaller(var1, var2);
      this.keyBindingInstaller.addBinding(KeyUtils.UNDO, "undo", this.undoAction);
      this.keyBindingInstaller.addBinding(KeyUtils.REDO, "redo", this.redoAction);
      this.keyBindingInstaller.install();
   }

   public void installKeyBindings(InputMap var1, ActionMap var2) {
      if (var1 != null && var2 != null) {
         var1.put(KeyUtils.UNDO, "undo");
         var2.put("undo", this.undoAction);
         var1.put(KeyUtils.REDO, "redo");
         var2.put("redo", this.redoAction);
      }

      this.inputMap = var1;
      this.actionMap = var2;
   }

   public void addUndoRequestFilter(UndoRequestFilter var1) {
      if (var1 != null) {
         this.undoFilters.add(var1);
      }
   }

   public void removeUndoRequestFilter(UndoRequestFilter var1) {
      this.undoFilters.remove(var1);
   }

   public void addRedoRequestFilter(RedoRequestFilter var1) {
      if (var1 != null) {
         this.redoFilters.add(var1);
      }
   }

   public void removeRedoRequestFilter(RedoRequestFilter var1) {
      this.redoFilters.remove(var1);
   }

   private final class RedoAction extends AbstractAction {
      public RedoAction() {
         super(UiResourceBundle.getString("Redo"));
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         UndoRedoControllerBase.this.redo();
      }
   }

   private final class UndoAction extends AbstractAction {
      public UndoAction() {
         super(UiResourceBundle.getString("Undo"));
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         UndoRedoControllerBase.this.undo();
      }
   }
}
