package org.etlforge.integration.data.entity.sfdc;

import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.SalesforceConnector;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.integration.data.entity.id.SalesforceConnectorId;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.integration.data.validation.ValidationStatus;

public final class SalesforceWizardEntityUtils {
   public static final String CONNECTOR_ID = "connector_id";

   public static <T extends IntegrationEntity & SalesforceWizardEntity> SalesforceConnectorId getConnectorId(T var0) {
      synchronized (var0.getDataLock()) {
         String var2 = var0.getProperty("connector_id");
         return StringUtils.isEmpty(var2) ? null : new SalesforceConnectorId(var2);
      }
   }

   public static <T extends IntegrationEntity & SalesforceWizardEntity> void setConnector(T var0, SalesforceConnector var1) {
      var0.setStringProperty("connector_id", var1 == null ? null : var1.getID().toString());
   }

   public static <T extends IntegrationEntity & SalesforceWizardEntity> void validateWizardHasConnector(T var0, ValidationMessageCollector var1) {
      if (((SalesforceWizardEntity)var0).getConnectorId() == null) {
         var1.addMessage("Must provide a Salesforce Connector.", ValidationStatus.INVALID);
      }
   }

   private SalesforceWizardEntityUtils() {
   }
}
