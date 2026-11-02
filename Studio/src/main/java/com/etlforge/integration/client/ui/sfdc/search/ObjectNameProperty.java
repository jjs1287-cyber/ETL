package com.etlforge.integration.client.ui.sfdc.search;

import org.etlforge.integration.client.entity.search.AbstractSearchableProperty;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.xml.ExpandedName;

public final class ObjectNameProperty extends AbstractSearchableProperty<String> {
   public ObjectNameProperty() {
      super("Salesforce object", String.class);
   }

   public String getValue(IntegrationEntity var1) {
      SalesforceWizardEntity var2 = (SalesforceWizardEntity)var1;
      ExpandedName var3 = var2.getObject();
      return var3 != null ? var3.getLocalName() : "";
   }
}
