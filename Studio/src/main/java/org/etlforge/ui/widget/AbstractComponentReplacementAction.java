package org.etlforge.ui.widget;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

public abstract class AbstractComponentReplacementAction<T extends JComponent> extends AbstractAction {
   protected final T component;
   private final Action defaultAction;

   public AbstractComponentReplacementAction(T var1, KeyStroke var2) {
      this.component = (T)var1;
      this.putValue("AcceleratorKey", var2);
      this.defaultAction = this.replaceDefaultAction(var2);
   }

   private Action replaceDefaultAction(KeyStroke var1) {
      InputMap var2 = this.component.getInputMap();
      Object var3 = var2.get(var1);
      ActionMap var4 = this.component.getActionMap();
      Action var5 = var4.get(var3);
      var4.put(var3, this);
      return var5;
   }

   @Override
   public final void actionPerformed(ActionEvent var1) {
      if (this.isSpecialCase()) {
         this.handleSpecialCase();
      } else {
         this.defaultAction.actionPerformed(var1);
      }
   }

   protected abstract boolean isSpecialCase();

   protected abstract void handleSpecialCase();
}
