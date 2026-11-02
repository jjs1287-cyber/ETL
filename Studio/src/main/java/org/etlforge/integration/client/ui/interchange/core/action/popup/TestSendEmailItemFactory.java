package org.etlforge.integration.client.ui.interchange.core.action.popup;

import javax.swing.Action;
import org.etlforge.application.ui.action.popup.SingleItemPopupMenuFactory;
import org.etlforge.integration.data.entity.EmailMessage;

public final class TestSendEmailItemFactory extends SingleItemPopupMenuFactory {
   public TestSendEmailItemFactory(Action var1) {
      super(var1, EmailMessage.class);
   }
}
