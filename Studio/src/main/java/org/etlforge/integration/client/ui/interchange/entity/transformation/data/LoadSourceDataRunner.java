package org.etlforge.integration.client.ui.interchange.entity.transformation.data;

import org.apache.commons.lang.StringUtils;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.entity.settings.EntitySettings;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectUtils;
import org.etlforge.integration.client.server.ServerManager;
import org.etlforge.integration.client.ui.interchange.entity.transformation.data.db.DatabaseFilterDefiner;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.data.structure.LdapStructure;
import org.etlforge.integration.data.test.LoadSourceHistory;
import org.etlforge.integration.data.test.LoadSourceResult;
import org.etlforge.integration.data.test.TestResultError;
import org.etlforge.integration.data.test.TestResultException;
import org.etlforge.integration.data.test.TestResultStringError;
import org.etlforge.integration.loadsource.client.LoadLdapSourceDataInput;
import org.etlforge.integration.loadsource.client.LoadSourceDataInputProvider;
import org.etlforge.integration.loadsource.client.LoadSourceDataJob;
import org.etlforge.integration.loadsource.client.PluginSettingsStore;
import org.etlforge.integration.loadsource.client.ProjectSourceDataFileInputProvider;
import org.etlforge.integration.loadsource.serverapi.LoadSourceDataInput;
import org.etlforge.system.callback.CallbackResult;
import org.etlforge.ui.util.file.RecentFileStore;

final class LoadSourceDataRunner implements Runnable {
   private final SourceDataServiceImpl service;
   private final TransformationPage page;
   private final Transformation tx;
   private final CallbackResult callback;
   private RecentFileStore projectFileStore;
   private volatile LoadSourceDataInput input;

   public LoadSourceDataRunner(SourceDataServiceImpl var1, TransformationPage var2, CallbackResult var3) {
      this.service = var1;
      this.page = var2;
      this.tx = var2.getObject();
      this.callback = var3;
   }

   public void setInput(LoadSourceDataInput var1) {
      this.input = var1;
   }

   public void setProjectFileStore(RecentFileStore var1) {
      this.projectFileStore = var1;
   }

   @Override
   public void run() {
      LoadSourceDataInput var1 = this.getDataInput();
      if (var1 == null) {
         this.callback.cancelled(null);
      } else {
         this.loadSourceDataImpl(var1);
      }
   }

   private void loadSourceDataImpl(LoadSourceDataInput var1) {
      LoadSourceDataJob var2 = this.createLoadSourceDataJob(var1);
      this.service.removeTestData();
      var2.submit(this.page.getWaitService(), 0);
   }

   private LoadSourceDataJob createLoadSourceDataJob(LoadSourceDataInput var1) {
      LoadSourceDataRunner.LoadSourceHandler var2 = new LoadSourceDataRunner.LoadSourceHandler(this.page, this.callback);
      return new LoadSourceDataJob(this.service, var1, this.tx.getID(), this.tx.getName(), var2);
   }

   private LoadSourceDataInput getDataInput() {
      if (this.input == null) {
         DataStructure var1 = this.tx.getInputStructure();
         if (var1 instanceof DatabaseStructure) {
            this.input = DatabaseFilterDefiner.getInput((DatabaseStructure)var1);
         } else if (var1 instanceof LdapStructure) {
            this.input = new LoadLdapSourceDataInput();
         } else {
            ApplicationWindow var2 = this.page.getWindow();
            this.input = this.getInputFile(var2);
         }
      }

      return this.input;
   }

   private LoadSourceDataInput getInputFile(ApplicationWindow var1) {
      LoadSourceDataInputProvider var2 = this.createInputProvider(var1);
      return var2.getInput();
   }

   private LoadSourceDataInputProvider createInputProvider(ApplicationWindow var1) {
      PluginSettingsStore var2 = this.createPluginsStore();
      ServerManager var3 = this.service.getServerManager();
      ProjectSourceDataFileInputProvider var4 = new ProjectSourceDataFileInputProvider(var1, var3, var2, this.tx.isXmlSourceEdi());
      var4.setProjectFileStore(this.projectFileStore);
      return var4;
   }

   private PluginSettingsStore createPluginsStore() {
      EntitySettings var1 = this.getTransformationSettings();
      return new PluginSettingsStoreImpl(this.tx, var1);
   }

   private EntitySettings getTransformationSettings() {
      ManagedProject var1 = ProjectUtils.getManagedProject(this.tx);
      if (var1 == null) {
         throw new IllegalArgumentException("tf must belong to a managed project");
      } else {
         return var1.getEntityResources().getSettings();
      }
   }

   private static final class LoadSourceHandler implements CallbackResult {
      private final Transformation tx;
      private final LoadSourceHistory history;
      private final CallbackResult externalCallback;

      public LoadSourceHandler(TransformationPage var1, CallbackResult var2) {
         this.tx = var1.getObject();
         this.externalCallback = var2;
         ManagedProject var3 = ProjectUtils.getManagedProject(this.tx);
         this.history = var3.getTestHistory().getLoadSourceHistory();
      }

      @Override
      public void succeeded(Object var1) {
         this.history.add(LoadSourceResult.success(this.tx));
         this.externalCallback.succeeded(var1);
      }

      @Override
      public void cancelled(Object var1) {
         this.externalCallback.cancelled(var1);
      }

      @Override
      public void failed(Object var1) {
         this.addErrorToHistory(var1);
         this.displayError(var1);
         this.externalCallback.failed(var1);
      }

      private void addErrorToHistory(Object var1) {
         TestResultError var2 = null;
         if (var1 instanceof Throwable) {
            var2 = new TestResultException((Throwable)var1);
         } else if (var1 != null) {
            var2 = new TestResultStringError(var1.toString());
         } else {
            var2 = new TestResultStringError("An unknown error occurred.");
         }

         this.history.add(LoadSourceResult.error(this.tx, var2));
      }

      private void displayError(Object var1) {
         if (var1 instanceof Throwable) {
            Throwable var2 = (Throwable)var1;
            String var3 = this.getErrorMessage(var2);
            ErrorLog.attention(LoadSourceDataRunner.LoadSourceHandler.class, var3, var2);
         } else {
            String var4 = var1 != null ? var1.toString() : "An unknown error occurred";
            Alert.error(var4, Strings.get("LoadSource.Error.Title"));
         }
      }

      private String getErrorMessage(Throwable var1) {
         String var2 = Strings.format("LoadSource.Error.Message", this.tx.getName());
         String var3 = var1.getMessage();
         if (StringUtils.isNotEmpty(var3)) {
            var2 = var2 + " " + var3;
         }

         return var2;
      }
   }
}
