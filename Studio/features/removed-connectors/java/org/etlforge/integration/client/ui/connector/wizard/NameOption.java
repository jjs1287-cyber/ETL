package org.etlforge.integration.client.ui.connector.wizard;

import com.google.common.base.Function;
import org.etlforge.integration.etlforgeconnector.ConnectorObject;

public interface NameOption extends Function<ConnectorObject, String> {
   String getLabel();
}
