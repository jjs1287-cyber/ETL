package org.etlforge.integration.client.ui.interchange.core.action;

import java.awt.event.ActionEvent;
import org.etlforge.application.ui.BusyWorker;
import org.etlforge.application.ui.action.AbstractApplicationAction;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.integration.client.project.ProjectManager;
import org.etlforge.integration.client.project.jitterpack.ImporterFactory;
import org.etlforge.integration.client.project.jitterpack.UnpackCallback;
import org.etlforge.integration.client.ui.jitterpack.DefaultFileExaminer;
import org.etlforge.integration.client.ui.jitterpack.ImportJitterPackDialog;
import org.etlforge.integration.client.ui.jitterpack.ImportJitterPackPage;
import org.etlforge.integration.client.ui.jitterpack.ImportJitterPackUi;
import org.etlforge.integration.client.ui.jitterpack.JitterPackerUi;
import org.etlforge.integration.client.ui.jitterpack.SelectedFileExaminer;
import org.etlforge.integration.client.ui.project.loading.LoadingIssuesPresenter;
import org.etlforge.integration.data.project.IntegrationProject;

public final class ImportJitterPackAction extends AbstractApplicationAction {
   public static final String ID = "action.ic.ImportJitterpak";
   private static final boolean USE_DIALOG = true;
   private final ApplicationWindow appWin;
   private final ProjectManager projectManager;
   private final ImporterFactory importerFactory;
   private String currentPageId;

   public ImportJitterPackAction(ApplicationWindow var1, ProjectManager var2, ImporterFactory var3) {
      super("action.ic.ImportJitterpak", InterchangeActionDictionary.DICTIONARY);
      this.appWin = var1;
      this.projectManager = var2;
      this.importerFactory = var3;
   }

   @Override
   public void execute(ActionEvent var1) {
      (new BusyWorker(this.appWin) {
         @Override
         protected void doWork() {
            ImportJitterPackAction.this.openImportUi();
         }
      }).run();
   }

   private void openImportUi() {
      this.openDialog();
   }

   private void openDialog() {
      ImportJitterPackUi var1 = this.createImportUi();
      ImportJitterPackAction.UnpackCallbackImpl var2 = new ImportJitterPackAction.UnpackCallbackImpl();
      ImportJitterPackDialog var3 = new ImportJitterPackDialog(var1, this.importerFactory, var2);
      var2.setDialog(var3);
      var3.show();
   }

   private void displayImportPage() {
      Editor var1 = this.locateOpenPage();
      if (var1 == null) {
         var1 = this.createImportPage();
      }

      this.appWin.getEditorService().openEditor(var1);
   }

   private Editor locateOpenPage() {
      return this.currentPageId != null ? this.appWin.getEditorService().getEditor(this.currentPageId) : null;
   }

   private Editor createImportPage() {
      ImportJitterPackUi var1 = this.createImportUi();
      ImportJitterPackAction.UnpackCallbackImpl var2 = new ImportJitterPackAction.UnpackCallbackImpl();
      ImportJitterPackPage var3 = new ImportJitterPackPage(var1, this.importerFactory, var2);
      var2.setPage(var3);
      this.currentPageId = var3.getId();
      return var3;
   }

   private ImportJitterPackUi createImportUi() {
      boolean var1 = false;
      ImportJitterPackUi var2 = new ImportJitterPackUi(this.projectManager, this.appWin, var1);
      var2.setFileExaminer(this.getFileExaminer());
      return var2;
   }

   private SelectedFileExaminer getFileExaminer() {
      JitterPackerUi var1 = new JitterPackerUi();
      var1.setLoadingIssuesHandler(new LoadingIssuesPresenter());
      return new DefaultFileExaminer(this.appWin, var1, this.importerFactory);
   }

   private final class UnpackCallbackImpl implements UnpackCallback {
      private Editor page;
      private ImportJitterPackDialog dialog;

      private UnpackCallbackImpl() {
      }

      public void setPage(Editor var1) {
         this.page = var1;
      }

      public void setDialog(ImportJitterPackDialog var1) {
         this.dialog = var1;
      }

      @Override
      public void unpackingCompleted(IntegrationProject var1) {
         if (this.page != null) {
            this.page.close();
         } else if (this.dialog != null) {
            this.dialog.close();
         }

         ImportJitterPackAction.this.projectManager.setCurrentProject(var1);
      }

      @Override
      public void unpackingCancelled(String var1) {
      }

      @Override
      public void unpackingFailed(String var1, Throwable var2) {
      }
   }
}
