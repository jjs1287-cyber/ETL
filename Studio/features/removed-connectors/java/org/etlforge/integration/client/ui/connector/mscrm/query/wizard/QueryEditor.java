package org.etlforge.integration.client.ui.connector.mscrm.query.wizard;

import org.etlforge.ui.UiProvider;

public interface QueryEditor extends UiProvider {
   String getFetchXml();

   boolean canDoForwardNavigation();
}
