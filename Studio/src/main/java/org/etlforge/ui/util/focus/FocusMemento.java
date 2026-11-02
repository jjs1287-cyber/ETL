package org.etlforge.ui.util.focus;

import com.google.common.base.Preconditions;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.KeyboardFocusManager;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.SwingUtilities;
import org.etlforge.system.Disposable;

public final class FocusMemento implements Disposable {
   private final Container root;
   private final KeyboardFocusManager keyboardFocusManager;
   private final PropertyChangeListener listener;
   private Component lastFocused;

   public FocusMemento(Container var1) {
      Preconditions.checkNotNull(var1, "root");
      this.root = var1;
      this.keyboardFocusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
      this.listener = new FocusMemento.Listener();
      this.keyboardFocusManager.addPropertyChangeListener("permanentFocusOwner", this.listener);
      this.checkCurrentFocusOwner();
   }

   public Component getLastFocusedComponent() {
      this.checkIfRemoved();
      return this.lastFocused;
   }

   @Override
   public void dispose() {
      this.keyboardFocusManager.removePropertyChangeListener("permanentFocusOwner", this.listener);
      this.lastFocused = null;
   }

   public void restore(int var1) {
      final Component var2 = this.getLastFocusedComponent();
      if (var2 != null) {
         if (var1 <= 0) {
            EventQueue.invokeLater(new Runnable() {
               @Override
               public void run() {
                  var2.requestFocusInWindow();
               }
            });
         } else {
            FocusUtils.requestFocusLater(var2, var1);
         }
      }
   }

   private void checkCurrentFocusOwner() {
      Component var1 = this.keyboardFocusManager.getPermanentFocusOwner();
      if (this.isValid(var1)) {
         this.lastFocused = var1;
      }
   }

   private void checkIfRemoved() {
      if (!this.isValid(this.lastFocused)) {
         this.lastFocused = null;
      }
   }

   private boolean isValid(Component var1) {
      return var1 != null && SwingUtilities.isDescendingFrom(var1, this.root);
   }

   private final class Listener implements PropertyChangeListener {
      private Listener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         Object var2 = var1.getNewValue();
         if (var2 instanceof Component) {
            this.handleNewFocusOwner((Component)var2);
         }
      }

      private void handleNewFocusOwner(Component var1) {
         if (FocusMemento.this.isValid(var1)) {
            FocusMemento.this.lastFocused = var1;
         }
      }
   }
}
