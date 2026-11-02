package org.etlforge.integration.client.ui.interchange.core.action.popup;

import org.etlforge.application.ui.window.editor.EditorDisplayer;
import org.etlforge.integration.activity.ui.core.action.ActivityAction;
import org.etlforge.integration.activity.ui.core.action.popup.AbstractActivityPopupMenuItemFactory;

public final class ActivityActionPopupMenuItemFactory extends AbstractActivityPopupMenuItemFactory {
   public ActivityActionPopupMenuItemFactory(ActivityAction var1, EditorDisplayer var2) {
      super(var1);
      this.setPageDisplayer(var2);
   }
}
