package org.etlforge.ui.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.List;
import javax.swing.Action;
import javax.swing.JPopupMenu;
import org.etlforge.system.Disposable;
import org.etlforge.ui.EnabledStateGate;
import org.etlforge.ui.widget.popup.KongaPopupMenu;

public final class PopupMenuRequestHandlers {
   public static Disposable install(Component var0, PopupMenuRequestHandler var1) {
      return PopupMenuRequestListener.install(var0, var1);
   }

   public static Disposable install(Component var0, Action... var1) {
      return install(var0, with(var1));
   }

   public static PopupMenuRequestHandler with(final Action... var0) {
      return new PopupMenuRequestHandler() {
         @Override
         public boolean popupMenuRequested(Component var1, MouseEvent var2) {
            KongaPopupMenu var3 = KongaPopupMenu.fromItems(var0);
            var3.show(var1, var2.getX(), var2.getY());
            return true;
         }
      };
   }

   public static Disposable install(Component var0, Collection<? extends Action> var1) {
      return install(var0, var1.toArray(new Action[var1.size()]));
   }

   public static PopupMenuRequestHandler fromActions(Iterable<? extends Action> var0, EnabledStateGate var1) {
      Preconditions.checkNotNull(var0, "actions");
      Preconditions.checkNotNull(var1, "enabledGate");
      return new PopupMenuRequestHandlers.HandlerUsingActions(Lists.newArrayList(var0), var1);
   }

   private PopupMenuRequestHandlers() {
   }

   private static final class HandlerUsingActions implements PopupMenuRequestHandler {
      private final List<Action> actions;
      private final EnabledStateGate enabledStateGate;
      private JPopupMenu popup;

      public HandlerUsingActions(List<Action> var1, EnabledStateGate var2) {
         this.actions = var1;
         this.enabledStateGate = var2;
      }

      @Override
      public boolean popupMenuRequested(Component var1, MouseEvent var2) {
         if (this.enabledStateGate.isEnabled()) {
            this.getPopupMenu().show(var1, var2.getX(), var2.getY());
            return true;
         } else {
            return false;
         }
      }

      private JPopupMenu getPopupMenu() {
         if (this.popup == null) {
            this.popup = this.createPopupMenu();
         }

         return this.popup;
      }

      private JPopupMenu createPopupMenu() {
         KongaPopupMenu var1 = new KongaPopupMenu();
         var1.addActions(this.actions);
         return var1;
      }
   }
}
