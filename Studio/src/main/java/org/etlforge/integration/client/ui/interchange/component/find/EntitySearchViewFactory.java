package org.etlforge.integration.client.ui.interchange.component.find;

import java.awt.EventQueue;
import org.etlforge.application.ui.window.AbstractWindowElementResource;
import org.etlforge.application.ui.window.DisposableWindowElementResource;
import org.etlforge.application.ui.window.WindowElement;
import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.application.ui.window.WindowSectionContent;
import org.etlforge.application.ui.window.WindowSectionContentProvider;
import org.etlforge.integration.client.project.CurrentProjectEvent;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.ui.entity.search.EntitySearchModel;
import org.etlforge.integration.client.ui.entity.search.EntitySearchUi;
import org.etlforge.integration.client.ui.entity.search.EntitySearchUiBuilder;
import org.etlforge.integration.client.ui.entity.search.EntitySearchView;
import org.etlforge.integration.client.ui.interchange.CurrentProjectListenerResource;
import org.etlforge.integration.data.project.IntegrationProject;

public final class EntitySearchViewFactory {
   private final CurrentProjectService currentProjectService;
   private final EntitySearchUiBuilder uiBuilder;
   private Object searchUiMemento;

   public EntitySearchViewFactory(CurrentProjectService var1, EntitySearchUiBuilder var2) {
      this.currentProjectService = var1;
      this.uiBuilder = var2;
   }

   public WindowSectionContentProvider newContentProvider(IntegrationProject var1) {
      return new EntitySearchViewFactory.ContentProviderImpl(var1);
   }

   private class ContentProviderImpl implements WindowSectionContentProvider {
      private final IntegrationProject project;

      public ContentProviderImpl(IntegrationProject var2) {
         this.project = var2;
      }

      @Override
      public WindowSectionContent getContent(WindowSection var1) {
         EntitySearchUi var2 = this.createSearchUi();
         EntitySearchView var3 = new EntitySearchView(var1, var2);
         this.addViewResources(var2, var3);
         return var3;
      }

      private EntitySearchUi createSearchUi() {
         EntitySearchUi var1 = EntitySearchViewFactory.this.uiBuilder.buildSearchUi(this.project);
         if (EntitySearchViewFactory.this.searchUiMemento != null) {
            var1.setMemento(EntitySearchViewFactory.this.searchUiMemento);
         }

         return var1;
      }

      private void addViewResources(final EntitySearchUi var1, EntitySearchView var2) {
         var2.addResource(new AbstractWindowElementResource() {
            @Override
            public void dispose(WindowElement var1x) {
               EntitySearchViewFactory.this.searchUiMemento = var1.getMemento();
            }
         });
         var2.addResource(new DisposableWindowElementResource(var1));
         var2.addResource(new EntitySearchViewFactory.ProjectListener(EntitySearchViewFactory.this.currentProjectService, var1));
      }

      @Override
      public String getContentId() {
         return EntitySearchView.CONTENT_ID;
      }
   }

   private static class ProjectListener extends CurrentProjectListenerResource {
      private final EntitySearchUi searchUi;

      public ProjectListener(CurrentProjectService var1, EntitySearchUi var2) {
         super(var1);
         this.searchUi = var2;
      }

      @Override
      public void projectOpened(CurrentProjectEvent var1) {
         final IntegrationProject var2 = var1.getProject();
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               EntitySearchModel var1x = ProjectListener.this.searchUi.getModel();
               var1x.setProject(var2);
            }
         });
      }

      @Override
      public void projectClosed(CurrentProjectEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               EntitySearchModel var1x = ProjectListener.this.searchUi.getModel();
               var1x.setProject(null);
            }
         });
      }
   }
}
