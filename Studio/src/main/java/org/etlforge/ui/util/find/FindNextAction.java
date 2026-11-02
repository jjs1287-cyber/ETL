package org.etlforge.ui.util.find;

import java.awt.event.ActionEvent;
import org.etlforge.ui.resources.UiResourceBundle;

public final class FindNextAction extends AbstractTextSearchAction {
   public FindNextAction(TextSearchModel var1, TextSearchController var2) {
      super(var1, var2, false);
      this.putValue("Name", UiResourceBundle.getString("TextSearch.Action.FindNext"));
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      this.controller.findNext();
   }
}
