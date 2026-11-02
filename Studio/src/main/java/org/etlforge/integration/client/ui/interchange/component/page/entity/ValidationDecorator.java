package org.etlforge.integration.client.ui.interchange.component.page.entity;

import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.validation.ValidationResultAcceptor;
import org.etlforge.integration.client.ui.validation.indicator.ToolbarValidationIndicator;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.validation.ValidationModel;
import org.etlforge.integration.data.project.validation.ValidationModelEvent;
import org.etlforge.integration.data.project.validation.ValidationModelListener;
import org.etlforge.integration.data.validation.EntityCollectionValidationResult;
import org.etlforge.integration.data.validation.ValidationStatus;
import org.etlforge.system.Disposable;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.event.ApplicationMessageEvent;

final class ValidationDecorator implements UiProvider, Disposable {
   private final ValidationModel model;
   private final ValidationModelListener listener = new ValidationDecorator.Listener();
   private final IntegrationEntity entity;
   private final ToolbarValidationIndicator indicator;
   private ValidationDecorator.StatusMessagePoster statusPoster;

   public ValidationDecorator(ValidationModel var1, IntegrationEntity var2, ApplicationWindow var3) {
      this.model = var1;
      this.entity = var2;
      var1.addListener(this.listener);
      this.indicator = new ToolbarValidationIndicator(var2.getEntityType());
      this.statusPoster = new ValidationDecorator.StatusMessagePoster(var3);
   }

   @Override
   public void dispose() {
      this.model.removeListener(this.listener);
   }

   public void validate() {
      this.model.validateItem(this.entity);
   }

   public void setEnabled(boolean var1) {
      this.indicator.setEnabled(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.indicator.getUi().decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.indicator.getUi().requestFocus();
   }

   private class IndicatorUpdator implements ValidationResultAcceptor {
      private IndicatorUpdator() {
      }

      @Override
      public void accept(EntityCollectionValidationResult var1) {
         this.notifyInvalidStatusPoster(var1);
         ValidationDecorator.this.indicator.update(var1);
         ValidationDecorator.this.statusPoster = null;
      }

      private void notifyInvalidStatusPoster(EntityCollectionValidationResult var1) {
         if (ValidationDecorator.this.statusPoster != null) {
            if (var1.isFatal()) {
               ValidationDecorator.this.statusPoster.postInvalid();
            } else if (var1.status() == ValidationStatus.WARNING) {
               ValidationDecorator.this.statusPoster.postWarning();
            }
         }
      }
   }

   private class Listener implements ValidationModelListener {
      private Listener() {
      }

      @Override
      public void validationWasPerformed(ValidationModelEvent var1) {
         EntityCollectionValidationResult var2 = ValidationDecorator.this.model.getCompoundValidationState(ValidationDecorator.this.entity);
         ValidationDecorator.IndicatorUpdator var3 = ValidationDecorator.this.new IndicatorUpdator();
         var3.accept(var2);
      }
   }

   private class StatusMessagePoster {
      private final ApplicationWindow window;

      public StatusMessagePoster(ApplicationWindow var2) {
         this.window = var2;
      }

      public void postInvalid() {
         String var1 = Strings.format("EntityPage.InvalidMessagePoster.Message", ValidationDecorator.this.entity.getName());
         ApplicationMessageEvent var2 = new ApplicationMessageEvent(ValidationDecorator.this, var1, 3);
         this.window.postApplicationMessageEvent(var2);
      }

      public void postWarning() {
      }
   }
}
