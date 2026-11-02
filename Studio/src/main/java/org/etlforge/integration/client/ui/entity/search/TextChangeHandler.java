package org.etlforge.integration.client.ui.entity.search;

import javax.swing.Action;
import org.etlforge.ui.action.ActionUtils;
import org.etlforge.ui.widget.DelayedDocumentContentChangeReaction;

final class TextChangeHandler extends DelayedDocumentContentChangeReaction {
   private final Action action;

   public TextChangeHandler(Action var1) {
      this.action = var1;
   }

   @Override
   public void run() {
      ActionUtils.run(this.action, this, "search");
   }
}
