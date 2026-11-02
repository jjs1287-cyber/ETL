package org.etlforge.integration.client.ui.validation.view;

import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.project.validation.ValidationModel;
import org.etlforge.integration.data.validation.ValidationStatus;

final class NotValidFilter implements EntityFilter {
   private final ValidationModel model;

   public NotValidFilter(ValidationModel var1) {
      this.model = var1;
   }

   @Override
   public boolean apply(IntegrationEntity var1) {
      return this.model.getStatus(var1, true) != ValidationStatus.VALID;
   }
}
