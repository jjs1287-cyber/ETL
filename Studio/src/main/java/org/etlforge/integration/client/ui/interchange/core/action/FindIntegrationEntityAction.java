package org.etlforge.integration.client.ui.interchange.core.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.WindowSectionContent;
import org.etlforge.application.ui.window.WindowSectionContentProvider;
import org.etlforge.application.ui.window.WindowSectionContentUpdater;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.ui.entity.search.EntitySearchUiBuilder;
import org.etlforge.integration.client.ui.entity.search.EntitySearchView;
import org.etlforge.integration.client.ui.entity.search.SearchType;
import org.etlforge.integration.client.ui.interchange.component.find.EntitySearchViewFactory;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.project.IntegrationProject;

public final class FindIntegrationEntityAction extends AbstractProjectBasedAction {
   public static final String ID = "action.ic.Find";
   private final ApplicationWindow appWin;
   private final EntitySearchViewFactory factory;

   public FindIntegrationEntityAction(ApplicationWindow var1, CurrentProjectService var2, Iterable<EntityType> var3, SearchType[] var4) {
      super(var2, "action.ic.Find");
      this.appWin = var1;
      EntitySearchUiBuilder var5 = new EntitySearchUiBuilder(var3, var4);
      this.factory = new EntitySearchViewFactory(var2, var5);
   }

   @Override
   public void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            FindIntegrationEntityAction.this.openSearchView();
         }
      });
   }

   private void openSearchView() {
      IntegrationProject var1 = this.getProject();
      WindowSectionContentProvider var2 = this.factory.newContentProvider(var1);
      this.appWin.displayContent(var2, ApplicationWindow.SOUTH, new FindIntegrationEntityAction.FocusRequestor(), null);
   }

   private static class FocusRequestor implements WindowSectionContentUpdater {
      private FocusRequestor() {
      }

      @Override
      public void update(WindowSectionContent var1) {
         ((EntitySearchView)var1).requestFocus();
      }
   }
}
