package org.etlforge.integration.client.ui.interchange.entity;

import java.util.Collections;
import org.etlforge.application.ui.window.AbstractWindowElementResource;
import org.etlforge.application.ui.window.WindowElement;
import org.etlforge.application.ui.window.WindowElementStateSpan;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.DeleteRequestHandler;

final class TransientEntityDeleter extends AbstractWindowElementResource {
   private final DeleteRequestHandler deleteHandler;
   private final IntegrationEntity entity;

   public TransientEntityDeleter(IntegrationEntity var1, DeleteRequestHandler var2) {
      super(WindowElementStateSpan.WHEN_OPEN);
      this.deleteHandler = var2;
      this.entity = var1;
   }

   @Override
   public void create(WindowElement var1) {
      super.create(var1);
   }

   @Override
   public void dispose(WindowElement var1) {
      if (this.entity.isTransient()) {
         this.deleteEntity();
      }
   }

   private void deleteEntity() {
      this.deleteHandler.deleteRequested(Collections.singletonList(this.entity));
   }
}
