package org.etlforge.integration.client.ui.connector.autodesk.function.wizard;

import com.google.common.base.Supplier;
import com.google.common.base.Throwables;
import org.etlforge.application.filestore.FileStore;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.application.ui.wizard.WizardPageException;
import org.etlforge.integration.client.connector.schema.DownloadedSchemasProcessor;
import org.etlforge.integration.client.connector.serverapi.ConnectorCallInfoProvider;
import org.etlforge.integration.client.connector.serverapi.Schemas;
import org.etlforge.integration.client.ui.connector.autodesk.function.SchemaDownloader;
import org.etlforge.integration.client.ui.connector.autodesk.function.SchemaRequestData;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.entity.wizard.AbstractEntityWizard;
import org.etlforge.integration.data.connector.AutoDeskFunction;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.ui.UiThreadSupplier;
import org.etlforge.ui.wait.MessageWaitService;
import org.etlforge.ui.wait.WaitLock;
import org.etlforge.ui.wait.WaitService;

public class AutoDeskFunctionWizard extends AbstractEntityWizard<AutoDeskFunction> {
   private final ConnectorCallInfoProvider infoProvider;
   private final EntityExplorerSupport projectExplorer;
   private final FileStore fileStore;

   public AutoDeskFunctionWizard(AutoDeskFunction var1, Folder var2, ConnectorCallInfoProvider var3, EntityExplorerSupport var4, FileStore var5) {
      super(var4.getWindow(), var1, var2);
      this.infoProvider = var3;
      this.projectExplorer = var4;
      this.fileStore = var5;
      this.setFinishWizardOnEventThread(false);
   }

   public EntityExplorerSupport getProjectExplorer() {
      return this.projectExplorer;
   }

   @Override
   protected String getStartSection() {
      return "Endpoint";
   }

   protected AutoDeskFunctionWizardPageChain getPageChain() {
      return (AutoDeskFunctionWizardPageChain)super.getPageChain();
   }

   @Override
   protected WaitLock startWaitForFinishWork() {
      WizardPage var1 = this.getPageChain().getLastPage();
      if (var1 instanceof AbstractWizardPage) {
         WaitService var2 = ((AbstractWizardPage)var1).getWaitService();
         return var2 instanceof MessageWaitService ? ((MessageWaitService)var2).startWait("Downloading the request and response structures") : var2.startWait();
      } else {
         return super.startWaitForFinishWork();
      }
   }

   protected void applyPageInformation(final AutoDeskFunction var1) throws WizardPageException {
      UiThreadSupplier.call(new Supplier<Void>() {
         public Void get() {
            AutoDeskFunctionWizard.this.getPageChain().applyTo(var1, AutoDeskFunctionWizard.this.getParent());
            return null;
         }
      });
   }

   protected void doAdditionalWorkOnFinish(AutoDeskFunction var1) throws WizardPageException {
      SchemaRequestData var2 = this.getSchemaRequestData();
      SchemaDownloader var3 = new SchemaDownloader(this.infoProvider, var2);

      try {
         Schemas var4 = var3.call();
         if (var4 == null) {
            throw new WizardPageException(this.getPageChain().getLastPage(), "Failed to download the request and response schemas.");
         }

         DownloadedSchemasProcessor var5 = new DownloadedSchemasProcessor(var1, this.fileStore, this.infoProvider);
         var5.process(var4);
      } catch (Exception var6) {
         Throwables.propagateIfInstanceOf(var6, WizardPageException.class);
         throw new WizardPageException(this.getPageChain().getLastPage(), "Failed to download the request and response schemas.", var6);
      }
   }

   private SchemaRequestData getSchemaRequestData() {
      return UiThreadSupplier.call(new Supplier<SchemaRequestData>() {
         public SchemaRequestData get() {
            return AutoDeskFunctionWizard.this.getPageChain().getSchemaRequestData();
         }
      });
   }
}
