package org.etlforge.integration.client.ui.validation.indicator;

import javax.swing.JComponent;
import org.etlforge.integration.data.validation.EntityCollectionValidationResult;

public interface ValidationResultTrigger {
   JComponent getComponent();

   void update(EntityCollectionValidationResult var1);

   void setEnabled(boolean var1);
}
