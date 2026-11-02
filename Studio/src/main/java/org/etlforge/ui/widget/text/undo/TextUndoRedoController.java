package org.etlforge.ui.widget.text.undo;

import java.awt.Component;
import java.awt.Container;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.text.JTextComponent;
import org.etlforge.ui.action.ActionContainer;
import org.etlforge.ui.action.ActionDecorator;
import org.etlforge.ui.undo.RedoRequestFilter;
import org.etlforge.ui.undo.UndoRedoController;
import org.etlforge.ui.undo.UndoRedoControllerBase;
import org.etlforge.ui.undo.UndoRequestFilter;

public final class TextUndoRedoController implements UndoRedoController {
   private UndoRedoControllerBase delegate;

   public TextUndoRedoController(JComponent var1) {
      this.init();
      this.install(var1);
   }

   public TextUndoRedoController(JTextComponent var1) {
      this(new JTextComponent[]{var1}, var1.getInputMap(0), var1.getActionMap());
   }

   public TextUndoRedoController(JTextComponent[] var1, InputMap var2, ActionMap var3) {
      this.init();
      this.install(var1, var2, var3);
   }

   private void init() {
      this.delegate = new UndoRedoControllerBase();
      this.delegate.addUndoRequestFilter(new UndoRequestFilter() {
         @Override
         public boolean preUndo() {
            UndoableEditTimer.getTimer().sendNow();
            return true;
         }
      });
      this.delegate.addRedoRequestFilter(new RedoRequestFilter() {
         @Override
         public boolean preRedo() {
            UndoableEditTimer.getTimer().sendNow();
            return true;
         }
      });
   }

   private void install(JComponent var1) {
      this.delegate.installKeyBindings(var1, 1);
      this.registerTextComponents(var1);
   }

   private void install(JTextComponent[] var1, InputMap var2, ActionMap var3) {
      this.delegate.installKeyBindings(var2, var3);
      this.registerTextComponents(var1);
   }

   @Override
   public boolean canRedo() {
      return this.delegate.canRedo();
   }

   @Override
   public boolean canUndo() {
      return this.delegate.canUndo();
   }

   @Override
   public void dispose() {
      this.delegate.dispose();
   }

   @Override
   public void redo() {
      this.delegate.redo();
   }

   @Override
   public void undo() {
      this.delegate.undo();
   }

   @Override
   public void alwaysConsumeKeyboardTriggers() {
      this.delegate.alwaysConsumeKeyboardTriggers();
   }

   @Override
   public Action getRedoAction() {
      return this.delegate.getRedoAction();
   }

   @Override
   public Action getUndoAction() {
      return this.delegate.getUndoAction();
   }

   public void addTo(ActionContainer var1) {
      this.delegate.addTo(var1);
   }

   public void removeFrom(ActionContainer var1) {
      this.delegate.removeFrom(var1);
   }

   public void decorateUndoAction(ActionDecorator var1) {
      this.delegate.decorateUndoAction(var1);
   }

   public void decorateRedoAction(ActionDecorator var1) {
      this.delegate.decorateRedoAction(var1);
   }

   private void registerTextComponents(Container var1) {
      for (Component var5 : var1.getComponents()) {
         if (var5 instanceof JTextComponent) {
            this.registerTextComponent((JTextComponent)var5);
         } else if (var5 instanceof Container) {
            this.registerTextComponents((Container)var5);
         }
      }
   }

   private void registerTextComponents(JTextComponent[] var1) {
      for (JTextComponent var5 : var1) {
         this.registerTextComponent(var5);
      }
   }

   private void registerTextComponent(JTextComponent var1) {
      UndoableTextEditSender var2 = new UndoableTextEditSender(var1);
      this.delegate.addEditSender(var2);
   }
}
