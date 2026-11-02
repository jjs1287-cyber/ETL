package org.etlforge.ui.widget;

import com.google.common.base.Preconditions;
import java.awt.AWTEvent;
import java.awt.Container;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import org.etlforge.ui.event.EventUtils;
import org.etlforge.ui.event.InputEventHandler;
import org.etlforge.ui.util.UiUtils;

public abstract class ComponentTreeInputEventHandler implements InputEventHandler {
   private final Container root;
   private AWTEventListener dispatcher;

   public ComponentTreeInputEventHandler(Container var1) {
      this.root = Preconditions.checkNotNull(var1);
      this.dispatcher = new ComponentTreeInputEventHandler.EventDispatcher();
   }

   public void install() {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            if (ComponentTreeInputEventHandler.this.dispatcher == null) {
               ComponentTreeInputEventHandler.this.dispatcher = ComponentTreeInputEventHandler.this.new EventDispatcher();
               Toolkit var1 = Toolkit.getDefaultToolkit();
               var1.addAWTEventListener(ComponentTreeInputEventHandler.this.dispatcher, 24L);
            }
         }
      });
   }

   public void uninstall() {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            if (ComponentTreeInputEventHandler.this.dispatcher != null) {
               Toolkit var1 = Toolkit.getDefaultToolkit();
               var1.removeAWTEventListener(ComponentTreeInputEventHandler.this.dispatcher);
               ComponentTreeInputEventHandler.this.dispatcher = null;
            }
         }
      });
   }

   @Override
   public abstract void handleKeyEvent(KeyEvent var1);

   @Override
   public abstract void handleMouseEvent(MouseEvent var1);

   private class EventDispatcher implements AWTEventListener {
      private EventDispatcher() {
      }

      @Override
      public void eventDispatched(AWTEvent var1) {
         if (this.isApplicable(var1)) {
            if (var1 instanceof MouseEvent) {
               ComponentTreeInputEventHandler.this.handleMouseEvent((MouseEvent)var1);
            } else if (var1 instanceof KeyEvent) {
               ComponentTreeInputEventHandler.this.handleKeyEvent((KeyEvent)var1);
            }
         }
      }

      private boolean isApplicable(AWTEvent var1) {
         return var1 instanceof InputEvent && EventUtils.isSourceDescendingFrom(ComponentTreeInputEventHandler.this.root, var1);
      }
   }
}
