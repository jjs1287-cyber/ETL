package org.etlforge.integration.client.ui.interchange.core.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.application.ui.window.WindowSectionContent;
import org.etlforge.application.ui.window.WindowSectionContentProvider;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.integration.client.project.CurrentProjectEvent;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.ui.de.global.ExpressionSourceDisplayer;
import org.etlforge.integration.client.ui.de.global.GlobalDataElementsView;
import org.etlforge.integration.client.ui.de.global.GlobalDataElementsViewFactory;
import org.etlforge.integration.client.ui.entity.UiCompanion;
import org.etlforge.integration.client.ui.interchange.CurrentProjectListenerResource;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Script;
import org.etlforge.integration.data.entity.transformation.TransformationExpressionSource;
import org.etlforge.integration.data.script.ExpressionSource;

public final class ViewReferencedDataElementsAction extends AbstractProjectBasedAction {
   public static final String ID = "action.ic.DataElements";
   private final ApplicationWindow appWin;

   public ViewReferencedDataElementsAction(ApplicationWindow var1, CurrentProjectService var2) {
      super(var2, "action.ic.DataElements");
      this.appWin = var1;
   }

   @Override
   public void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ViewReferencedDataElementsAction.this.openDataElementsView();
         }
      });
   }

   private void openDataElementsView() {
      WindowSectionContentProvider var1 = this.createViewFactory();
      this.appWin.displayContent(var1, ApplicationWindow.SectionPosition.SOUTH, null, null);
   }

   private WindowSectionContentProvider createViewFactory() {
      GlobalDataElementsViewFactory var1 = new GlobalDataElementsViewFactory(this.getProject());
      var1.setExpressionSourceDisplayer(new ViewReferencedDataElementsAction.ExpressionSourceDisplayerImpl());
      return new ViewReferencedDataElementsAction.ProviderWrapper(var1, this.getCurrentProjectService());
   }

   private static class ExpressionSourceDisplayerImpl implements ExpressionSourceDisplayer {
      private ExpressionSourceDisplayerImpl() {
      }

      @Override
      public void display(ExpressionSource var1) {
         if (var1 instanceof Script) {
            this.openPage((Script)var1);
         } else if (var1 instanceof TransformationExpressionSource) {
            TransformationExpressionSource var2 = (TransformationExpressionSource)var1;
            Editor var3 = this.openPage(var2.getTransformation());
            if (var3 instanceof ExpressionSourceDisplayer) {
               ((ExpressionSourceDisplayer)var3).display(var1);
            }
         }
      }

      private Editor openPage(IntegrationEntity var1) {
         return UiCompanion.of(var1).openPage();
      }
   }

   private static class ProviderWrapper implements WindowSectionContentProvider {
      private final GlobalDataElementsViewFactory impl;
      private final CurrentProjectService currentProjectService;

      public ProviderWrapper(GlobalDataElementsViewFactory var1, CurrentProjectService var2) {
         this.impl = var1;
         this.currentProjectService = var2;
      }

      @Override
      public String getContentId() {
         return this.impl.getContentId();
      }

      @Override
      public WindowSectionContent getContent(WindowSection var1) {
         GlobalDataElementsView var2 = this.impl.getContent(var1);
         ViewReferencedDataElementsAction.ProviderWrapper.ProjectListener var3 = new ViewReferencedDataElementsAction.ProviderWrapper.ProjectListener(
            this.currentProjectService, var2
         );
         var2.addResource(var3);
         return var2;
      }

      private static class ProjectListener extends CurrentProjectListenerResource {
         private final GlobalDataElementsView view;

         public ProjectListener(CurrentProjectService var1, GlobalDataElementsView var2) {
            super(var1);
            this.view = var2;
         }

         @Override
         public void projectOpened(CurrentProjectEvent var1) {
            this.view.setProject(var1.getProject());
         }

         @Override
         public void projectClosed(CurrentProjectEvent var1) {
            this.view.setProject(null);
         }
      }
   }
}
