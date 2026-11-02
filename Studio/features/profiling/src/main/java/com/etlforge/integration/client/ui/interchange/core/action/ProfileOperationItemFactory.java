package com.etlforge.integration.client.ui.interchange.core.action;

import javax.swing.Action;
import org.etlforge.application.ui.action.popup.SingleItemPopupMenuFactory;
import org.etlforge.integration.data.entity.Operation;

public final class ProfileOperationItemFactory extends SingleItemPopupMenuFactory {
   public ProfileOperationItemFactory(Action var1) {
      super(var1, Operation.class);
   }
}
