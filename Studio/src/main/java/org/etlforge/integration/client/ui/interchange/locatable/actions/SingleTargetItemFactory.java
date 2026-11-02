package org.etlforge.integration.client.ui.interchange.locatable.actions;

import javax.swing.Action;
import org.etlforge.application.ui.action.popup.SingleItemPopupMenuFactory;
import org.etlforge.integration.data.entity.Target;

public final class SingleTargetItemFactory extends SingleItemPopupMenuFactory {
   public SingleTargetItemFactory(Action var1) {
      super(var1, Target.class);
   }
}
