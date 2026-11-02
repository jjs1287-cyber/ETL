package org.etlforge.integration.client.ui.interchange;

import com.google.common.base.Preconditions;
import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.application.ui.window.WindowSectionContent;
import org.etlforge.application.ui.window.WindowSectionContentProvider;
import org.etlforge.integration.client.project.CurrentProjectEvent;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.ui.validation.view.ValidationView;
import org.etlforge.integration.data.project.validation.ValidationModel;

public final class ValidationWindowProviderFactory {
   private final ValidationModel model;
   private CurrentProjectService currentProjectService;

   private ValidationWindowProviderFactory(ValidationModel var1) {
      this.model = var1;
   }

   public static ValidationWindowProviderFactory newFactory(ValidationModel var0) {
      Preconditions.checkNotNull(var0, "model");
      return new ValidationWindowProviderFactory(var0);
   }

   public WindowSectionContentProvider newContentProvider() {
      return new ValidationWindowProviderFactory.ContentProviderImpl(this.model, this.currentProjectService);
   }

   public void setCurrentProjectService(CurrentProjectService var1) {
      this.currentProjectService = var1;
   }

   private static class ContentProviderImpl implements WindowSectionContentProvider {
      private final ValidationModel model;
      private final CurrentProjectService currentProjectService;

      public ContentProviderImpl(ValidationModel var1, CurrentProjectService var2) {
         this.model = var1;
         this.currentProjectService = var2;
      }

      @Override
      public WindowSectionContent getContent(WindowSection var1) {
         ValidationView var2 = new ValidationView(var1, this.model);
         if (this.currentProjectService != null) {
            var2.addResource(new ValidationWindowProviderFactory.CurrentProjectListener(this.currentProjectService, var2));
         }

         return var2;
      }

      @Override
      public String getContentId() {
         return ValidationView.CONTENT_ID;
      }
   }

   private static class CurrentProjectListener extends CurrentProjectListenerResource {
      private final ValidationView view;

      public CurrentProjectListener(CurrentProjectService var1, ValidationView var2) {
         super(var1);
         this.view = var2;
      }

      @Override
      public void projectOpened(CurrentProjectEvent var1) {
         ManagedProject var2 = var1.getManagedProject();
         this.view.setValidationModel(var2.getValidationModel());
      }

      @Override
      public void projectClosed(CurrentProjectEvent var1) {
         this.view.setValidationModel(null);
      }
   }
}
