package org.etlforge.integration.client.ui.interchange.entity;

import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.location.DataLocationType;

public interface LocatablePage<T extends Locatable> {
   T getLocatable();

   DataLocationType getSelectedLocationType();
}
