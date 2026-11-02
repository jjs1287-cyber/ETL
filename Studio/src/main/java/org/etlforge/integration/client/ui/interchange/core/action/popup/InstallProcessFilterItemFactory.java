package org.etlforge.integration.client.ui.interchange.core.action.popup;

import javax.swing.Action;
import org.etlforge.application.ui.action.popup.SingleItemPopupMenuFactory;
import org.etlforge.integration.data.entity.Operation;

public final class InstallProcessFilterItemFactory extends SingleItemPopupMenuFactory {
   public InstallProcessFilterItemFactory(Action var1) {
      super(var1, Operation.class);
   }
}
