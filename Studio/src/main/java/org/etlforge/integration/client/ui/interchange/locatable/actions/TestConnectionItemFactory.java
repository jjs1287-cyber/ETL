package org.etlforge.integration.client.ui.interchange.locatable.actions;

import javax.swing.Action;
import org.etlforge.application.ui.action.popup.SingleItemPopupMenuFactory;
import org.etlforge.integration.data.Locatable;

public final class TestConnectionItemFactory extends SingleItemPopupMenuFactory {
   public TestConnectionItemFactory(Action var1) {
      super(var1, Locatable.class);
   }
}
