package org.etlforge.ui.widget.tree.check;

import org.etlforge.util.event.AbstractListenerManager;
import org.etlforge.util.event.ListenerManager;

public class CheckBoxTreeListeners extends AbstractListenerManager<CheckBoxTreeListener> implements ListenerManager<CheckBoxTreeListener> {
   protected CheckBoxTreeListener createNotifier() {
      return new CheckBoxTreeListener() {
         @Override
         public void nodeToggled(CheckBoxTreeEvent var1) {
            for (CheckBoxTreeListener var3 : CheckBoxTreeListeners.this.getListeners()) {
               var3.nodeToggled(var1);
            }
         }

         @Override
         public void overallCheckedStateChanged(CheckBoxTreeEvent var1) {
            for (CheckBoxTreeListener var3 : CheckBoxTreeListeners.this.getListeners()) {
               var3.overallCheckedStateChanged(var1);
            }
         }
      };
   }
}
