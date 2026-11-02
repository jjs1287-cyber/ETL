package org.etlforge.integration.client.ui.connector.sap.function.wizard;

import com.google.common.base.Supplier;
import org.etlforge.application.filestore.FileStore;
import org.etlforge.application.ui.wizard.WizardPageException;
import org.etlforge.integration.client.connector.schema.SchemaRequestData;
import org.etlforge.integration.client.connector.serverapi.ConnectorCallInfoProvider;
import org.etlforge.integration.client.ui.connector.wizard.AbstractConnectorFunctionWizard;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.data.connector.SapFunction;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.ui.UiThreadSupplier;

public final class SapFunctionWizard extends AbstractConnectorFunctionWizard<SapFunction> {
   public SapFunctionWizard(SapFunction var1, Folder var2, ConnectorCallInfoProvider var3, EntityExplorerSupport var4, FileStore var5) {
      super(var1, var2, var3, var4, var5);
   }

   @Override
   protected String getStartSection() {
      return "Endpoint";
   }

   protected SapFunctionWizardPageChain getPageChain() {
      return (SapFunctionWizardPageChain)super.getPageChain();
   }

   protected void applyPageInformation(final SapFunction var1) throws WizardPageException {
      UiThreadSupplier.call(new Supplier<Void>() {
         public Void get() {
            SapFunctionWizard.this.getPageChain().applyTo(var1, SapFunctionWizard.this.getParent());
            return null;
         }
      });
   }

   @Override
   protected SchemaRequestData getSchemaRequestData() {
      return this.getPageChain().getSchemaRequestData();
   }
}
