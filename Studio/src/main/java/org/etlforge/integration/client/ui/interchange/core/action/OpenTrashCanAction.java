package org.etlforge.integration.client.ui.interchange.core.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.DisposableWindowElementResource;
import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.application.ui.window.WindowSectionContent;
import org.etlforge.application.ui.window.WindowSectionContentProvider;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.ui.project.trashcan.TrashCanView;
import org.etlforge.integration.client.ui.project.trashcan.TrashCanViewUi;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.util.UiUtils;

public final class OpenTrashCanAction extends AbstractProjectBasedAction {
   public static final String ID = "action.ic.TrashCan";
   private final ApplicationWindow appWin;
   private EntityFilter visibleItemsFilter;

   public OpenTrashCanAction(ApplicationWindow var1, CurrentProjectService var2) {
      super(var2, "action.ic.TrashCan");
      this.appWin = var1;
   }

   public void setVisibleItemsFilter(EntityFilter var1) {
      this.visibleItemsFilter = var1;
   }

   @Override
   public void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            OpenTrashCanAction.this.openTrashCan();
         }
      });
   }

   private void openTrashCan() {
      OpenTrashCanAction.TrashCanDisplayer var1 = new OpenTrashCanAction.TrashCanDisplayer();
      this.appWin.displayContent(var1, ApplicationWindow.SectionPosition.EAST, null, null);
   }

   @Override
   protected void onProjectClosed(IntegrationProject var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            OpenTrashCanAction.this.closeTrashCan();
         }
      });
   }

   private void closeTrashCan() {
      WindowSection var1 = this.appWin.getWindowSection(ApplicationWindow.SectionPosition.EAST);
      WindowSectionContent var2 = var1.getContent(TrashCanView.CONTENT_ID);
      if (var2 != null) {
         var1.removeContent(var2);
      }
   }

   private class TrashCanDisplayer implements WindowSectionContentProvider {
      private TrashCanDisplayer() {
      }

      @Override
      public String getContentId() {
         return TrashCanView.CONTENT_ID;
      }

      @Override
      public WindowSectionContent getContent(WindowSection var1) {
         TrashCanViewUi var2 = this.createUi();
         TrashCanView var3 = new TrashCanView(var1, var2);
         var3.addResource(new DisposableWindowElementResource(var2));
         return var3;
      }

      private TrashCanViewUi createUi() {
         ManagedProject var1 = OpenTrashCanAction.this.getManagedProject();
         TrashCanViewUi var2 = new TrashCanViewUi(var1);
         var2.setEntityFilter(OpenTrashCanAction.this.visibleItemsFilter);
         var2.setWaitService(OpenTrashCanAction.this.appWin);
         return var2;
      }
   }
}
