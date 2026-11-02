package org.etlforge.integration.client.ui.connector.wizard;

import com.google.common.base.Supplier;
import com.google.common.base.Throwables;
import java.util.concurrent.Callable;
import org.etlforge.application.filestore.FileStore;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.application.ui.wizard.WizardPageException;
import org.etlforge.integration.client.connector.schema.DownloadedSchemasProcessor;
import org.etlforge.integration.client.connector.schema.SchemaRequestData;
import org.etlforge.integration.client.connector.schema.SchemasDownloader;
import org.etlforge.integration.client.connector.serverapi.ConnectorCallInfoProvider;
import org.etlforge.integration.client.connector.serverapi.Schemas;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.entity.wizard.AbstractEntityWizard;
import org.etlforge.integration.data.connector.AbstractConnectorCall;
import org.etlforge.integration.data.connector.ConnectorCall;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.ui.UiThreadSupplier;
import org.etlforge.ui.wait.MessageWaitService;
import org.etlforge.ui.wait.WaitLock;
import org.etlforge.ui.wait.WaitService;

public abstract class AbstractConnectorFunctionWizard<F extends IntegrationEntity & ConnectorCall> extends AbstractEntityWizard<F> {
   private final ConnectorCallInfoProvider infoProvider;
   private final EntityExplorerSupport projectExplorer;
   private final FileStore fileStore;

   protected AbstractConnectorFunctionWizard(F var1, Folder var2, ConnectorCallInfoProvider var3, EntityExplorerSupport var4, FileStore var5) {
      super(var4.getWindow(), (F)var1, var2);
      this.infoProvider = var3;
      this.projectExplorer = var4;
      this.fileStore = var5;
      this.setFinishWizardOnEventThread(false);
   }

   public final ConnectorCallInfoProvider getInfoProvider() {
      return this.infoProvider;
   }

   public final EntityExplorerSupport getProjectExplorer() {
      return this.projectExplorer;
   }

   public final FileStore getFileStore() {
      return this.fileStore;
   }

   @Override
   protected final WaitLock startWaitForFinishWork() {
      WizardPage var1 = this.getPageChain().getLastPage();
      if (var1 instanceof AbstractWizardPage) {
         WaitService var2 = ((AbstractWizardPage)var1).getWaitService();
         return var2 instanceof MessageWaitService ? ((MessageWaitService)var2).startWait(this.getDownloadingSchemasMessage()) : var2.startWait();
      } else {
         return super.startWaitForFinishWork();
      }
   }

   protected String getDownloadingSchemasMessage() {
      return "Downloading the request and response structures";
   }

   @Override
   protected final void doAdditionalWorkOnFinish(F var1) throws WizardPageException {
      try {
         Callable var2 = this.createSchemasDownloader();
         Schemas var3 = (Schemas)var2.call();
         if (var3 == null) {
            throw new WizardPageException(this.getPageChain().getLastPage(), "Failed to download the request and response schemas.");
         }

         DownloadedSchemasProcessor var4 = new DownloadedSchemasProcessor((AbstractConnectorCall)var1, this.fileStore, this.infoProvider);
         var4.process(var3);
      } catch (Exception var5) {
         Throwables.propagateIfInstanceOf(var5, WizardPageException.class);
         throw new WizardPageException(this.getPageChain().getLastPage(), "Failed to download the request and response schemas.", var5);
      }
   }

   private Callable<Schemas> createSchemasDownloader() {
      SchemaRequestData var1 = UiThreadSupplier.call(new Supplier<SchemaRequestData>() {
         public SchemaRequestData get() {
            return AbstractConnectorFunctionWizard.this.getSchemaRequestData();
         }
      });
      return new SchemasDownloader(this.infoProvider, var1);
   }

   protected abstract SchemaRequestData getSchemaRequestData();
}
