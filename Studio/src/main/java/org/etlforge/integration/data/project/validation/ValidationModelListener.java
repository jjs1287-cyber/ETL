package org.etlforge.integration.data.project.validation;

import java.util.EventListener;

public interface ValidationModelListener extends EventListener {
   void validationWasPerformed(ValidationModelEvent var1);
}
