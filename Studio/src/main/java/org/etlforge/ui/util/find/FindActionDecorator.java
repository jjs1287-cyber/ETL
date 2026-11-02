package org.etlforge.ui.util.find;

import javax.swing.Action;
import javax.swing.Icon;
import org.etlforge.ui.action.ActionDecorator;
import org.etlforge.ui.resources.UiResourceBundle;

public final class FindActionDecorator implements ActionDecorator {
   private final String name = UiResourceBundle.getString("Find...");
   private final Icon icon = null;

   @Override
   public void decorate(Action var1) {
      var1.putValue("Name", this.name);
      var1.putValue("SmallIcon", this.icon);
   }
}
