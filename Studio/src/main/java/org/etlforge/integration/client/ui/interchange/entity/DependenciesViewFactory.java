package org.etlforge.integration.client.ui.interchange.entity;

import com.google.common.base.Function;
import javax.swing.Icon;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.DisposableWindowElementResource;
import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.application.ui.window.WindowSectionContent;
import org.etlforge.application.ui.window.WindowSectionContentProvider;
import org.etlforge.application.ui.window.WindowSectionContentUpdater;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.ui.entity.dependencies.graph.DependencyGraph;
import org.etlforge.integration.client.ui.entity.dependencies.graph.DependencyGraphView;
import org.etlforge.integration.client.ui.interchange.ProjectSpecificWindowElementContentResource;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.entity.DependencyStore;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.ProjectUtils;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.integration.uidelegate.entity.LocationDecoratedIcons;

public final class DependenciesViewFactory {
   private final ApplicationWindow window;
   private final CurrentProjectService currentProjectService;

   public DependenciesViewFactory(ApplicationWindow var1, CurrentProjectService var2) {
      this.window = var1;
      this.currentProjectService = var2;
   }

   public void showDependencies(IntegrationEntity var1) {
      DependenciesViewFactory.ContentProvider var2 = new DependenciesViewFactory.ContentProvider(var1);
      DependenciesViewFactory.ContentUpdater var3 = new DependenciesViewFactory.ContentUpdater(var1);
      this.window.displayContent(var2, ApplicationWindow.SOUTH, var3, null);
   }

   private class ContentProvider implements WindowSectionContentProvider {
      private final IntegrationEntity entity;

      public ContentProvider(IntegrationEntity var2) {
         this.entity = var2;
      }

      @Override
      public String getContentId() {
         return DependencyGraphView.CONTENT_ID;
      }

      @Override
      public WindowSectionContent getContent(WindowSection var1) {
         DependencyStore var2 = this.getDependencies();
         DependencyGraph var3 = new DependencyGraph(this.entity, var2, new DependenciesViewFactory.IconLookup());
         DependencyGraphView var4 = new DependencyGraphView(var1, var3);
         var4.addResource(new DisposableWindowElementResource(var2));
         var4.addResource(new ProjectSpecificWindowElementContentResource(DependenciesViewFactory.this.currentProjectService, var1, var4));
         return var4;
      }

      private DependencyStore getDependencies() {
         IntegrationProject var1 = ProjectUtils.getProject(this.entity);
         if (var1 == null) {
            throw new IllegalStateException("entity does not belong to a project");
         } else {
            return var1.getCurrentDependencies();
         }
      }
   }

   private static class ContentUpdater implements WindowSectionContentUpdater {
      private final IntegrationEntity entity;

      public ContentUpdater(IntegrationEntity var1) {
         this.entity = var1;
      }

      @Override
      public void update(WindowSectionContent var1) {
         ((DependencyGraphView)var1).showDependenciesFor(this.entity);
      }
   }

   private static class IconLookup implements Function<IntegrationEntity, Icon> {
      private IconLookup() {
      }

      public Icon apply(IntegrationEntity var1) {
         if (var1 instanceof Locatable) {
            Locatable var2 = (Locatable)var1;
            LocationDecoratedIcons var3 = LocationDecoratedIcons.forLocatable(var2);
            return var3.getIcon(var2, EntityIcons.Size.SMALL);
         } else {
            return null;
         }
      }
   }
}
