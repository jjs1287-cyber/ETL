package org.etlforge.integration.data.location;

import java.beans.PropertyChangeEvent;
import org.etlforge.integration.data.Locatable;

public class LocatableChangeEvent extends PropertyChangeEvent {
   private final Locatable locatable;

   public LocatableChangeEvent(Locatable var1, PropertyChangeEvent var2) {
      super(var2.getSource(), var2.getPropertyName(), var2.getOldValue(), var2.getNewValue());
      this.locatable = var1;
   }

   public Locatable getLocatable() {
      return this.locatable;
   }
}
