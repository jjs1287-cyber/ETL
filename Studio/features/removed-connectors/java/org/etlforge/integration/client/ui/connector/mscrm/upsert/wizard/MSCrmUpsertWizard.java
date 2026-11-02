package org.etlforge.integration.client.ui.connector.mscrm.upsert.wizard;

import com.google.common.base.Supplier;
import com.google.common.base.Throwables;
import org.etlforge.application.filestore.FileStore;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.application.ui.wizard.WizardPageException;
import org.etlforge.integration.client.connector.mscrm.serverapi.MSCrmDesignInfoProvider;
import org.etlforge.integration.client.connector.schema.DownloadedSchemasProcessor;
import org.etlforge.integration.client.connector.schema.SchemaRequestData;
import org.etlforge.integration.client.connector.schema.SchemasDownloader;
import org.etlforge.integration.client.connector.serverapi.Schemas;
import org.etlforge.integration.client.ui.connector.mscrm.ConnectorCallInfoProviderAdapter;
import org.etlforge.integration.client.ui.connector.mscrm.MSCrmObjectLookupFactory;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.entity.wizard.AbstractEntityWizard;
import org.etlforge.integration.data.connector.MSCrmUpsert;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.ui.UiThreadSupplier;
import org.etlforge.ui.wait.MessageWaitService;
import org.etlforge.ui.wait.WaitLock;
import org.etlforge.ui.wait.WaitService;

public final class MSCrmUpsertWizard extends AbstractEntityWizard<MSCrmUpsert> {
   private final MSCrmDesignInfoProvider infoProvider;
   private final EntityExplorerSupport projectExplorer;
   private final FileStore fileStore;

   public MSCrmUpsertWizard(
      MSCrmUpsert var1, Folder var2, MSCrmDesignInfoProvider var3, MSCrmObjectLookupFactory var4, EntityExplorerSupport var5, FileStore var6
   ) {
      super(var5.getWindow(), var1, var2);
      this.infoProvider = var3;
      this.projectExplorer = var5;
      this.fileStore = var6;
      this.setFinishWizardOnEventThread(false);
   }

   public EntityExplorerSupport getProjectExplorer() {
      return this.projectExplorer;
   }

   @Override
   protected String getStartSection() {
      return "Endpoint";
   }

   @Override
   protected WaitLock startWaitForFinishWork() {
      WizardPage var1 = this.getPageChain().getLastPage();
      if (var1 instanceof AbstractWizardPage) {
         WaitService var2 = ((AbstractWizardPage)var1).getWaitService();
         return var2 instanceof MessageWaitService ? ((MessageWaitService)var2).startWait("Generating the Upsert response structure") : var2.startWait();
      } else {
         return super.startWaitForFinishWork();
      }
   }

   protected void applyPageInformation(final MSCrmUpsert var1) throws WizardPageException {
      UiThreadSupplier.call(new Supplier<Void>() {
         public Void get() {
            MSCrmUpsertWizard.this.getPageChain().applyTo(var1, MSCrmUpsertWizard.this.getParent());
            return null;
         }
      });
   }

   protected MSCrmUpsertWizardPageChain getPageChain() {
      return (MSCrmUpsertWizardPageChain)super.getPageChain();
   }

   protected void doAdditionalWorkOnFinish(MSCrmUpsert var1) throws WizardPageException {
      SchemaRequestData var2 = this.getSchemaRequestData();
      SchemasDownloader var3 = new SchemasDownloader(new ConnectorCallInfoProviderAdapter(this.infoProvider), var2);

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
            return MSCrmUpsertWizard.this.getPageChain().getSchemaRequestData();
         }
      });
   }
}
