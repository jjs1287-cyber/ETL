package org.etlforge.integration.client.ui.connector.netsuite.function.wizard;

import org.etlforge.integration.client.ui.connector.netsuite.lookup.NetSuiteCustomFieldsDesignLookup;
import org.etlforge.integration.client.ui.connector.netsuite.lookup.NetSuiteDefinitionDesignLookup;
import org.etlforge.integration.client.ui.entity.wizard.EntityWizardFactory;

public abstract class NetSuiteEntityWizardFactory extends EntityWizardFactory {
   private final NetSuiteDefinitionDesignLookup defLookup;
   private final NetSuiteCustomFieldsDesignLookup cfLookup;

   public NetSuiteEntityWizardFactory(NetSuiteDefinitionDesignLookup var1, NetSuiteCustomFieldsDesignLookup var2) {
      this.defLookup = var1;
      this.cfLookup = var2;
   }

   public NetSuiteCustomFieldsDesignLookup getNetSuiteCustomFieldsDesignLookup() {
      return this.cfLookup;
   }

   public NetSuiteDefinitionDesignLookup getNetSuiteDefinitionDesignLookup() {
      return this.defLookup;
   }
}
