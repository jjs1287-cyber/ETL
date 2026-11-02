package org.etlforge.ui.util.find;

import java.awt.event.ActionEvent;
import org.etlforge.ui.resources.UiResourceBundle;

public final class ReplaceAction extends AbstractTextSearchAction {
   public ReplaceAction(TextSearchModel var1, TextSearchController var2) {
      super(var1, var2, true);
      this.putValue("Name", UiResourceBundle.getString("TextSearch.Action.Replace"));
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      this.controller.replace();
   }
}
