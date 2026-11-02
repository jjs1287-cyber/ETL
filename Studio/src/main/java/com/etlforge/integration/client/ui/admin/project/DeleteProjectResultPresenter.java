package com.etlforge.integration.client.ui.admin.project;

import com.google.common.collect.Maps;
import com.etlforge.integration.client.admin.project.DeleteIntegrationProjectResult;
import com.etlforge.integration.client.ui.admin.resources.EnterpriseAdminResources;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import org.etlforge.integration.client.ui.admin.component.project.ProjectConsolePage;
import org.etlforge.integration.data.entity.id.IntegrationProjectId;
import org.etlforge.ui.Empty;
import org.etlforge.ui.Size;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.CaptionLayout;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.selection.SelectionUtils;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.overlay.OverlayCenterPositioner;
import org.etlforge.ui.widget.overlay.OverlayContainer;
import org.etlforge.ui.widget.overlay.OverlayDecorator;
import org.etlforge.ui.widget.text.KongaTextArea;

final class DeleteProjectResultPresenter {
   private final ProjectConsolePage console;
   private final Map<IntegrationProjectId, String> projectNames;

   public DeleteProjectResultPresenter(ProjectConsolePage var1, Map<IntegrationProjectId, String> var2) {
      this.console = var1;
      this.projectNames = Maps.newHashMap(var2);
   }

   public void show(List<DeleteIntegrationProjectResult> var1) {
      if (!this.isEverythingOk(var1)) {
         DeleteProjectResultPresenter.Ui var2 = new DeleteProjectResultPresenter.Ui(var1);
         this.getInternalPresenter().present(var2);
      }
   }

   private boolean isEverythingOk(List<DeleteIntegrationProjectResult> var1) {
      for (DeleteIntegrationProjectResult var3 : var1) {
         if (!var3.success() || !var3.getWarnings().isEmpty()) {
            return false;
         }
      }

      return true;
   }

   private DeleteProjectResultPresenter.InternalPresenter getInternalPresenter() {
      return this.console != null
         ? new DeleteProjectResultPresenter.OverlayPresenter(this.console.getOverlayContainer())
         : new DeleteProjectResultPresenter.DialogPresenter();
   }

   private class DialogPresenter implements DeleteProjectResultPresenter.InternalPresenter {
      private DialogPresenter() {
      }

      @Override
      public void present(UiProvider var1) {
         KongaDialog var2 = new KongaDialog(this.lookupOwner(), EnterpriseAdminResources.getString("DeleteProjects.Warnings.Title"), true);
         var2.standardLayout(var1, KongaDialog.CLOSE_ONLY);
         var2.manageLocation(DeleteProjectResultPresenter.class);
         var2.setVisible(true);
      }

      private JFrame lookupOwner() {
         JFrame var1 = null;
         if (DeleteProjectResultPresenter.this.console != null) {
            var1 = DeleteProjectResultPresenter.this.console.getWindow().getFrame();
         }

         if (var1 == null) {
            var1 = UiUtils.getActiveFrame();
         }

         return var1;
      }
   }

   private interface InternalPresenter {
      void present(UiProvider var1);
   }

   private static class OverlayPresenter implements DeleteProjectResultPresenter.InternalPresenter {
      private final OverlayContainer container;

      public OverlayPresenter(OverlayContainer var1) {
         this.container = var1;
      }

      @Override
      public void present(UiProvider var1) {
         BorderLayoutBuilder var2 = new BorderLayoutBuilder().center(var1).withBorder(Empty.border(10));
         OverlayDecorator var3 = new OverlayDecorator(var2, EnterpriseAdminResources.getString("DeleteProjects.Warnings.Title"));
         var3.installOkAndCancelShortCuts();
         var3.setBlocking(true);
         var3.useCloseOnly();
         var3.show(this.container, new OverlayCenterPositioner());
         var1.requestFocus();
      }
   }

   private class Ui implements UiProvider, SelectionListener {
      private final DeleteProjectResultTree tree;
      private final KongaTextArea messageViewer;

      public Ui(List<DeleteIntegrationProjectResult> var2) {
         this.tree = new DeleteProjectResultTree(DeleteProjectResultPresenter.this.projectNames, var2);
         this.tree.addMessageSelectionListener(this);
         this.messageViewer = KongaTextArea.viewer(Size.rows(4), Size.columns(60));
         this.messageViewer.setLabel(EnterpriseAdminResources.getString("DeleteProjects.Details"));
      }

      @Override
      public void decorate(UiCanvas var1) {
         String var2 = EnterpriseAdminResources.getString("DeleteProjects.Warnings");
         Grid var3 = Vertical.grid(10).of(this.tree, this.messageViewer.asUiProvider());
         CaptionLayout.of(var2, var3).decorate(var1);
      }

      @Override
      public void requestFocus() {
         this.tree.requestFocus();
      }

      @Override
      public void selectionChanged(SelectionChangedEvent var1) {
         String var2 = SelectionUtils.getSingleSelection(var1.getSelection(), String.class);
         this.messageViewer.setText(var2);
         this.messageViewer.setCaretPosition(0);
      }
   }
}
