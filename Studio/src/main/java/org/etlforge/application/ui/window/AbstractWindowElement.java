package org.etlforge.application.ui.window;

import com.google.common.base.Preconditions;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.ApplicationUiUtils;
import org.etlforge.application.ui.window.event.WindowElementStateEvent;
import org.etlforge.application.ui.window.event.WindowElementStateListener;
import org.etlforge.application.ui.window.event.WindowElementStateListeners;

public abstract class AbstractWindowElement implements WindowElement {
   private final String id;
   private ApplicationWindow window;
   private EnumSet<WindowElementState> currentState;
   private WindowElementStateListener internalStateListener;
   private final WindowElementStateListeners stateListeners;
   private List<WindowElementResource> resources;

   protected AbstractWindowElement(String var1) {
      Preconditions.checkNotNull(var1, "id");
      this.id = var1;
      this.currentState = EnumSet.noneOf(WindowElementState.class);
      this.stateListeners = new WindowElementStateListeners();
   }

   @Override
   public final String getId() {
      return this.id;
   }

   @Override
   public final ApplicationWindow getWindow() {
      return this.window;
   }

   @Override
   public final EnumSet<WindowElementState> getState() {
      return this.currentState.clone();
   }

   @Override
   public final void appendState(WindowElementState var1) {
      if (var1 != null && !this.currentState.contains(var1)) {
         ApplicationUiUtils.logIfNotOnEDT(this.getClass(), "appendState");
         if (this.window == null && var1 != WindowElementState.CLOSED) {
            ErrorLog.internal(
               this.getClass(),
               null,
               new Exception(
                  "Attempt to set state "
                     + var1
                     + " on a window element (id="
                     + this.id
                     + ") that has not been added to, or has already been removed from, the application window."
               )
            );
         } else {
            EnumSet var2 = this.currentState;
            this.currentState = this.currentState.clone();
            this.currentState.add(var1);
            this.currentState.remove(var1.getOppositeState());
            this.fireStateChanged(var2, this.currentState.clone());
         }
      }
   }

   @Override
   public final void revokeState(WindowElementState var1) {
      ApplicationUiUtils.logIfNotOnEDT(this.getClass(), "revokeState");
      if (var1 != null && this.currentState.contains(var1)) {
         EnumSet var2 = this.currentState;
         this.currentState = this.currentState.clone();
         this.currentState.remove(var1);
         this.currentState.add(var1.getOppositeState());
         this.fireStateChanged(var2, this.currentState.clone());
      }
   }

   @Override
   public void addToWindow(ApplicationWindow var1) {
      Preconditions.checkNotNull(var1, "window");
      Preconditions.checkState(this.window == null, "This WindowElement has already been added to a window");
      ApplicationUiUtils.logIfNotOnEDT(this.getClass(), "addToWindow");
      this.window = var1;
      if (this.internalStateListener == null) {
         this.internalStateListener = new AbstractWindowElement.StateListenerImpl();
      }

      this.addStateListener(this.internalStateListener);
   }

   @Override
   public boolean removeFromWindow(boolean var1) {
      ApplicationUiUtils.logIfNotOnEDT(this.getClass(), "removeFromWindow");
      this.revokeState(WindowElementState.ACTIVE);
      this.window = null;
      return true;
   }

   @Override
   public final void addStateListener(WindowElementStateListener var1) {
      this.stateListeners.add(var1);
   }

   @Override
   public final void removeStateListener(WindowElementStateListener var1) {
      this.stateListeners.remove(var1);
   }

   @Override
   public final void addResource(WindowElementResource var1) {
      Preconditions.checkNotNull(var1, "r");
      if (this.resources == null) {
         this.resources = new CopyOnWriteArrayList<>();
      }

      this.resources.add(var1);
      if (var1.getLifeSpan().isAlive(this.getState())) {
         var1.create(this);
      }
   }

   @Override
   public final void removeResource(WindowElementResource var1) {
      if (var1 != null && this.resources != null) {
         if (this.resources.remove(var1)) {
            var1.dispose(this);
            if (this.resources.isEmpty()) {
               this.resources = null;
            }
         }
      }
   }

   protected void opened() {
   }

   protected void closed() {
      this.removeStateListener(this.internalStateListener);
   }

   protected void activated() {
   }

   protected void deactivated() {
   }

   protected void attached() {
   }

   protected void detached() {
   }

   protected final boolean isAddedToWindow() {
      return this.window != null;
   }

   @Override
   public String toString() {
      return this.getClass().getSimpleName() + " - ID = " + this.getId();
   }

   private void fireStateChanged(EnumSet<WindowElementState> var1, EnumSet<WindowElementState> var2) {
      WindowElementStateEvent var3 = new WindowElementStateEvent(this, var1, var2);
      this.stateListeners.call().stateChanged(var3);
   }

   private void updateResources(WindowElementStateEvent var1) {
      if (this.resources != null) {
         for (WindowElementResource var3 : this.resources) {
            if (var3.getLifeSpan().isEntering(var1)) {
               var3.create(this);
            } else if (var3.getLifeSpan().isLeaving(var1)) {
               var3.dispose(this);
            }
         }
      }
   }

   private class StateListenerImpl implements WindowElementStateListener {
      private StateListenerImpl() {
      }

      @Override
      public void stateChanged(WindowElementStateEvent var1) {
         AbstractWindowElement.this.updateResources(var1);
         if (WindowElementState.OPEN.wasAdded(var1)) {
            AbstractWindowElement.this.opened();
         } else if (WindowElementState.CLOSED.wasAdded(var1)) {
            AbstractWindowElement.this.closed();
         }

         if (WindowElementState.ACTIVE.wasAdded(var1)) {
            AbstractWindowElement.this.activated();
         } else if (WindowElementState.INACTIVE.wasAdded(var1)) {
            AbstractWindowElement.this.deactivated();
         }

         if (WindowElementState.ATTACHED.wasAdded(var1)) {
            AbstractWindowElement.this.attached();
         } else if (WindowElementState.DETACHED.wasAdded(var1)) {
            AbstractWindowElement.this.detached();
         }
      }
   }
}
