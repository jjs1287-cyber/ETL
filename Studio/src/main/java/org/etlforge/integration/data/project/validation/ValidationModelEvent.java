package org.etlforge.integration.data.project.validation;

import java.util.EventObject;

public class ValidationModelEvent extends EventObject {
   public ValidationModelEvent(ValidationModel var1) {
      super(var1);
   }

   public ValidationModel getSource() {
      return (ValidationModel)super.getSource();
   }
}
