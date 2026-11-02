package org.etlforge.integration.etlforgeconnector;

import java.io.Serializable;
import org.etlforge.util.name.Nameable;

public interface ConnectorObject extends Nameable, Serializable {
   @Override
   String getName();

   String getDescription();
}
