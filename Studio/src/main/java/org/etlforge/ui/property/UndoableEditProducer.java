package org.etlforge.ui.property;

import com.google.common.collect.Sets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.etlforge.ui.event.UndoableEditListeners;
import org.etlforge.ui.undo.UndoableEditSender;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.Property;

public final class UndoableEditProducer implements PropertyChangeListener, UndoableEditSender {
   private final UndoableEditListeners listeners = new UndoableEditListeners();
   private boolean blocked;
   private final Set<String> propertiesToIgnore = Sets.newHashSet();

   public void ignoreProperty(String var1) {
      this.propertiesToIgnore.add(var1);
   }

   @Override
   public void propertyChange(PropertyChangeEvent var1) {
      if (this.isProcessingRequired(var1)) {
         UndoableEditProducer.Edit var2 = new UndoableEditProducer.Edit(var1);
         this.listeners.call().undoableEditHappened(new UndoableEditEvent(this, var2));
      }
   }

   private boolean isProcessingRequired(PropertyChangeEvent var1) {
      if (this.blocked) {
         return false;
      }

      String var2 = var1.getPropertyName();
      boolean var3 = this.propertiesToIgnore.contains(var2);
      return !var3;
   }

   @Override
   public void dispose() {
      this.listeners.removeAll();
   }

   @Override
   public void addUndoableEditListener(UndoableEditListener var1) {
      this.listeners.add(var1);
   }

   @Override
   public void removeUndoableEditListener(UndoableEditListener var1) {
      this.listeners.remove(var1);
   }

   private class Edit extends AbstractUndoableEdit {
      private final Property property;
      private final Object oldValue;
      private final Object newValue;

      public Edit(PropertyChangeEvent var2) {
         AbstractPropertyBasedModel var3 = (AbstractPropertyBasedModel)var2.getSource();
         this.property = var3.getProperty(var2.getPropertyName());
         this.oldValue = var2.getOldValue();
         this.newValue = var2.getNewValue();
      }

      @Override
      public void undo() throws CannotUndoException {
         super.undo();
         this.set(this.oldValue);
      }

      @Override
      public void redo() throws CannotRedoException {
         super.redo();
         this.set(this.newValue);
      }

      private void set(Object var1) {
         UndoableEditProducer.this.blocked = true;
         this.property.set(var1);
         UndoableEditProducer.this.blocked = false;
      }
   }
}
