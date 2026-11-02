package org.etlforge.ui.action;

import com.google.common.base.Predicate;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.etlforge.ui.util.UiUtils;

public final class ActionUtils {
   public static final Predicate<Action> ENABLED = new Predicate<Action>() {
      public boolean apply(Action var1) {
         return var1 != null && var1.isEnabled();
      }
   };

   public static void run(final Action var0, Object var1, String var2) {
      final ActionEvent var3 = new ActionEvent(var1, 1001, var2);
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            var0.actionPerformed(var3);
         }
      });
   }

   public static void runLater(final Action var0, Object var1, String var2) {
      final ActionEvent var3 = new ActionEvent(var1, 1001, var2);
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            var0.actionPerformed(var3);
         }
      });
   }

   public static void setEnabledSafely(final Action var0, final boolean var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            var0.setEnabled(var1);
         }
      });
   }

   private ActionUtils() {
   }
}
