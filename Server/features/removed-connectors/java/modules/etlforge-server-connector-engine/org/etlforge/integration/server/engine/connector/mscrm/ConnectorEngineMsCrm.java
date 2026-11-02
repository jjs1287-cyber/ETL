package org.etlforge.integration.server.engine.connector.mscrm;

import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.mscrm.metadata.BasicEntityDetails;
import org.etlforge.integration.mscrm.metadata.MSCrmMetaDataException;
import org.etlforge.integration.mscrm.metadata.MSCrmObject;
import org.etlforge.integration.mscrm.query.FetchXml;
import org.etlforge.integration.server.engine.connector.framework.ConnectorEngine;
import org.etlforge.integration.server.engine.connector.framework.ConnectorObjectList;
import org.etlforge.integration.server.engine.connector.framework.ConnectorXsdSchemaFiles;
import org.etlforge.integration.server.engine.connector.mscrm.adapter.ConnectorEngineMsCrmAdapter;

public class ConnectorEngineMsCrm implements ConnectorEngine {
   private final ConnectorEngineMsCrmAdapter adapter;

   ConnectorEngineMsCrm(ConnectorEndpointMsCrm var1) {
      this.adapter = new ConnectorEngineMsCrmAdapter(var1);
   }

   public ArrayList<BasicEntityDetails> describeAllMSCrmObjects() {
      return this.adapter.describeAllMSCrmObjects();
   }

   public MSCrmObject getDetailedMetaData(String var1) throws MSCrmMetaDataException {
      return this.adapter.getDetailedMetaData(var1);
   }

   public List<FetchXml> getSavedQueries(String var1) throws MSCrmMetaDataException {
      return this.adapter.getSavedQueries(var1);
   }

   @Override
   public ConnectorObjectList getConnectorObjectList(List<String> var1, String var2) {
      return this.adapter.getConnectorObjectList();
   }

   @Override
   public ConnectorXsdSchemaFiles getConnectorSchemaFiles(boolean var1, List<String> var2) {
      ConnectorInputValidatorMsCrm var3 = new ConnectorInputValidatorMsCrm(var2);
      var3.validatecomplete();
      return this.adapter.getConnectorXsdSchemas(var3.crmOperation, var3.crmObject);
   }

   @Override
   public void executeConnectorFileUtf8(List<String> var1, String var2, String var3) {
      ConnectorInputValidatorMsCrm var4 = new ConnectorInputValidatorMsCrm(var1);
      var4.validatecomplete();
      this.adapter.executeConnectorFileUtf8(var4.crmOperation, var4.crmObject, var2, var3);
   }
}
