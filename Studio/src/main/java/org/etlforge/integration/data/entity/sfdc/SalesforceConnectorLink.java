package org.etlforge.integration.data.entity.sfdc;

import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.SalesforceConnector;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.integration.data.entity.link.AbstractEntityLink;

public final class SalesforceConnectorLink<T extends IntegrationEntity & SalesforceWizardEntity> extends AbstractEntityLink {
   public SalesforceConnectorLink(SalesforceConnector var1, T var2) {
      super(var1, var2);
   }

   @Override
   protected void mainObjectDeleted() {
      ((SalesforceWizardEntity)this.getDependantObject()).setConnector(null);
      super.mainObjectDeleted();
   }

   @Override
   public boolean isStrong() {
      return false;
   }
}
