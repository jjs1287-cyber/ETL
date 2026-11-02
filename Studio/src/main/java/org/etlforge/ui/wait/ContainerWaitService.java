package org.etlforge.ui.wait;

import com.google.common.collect.Maps;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.util.Map;
import org.etlforge.system.callback.Acceptor;
import org.etlforge.ui.widget.ComponentTraverser;
import org.etlforge.ui.widget.ComponentTreeInputEventBlocker;

public final class ContainerWaitService extends AbstractWaitService implements WaitService {
   private final Container container;
   private final ComponentTreeInputEventBlocker eventBlocker;
   private ContainerWaitService.ContainerState preWaitState;

   public ContainerWaitService(Container var1) {
      this.container = var1;
      this.eventBlocker = new ComponentTreeInputEventBlocker(var1);
   }

   @Override
   protected void goToWaitingState() {
      this.preWaitState = new ContainerWaitService.ContainerState(this.container);
      this.eventBlocker.install();
      ComponentTraverser.traverse(this.container, new Acceptor<Component>() {
         public boolean accept(Component var1) {
            var1.setCursor(Cursor.getPredefinedCursor(3));
            return true;
         }
      });
   }

   @Override
   protected void stopWaiting() {
      if (this.preWaitState != null) {
         this.eventBlocker.uninstall();
         this.preWaitState.restore();
         this.preWaitState = null;
      }
   }

   private static class ComponentState {
      private final Component component;
      private final Cursor cursor;

      public ComponentState(Component var1) {
         this.component = var1;
         this.cursor = var1.getCursor();
      }

      public void restore() {
         this.component.setCursor(this.cursor);
      }
   }

   private static class ContainerState {
      private final Container container;
      private final Map<Component, ContainerWaitService.ComponentState> components;

      public ContainerState(Container var1) {
         this.container = var1;
         this.components = Maps.newIdentityHashMap();
         ComponentTraverser.traverse(var1, new Acceptor<Component>() {
            public boolean accept(Component var1) {
               ContainerState.this.components.put(var1, new ContainerWaitService.ComponentState(var1));
               return true;
            }
         });
      }

      public void restore() {
         ComponentTraverser.traverse(this.container, new Acceptor<Component>() {
            public boolean accept(Component var1) {
               ContainerWaitService.ComponentState var2 = ContainerState.this.components.get(var1);
               if (var2 != null) {
                  var2.restore();
               }

               return true;
            }
         });
      }
   }
}
