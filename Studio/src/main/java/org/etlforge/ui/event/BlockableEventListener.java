package org.etlforge.ui.event;

import java.util.EventListener;
import java.util.EventObject;

public abstract class BlockableEventListener implements EventListener {
   private boolean m_isBlocked = false;
   private boolean m_isOneTimeBlocked = false;

   public final void blockOneTime() {
      this.m_isBlocked = true;
      this.m_isOneTimeBlocked = true;
   }

   public final boolean isOneTimeBlocked() {
      return this.m_isOneTimeBlocked;
   }

   public final void setBlocked(boolean var1) {
      this.m_isBlocked = var1;
      if (!var1) {
         this.m_isOneTimeBlocked = false;
      }
   }

   public final boolean isBlocked() {
      return this.m_isBlocked;
   }

   protected final class EventHandler<T extends EventObject> {
      public final void handle(T var1, BlockableEventListener.IDelegate<T> var2) {
         if (BlockableEventListener.this.isBlocked()) {
            if (BlockableEventListener.this.isOneTimeBlocked()) {
               BlockableEventListener.this.setBlocked(false);
            }
         } else {
            var2.execute(var1);
         }
      }
   }

   protected interface IDelegate<T extends EventObject> {
      void execute(T var1);
   }
}
