package org.etlforge.ui.widget.table;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.table.TableModel;
import javax.swing.undo.UndoManager;
import org.etlforge.ui.util.KeyBindingInstaller;
import org.etlforge.ui.util.KeyUtils;

public final class TableUndoRedoService {
   private final UndoManager undoManager;
   private final TableUndoRedoService.UndoAction undoAction;
   private final TableUndoRedoService.RedoAction redoAction;
   private final Map<KongaTable, KeyBindingInstaller> keyBindingsMap;
   private final UndoableEditListener editListener;

   public TableUndoRedoService(UndoManager var1) {
      this.undoManager = var1;
      this.undoAction = new TableUndoRedoService.UndoAction();
      this.redoAction = new TableUndoRedoService.RedoAction();
      this.keyBindingsMap = Maps.newHashMap();
      this.editListener = new TableUndoRedoService.Listener();
   }

   public TableUndoRedoService() {
      this(new UndoManager());
   }

   public void install(KongaTable var1, boolean var2) {
      TableModel var3 = var1.getModel();
      Preconditions.checkArgument(var3 instanceof AbstractKongaTableModel, "Only AbstractKongaTableModels are supported");
      this.install(var1, (AbstractKongaTableModel)var3, var2);
   }

   private void install(KongaTable var1, AbstractKongaTableModel var2, boolean var3) {
      var2.setUndoSupported(true);
      var2.addUndoableEditListener(this.editListener);
      if (var3) {
         this.installKeyBindings(var1);
         var1.addPropertyChangeListener("editing", new TableUndoRedoService.KeyBindingsUpdater());
      }
   }

   private void installKeyBindings(KongaTable var1) {
      KeyBindingInstaller var2 = new KeyBindingInstaller(var1);
      var2.addBinding(null, "undo", this.undoAction);
      var2.addBinding(null, "redo", this.redoAction);
      var2.install();
      this.keyBindingsMap.put(var1, var2);
   }

   private void updateActionStates() {
      this.undoAction.updateEnabledState();
      this.redoAction.updateEnabledState();
   }

   private class KeyBindingsUpdater implements PropertyChangeListener {
      private KeyBindingsUpdater() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         assert "editing".equals(var1.getPropertyName());
         boolean var2 = (Boolean)var1.getNewValue();
         KongaTable var3 = (KongaTable)var1.getSource();
         if (var2) {
            this.removeKeyBindings(var3);
         } else {
            TableUndoRedoService.this.installKeyBindings(var3);
         }
      }

      private void removeKeyBindings(KongaTable var1) {
         KeyBindingInstaller var2 = TableUndoRedoService.this.keyBindingsMap.remove(var1);
         if (var2 != null) {
            var2.remove();
         }
      }
   }

   private class Listener implements UndoableEditListener {
      private Listener() {
      }

      @Override
      public void undoableEditHappened(UndoableEditEvent var1) {
         TableUndoRedoService.this.undoManager.undoableEditHappened(var1);
         TableUndoRedoService.this.updateActionStates();
      }
   }

   private class RedoAction extends AbstractAction {
      public RedoAction() {
         super("Redo");
         this.putValue("AcceleratorKey", KeyUtils.REDO);
         this.updateEnabledState();
      }

      public final void updateEnabledState() {
         boolean var1 = TableUndoRedoService.this.undoManager.canRedo();
         this.setEnabled(var1);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         TableUndoRedoService.this.undoManager.redo();
         TableUndoRedoService.this.updateActionStates();
      }
   }

   private class UndoAction extends AbstractAction {
      public UndoAction() {
         super("Undo");
         this.putValue("AcceleratorKey", KeyUtils.UNDO);
         this.updateEnabledState();
      }

      public final void updateEnabledState() {
         boolean var1 = TableUndoRedoService.this.undoManager.canUndo();
         this.setEnabled(var1);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         TableUndoRedoService.this.undoManager.undo();
         TableUndoRedoService.this.updateActionStates();
      }
   }
}
