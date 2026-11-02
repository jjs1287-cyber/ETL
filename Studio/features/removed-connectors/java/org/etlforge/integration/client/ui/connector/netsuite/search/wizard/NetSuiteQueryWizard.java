package org.etlforge.integration.client.ui.connector.netsuite.search.wizard;

import com.google.common.base.Supplier;
import org.etlforge.application.filestore.FileStore;
import org.etlforge.application.ui.wizard.WizardPageException;
import org.etlforge.integration.client.connector.netsuite.serverapi.NetSuiteDesignInfoProvider;
import org.etlforge.integration.client.connector.schema.SchemaRequestData;
import org.etlforge.integration.client.ui.connector.wizard.AbstractConnectorFunctionWizard;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.data.connector.NetSuiteQuery;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.ui.UiThreadSupplier;

public final class NetSuiteQueryWizard extends AbstractConnectorFunctionWizard<NetSuiteQuery> {
   public NetSuiteQueryWizard(NetSuiteQuery var1, Folder var2, NetSuiteDesignInfoProvider var3, EntityExplorerSupport var4, FileStore var5) {
      super(var1, var2, var3, var4, var5);
   }

   @Override
   protected String getStartSection() {
      return "Endpoint";
   }

   protected void applyPageInformation(final NetSuiteQuery var1) throws WizardPageException {
      UiThreadSupplier.call(new Supplier<Void>() {
         public Void get() {
            NetSuiteQueryWizard.this.getPageChain().applyTo(var1, NetSuiteQueryWizard.this.getParent());
            return null;
         }
      });
   }

   protected NetSuiteQueryWizardPageChain getPageChain() {
      return (NetSuiteQueryWizardPageChain)super.getPageChain();
   }

   @Override
   protected SchemaRequestData getSchemaRequestData() {
      return this.getPageChain().getSchemaRequestData();
   }
}
