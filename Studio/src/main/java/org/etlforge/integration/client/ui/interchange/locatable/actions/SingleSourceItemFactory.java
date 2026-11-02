package org.etlforge.integration.client.ui.interchange.locatable.actions;

import javax.swing.Action;
import org.etlforge.application.ui.action.popup.SingleItemPopupMenuFactory;
import org.etlforge.integration.data.entity.Source;

public final class SingleSourceItemFactory extends SingleItemPopupMenuFactory {
   public SingleSourceItemFactory(Action var1) {
      super(var1, Source.class);
   }
}
