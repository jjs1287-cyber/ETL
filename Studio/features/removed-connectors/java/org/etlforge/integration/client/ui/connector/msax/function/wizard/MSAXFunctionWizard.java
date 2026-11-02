package org.etlforge.integration.client.ui.connector.msax.function.wizard;

import com.google.common.base.Supplier;
import org.etlforge.application.filestore.FileStore;
import org.etlforge.application.ui.wizard.WizardPageException;
import org.etlforge.integration.client.connector.schema.SchemaRequestData;
import org.etlforge.integration.client.connector.serverapi.ConnectorCallInfoProvider;
import org.etlforge.integration.client.ui.connector.wizard.AbstractConnectorFunctionWizard;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.data.connector.MSAXFunction;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.ui.UiThreadSupplier;

public final class MSAXFunctionWizard extends AbstractConnectorFunctionWizard<MSAXFunction> {
   public MSAXFunctionWizard(MSAXFunction var1, Folder var2, ConnectorCallInfoProvider var3, EntityExplorerSupport var4, FileStore var5) {
      super(var1, var2, var3, var4, var5);
   }

   @Override
   protected String getStartSection() {
      return "Endpoint";
   }

   protected MSAXFunctionWizardPageChain getPageChain() {
      return (MSAXFunctionWizardPageChain)super.getPageChain();
   }

   protected void applyPageInformation(final MSAXFunction var1) throws WizardPageException {
      UiThreadSupplier.call(new Supplier<Void>() {
         public Void get() {
            MSAXFunctionWizard.this.getPageChain().applyTo(var1, MSAXFunctionWizard.this.getParent());
            return null;
         }
      });
   }

   @Override
   protected SchemaRequestData getSchemaRequestData() {
      return this.getPageChain().getSchemaRequestData();
   }
}
