package org.etlforge.application.ui.action.delegator;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import org.etlforge.application.ui.action.ApplicationAction;

public final class ActionDelegator {
   private final Map<String, ApplicationAction> actions = Maps.newHashMap();
   private final Map<String, ActionHandler> handlers = Maps.newHashMap();
   private final Map<String, ActionDelegator.ActionStateImpl> publishedActionStates = Maps.newHashMap();
   private final Object lock = new Object();

   public void registerAction(ApplicationAction var1) {
      synchronized (this.lock) {
         this.actions.put(var1.getID(), var1);
         ActionDelegator.ActionStateImpl var3 = this.publishedActionStates.get(var1.getID());
         if (var3 != null) {
            var3.activate(var1);
         }
      }
   }

   public ActionState registerHandler(String var1, ActionHandler var2) {
      Preconditions.checkNotNull(var1, "actionId");
      Preconditions.checkNotNull(var2, "handler");
      synchronized (this.lock) {
         ActionHandler var4 = this.handlers.get(var1);
         if (var4 == var2) {
            return this.publishedActionStates.get(var1);
         }

         ActionDelegator.ActionStateImpl var5 = this.publishedActionStates.get(var1);
         if (var5 != null) {
            var5.dispose();
         }

         Action var6 = this.actions.get(var1);
         var5 = new ActionDelegator.ActionStateImpl(var6);
         this.publishedActionStates.put(var1, var5);
         this.handlers.put(var1, var2);
         return var5;
      }
   }

   public void removeHandler(String var1, ActionHandler var2) {
      Preconditions.checkNotNull(var1, "actionId");
      Preconditions.checkNotNull(var2, "handler");
      synchronized (this.lock) {
         ActionHandler var4 = this.handlers.get(var1);
         if (var4 == var2) {
            this.handlers.remove(var1);
            ActionDelegator.ActionStateImpl var5 = this.publishedActionStates.remove(var1);
            var5.dispose();
            Action var6 = this.actions.get(var1);
            if (var6 != null) {
               var6.setEnabled(false);
            }
         }
      }
   }

   public void delegate(ApplicationAction var1, ActionEvent var2) throws NoRegisteredDelegateException {
      assert EventQueue.isDispatchThread() : "ActionDelegator.delegate() must be called on the event dispatching thread";
      Preconditions.checkNotNull(var1, "source");
      Preconditions.checkNotNull(var2, "evt");
      synchronized (this.lock) {
         ActionHandler var4 = this.handlers.get(var1.getID());
         if (var4 == null) {
            throw new NoRegisteredDelegateException(var1.getID());
         }

         var4.handle(var2);
      }
   }

   public boolean isDelegated(String var1) {
      synchronized (this.lock) {
         return this.handlers.containsKey(var1);
      }
   }

   private static class ActionStateImpl implements ActionState {
      private Action action;
      private final Set<String> updatedKeys = new HashSet<>();

      public ActionStateImpl(Action var1) {
         if (var1 == null) {
            this.action = new ActionDelegator.NullAction();
         } else {
            this.activate(var1);
         }
      }

      public synchronized void activate(Action var1) {
         assert var1 != null;
         if (this.action != var1) {
            if (this.action != null) {
               this.transferActionState(this.action, var1);
            }

            this.action = var1;
         }
      }

      public synchronized void dispose() {
         this.action = new ActionDelegator.NullAction();
      }

      @Override
      public synchronized void setEnabled(boolean var1) {
         this.action.setEnabled(var1);
      }

      @Override
      public synchronized void setSelected(boolean var1) {
         this.action.putValue("ToggleAction#ON_OFF", var1);
         this.updatedKeys.add("ToggleAction#ON_OFF");
      }

      @Override
      public synchronized void setIcon(Icon var1) {
         this.action.putValue("SmallIcon", var1);
         this.updatedKeys.add("SmallIcon");
      }

      @Override
      public synchronized void setName(String var1) {
         this.action.putValue("Name", var1);
         this.updatedKeys.add("Name");
      }

      @Override
      public synchronized void setToolTip(String var1) {
         this.action.putValue("ShortDescription", var1);
         this.updatedKeys.add("ShortDescription");
      }

      private void transferActionState(Action var1, Action var2) {
         var2.setEnabled(var1.isEnabled());

         for (String var4 : this.updatedKeys) {
            var2.putValue(var4, var1.getValue(var4));
         }

         this.updatedKeys.clear();
      }
   }

   private static final class NullAction extends AbstractAction {
      private NullAction() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
      }
   }
}
