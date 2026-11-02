package org.etlforge.ui.input;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.etlforge.ui.event.UndoableEditListeners;
import org.etlforge.util.Property;

public final class PropertyChangeSource<T> implements ChangeSource {
   private final Property<T> property;
   private boolean sendChangeEvents;
   private boolean sendUndoEvents;
   private final InputChangeListeners changeListeners = new InputChangeListeners();
   private final UndoableEditListeners undoListeners = new UndoableEditListeners();
   private final PropertyChangeSource<T>.PropertyListener propertyListener = new PropertyChangeSource.PropertyListener();

   public static <E> PropertyChangeSource<E> of(Property<E> var0) {
      return new PropertyChangeSource(var0);
   }

   public PropertyChangeSource(Property<T> var1) {
      this.property = var1;
      this.property.addListener(this.propertyListener);
   }

   public void dispose() {
      this.property.removeListener(this.propertyListener);
   }

   @Override
   public void addInputChangeListener(InputChangeListener var1) {
      this.changeListeners.add(var1);
   }

   @Override
   public void removeInputChangeListener(InputChangeListener var1) {
      this.changeListeners.remove(var1);
   }

   @Override
   public void addUndoableEditListener(UndoableEditListener var1) {
      this.undoListeners.add(var1);
   }

   @Override
   public void removeUndoableEditListener(UndoableEditListener var1) {
      this.undoListeners.remove(var1);
   }

   @Override
   public boolean isSendingInputChangeEvents() {
      return this.sendChangeEvents;
   }

   @Override
   public void setSendInputChangeEvents(boolean var1) {
      this.sendChangeEvents = var1;
   }

   @Override
   public boolean isSendingUndoEvents() {
      return this.sendUndoEvents;
   }

   @Override
   public void setSendUndoEvents(boolean var1) {
      this.sendUndoEvents = var1;
   }

   private class Edit extends AbstractUndoableEdit {
      private final T oldValue;
      private final T newValue;

      public Edit(T var2, T var3) {
         this.oldValue = (T)var2;
         this.newValue = (T)var3;
      }

      @Override
      public String getPresentationName() {
         return PropertyChangeSource.this.property.name() + " Change";
      }

      @Override
      public void undo() throws CannotUndoException {
         super.undo();
         this.setValue(this.oldValue);
      }

      private void setValue(T var1) {
         PropertyChangeSource.this.propertyListener.internalUndoEvent = true;
         PropertyChangeSource.this.property.set((T)var1);
         PropertyChangeSource.this.propertyListener.internalUndoEvent = false;
      }

      @Override
      public void redo() throws CannotRedoException {
         super.redo();
         this.setValue(this.newValue);
      }
   }

   private class PropertyListener implements PropertyChangeListener {
      private boolean internalUndoEvent;

      private PropertyListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         if (PropertyChangeSource.this.sendChangeEvents) {
            PropertyChangeSource.this.changeListeners
               .call()
               .inputChanged(new InputChangeEvent(PropertyChangeSource.this, PropertyChangeSource.this.property.name()));
         }

         if (PropertyChangeSource.this.sendUndoEvents && !this.internalUndoEvent) {
            this.sendUndoableEdit(var1);
         }
      }

      private void sendUndoableEdit(PropertyChangeEvent var1) {
         Object var2 = var1.getOldValue();
         Object var3 = var1.getNewValue();
         PropertyChangeSource<T>.Edit var4 = PropertyChangeSource.this.new Edit((T)var2, (T)var3);
         PropertyChangeSource.this.undoListeners.call().undoableEditHappened(new UndoableEditEvent(PropertyChangeSource.this, var4));
      }
   }
}
