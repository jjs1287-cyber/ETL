package org.etlforge.ui.action;

import com.google.common.base.Preconditions;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

public final class ActionFactory {
   public static Action forKey(KeyStroke var0, final ActionListener var1) {
      Preconditions.checkNotNull(var0, "key");
      Preconditions.checkNotNull(var1, "handler");
      AbstractAction var2 = new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent var1x) {
            if (this.isEnabled()) {
               var1.actionPerformed(var1x);
            }
         }
      };
      var2.putValue("AcceleratorKey", var0);
      return var2;
   }

   private ActionFactory() {
   }
}
