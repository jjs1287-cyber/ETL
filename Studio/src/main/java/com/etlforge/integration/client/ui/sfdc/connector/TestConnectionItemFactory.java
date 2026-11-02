package com.etlforge.integration.client.ui.sfdc.connector;

import javax.swing.Action;
import org.etlforge.application.ui.action.popup.SingleItemPopupMenuFactory;
import org.etlforge.integration.data.entity.SalesforceConnector;

public final class TestConnectionItemFactory extends SingleItemPopupMenuFactory {
   public TestConnectionItemFactory(Action var1) {
      super(var1, SalesforceConnector.class);
   }
}
