package org.etlforge.ui.util.find;

import java.awt.event.ActionEvent;
import org.etlforge.ui.resources.UiResourceBundle;

public final class ReplaceAllAction extends AbstractTextSearchAction {
   public ReplaceAllAction(TextSearchModel var1, TextSearchController var2) {
      super(var1, var2, true);
      this.putValue("Name", UiResourceBundle.getString("TextSearch.Action.ReplaceAll"));
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      this.controller.replaceAll();
   }

   @Override
   protected boolean isActionSupported() {
      return this.hasSearchString() && this.target instanceof SearchAndReplaceTarget;
   }
}
