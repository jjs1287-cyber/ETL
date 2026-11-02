package org.etlforge.integration.data.project.validation;

import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.validation.EntityCollectionValidationResult;
import org.etlforge.integration.data.validation.ValidationStatus;

public interface ValidationModel {
   IntegrationProject getProject();

   ValidationStatus validateProject();

   ValidationStatus validateItem(IntegrationEntity var1);

   EntityCollectionValidationResult getCompoundValidationState(IntegrationEntity var1);

   ValidationStatus getStatus(IntegrationEntity var1, boolean var2);

   void addListener(ValidationModelListener var1);

   void removeListener(ValidationModelListener var1);
}
