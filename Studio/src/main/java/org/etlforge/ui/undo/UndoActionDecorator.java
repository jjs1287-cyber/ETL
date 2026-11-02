package org.etlforge.ui.undo;

import javax.swing.Action;
import org.etlforge.ui.action.ActionDecorator;
import org.etlforge.ui.resources.UiResourceBundle;
import org.etlforge.ui.util.KeyUtils;

public enum UndoActionDecorator implements ActionDecorator {
   INSTANCE;

   @Override
   public void decorate(Action var1) {
      var1.putValue("Name", UiResourceBundle.getString("Undo"));
      var1.putValue("AcceleratorKey", KeyUtils.UNDO);
   }
}
