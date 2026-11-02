package org.etlforge.integration.server.engine.connector.ebs.adapter;

import com.google.common.base.Strings;
import com.ubs.core.adapter.ebs.connection.EbsDesignConnection;
import com.ubs.core.adapter.ebs.connection.EbsExecutionConnection;
import com.ubs.core.adapter.exception.AdapterExecutionException;
import java.util.Map;
import org.etlforge.integration.server.engine.connector.ConnectorExecutionSystem;
import org.etlforge.integration.server.engine.connector.ebs.ConnectorEndpointEbs;
import org.etlforge.integration.server.engine.connector.exception.ConnectorAdapterRuntimeException;
import org.etlforge.integration.server.engine.connector.framework.ConnectorXsdSchemaFiles;
import org.xml.sax.SAXException;

public class ConnectorEngineEbsAdapter {
   final ConnectorEndpointEbsAdapter adapter;

   public ConnectorEngineEbsAdapter(ConnectorEndpointEbs var1) {
      this.adapter = var1.getConnectorEndpointEbsAdapter();
   }

   public ConnectorXsdSchemaFiles getConnectorXsdSchemas(String var1) {
      EbsDesignConnection var2 = EbsConnectionWrapper.getOpenDesignConnection(this.adapter.getEbsConnectionProperties());

      try {
         EbsRequestSchemaWrapper var3 = new EbsRequestSchemaWrapper(var2, var1);
         String var4 = var3.getRequestSchema();
         String var5 = var3.getRequestRootElementExpandedName();
         return new ConnectorXsdSchemaFiles(
            var4,
            var5,
            "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?> <xs:schema version=\"1.0\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\t<xs:element name=\"output\">\t<xs:complexType><xs:sequence>\t<xs:element name=\"result\" type=\"xs:string\" />\t<xs:element name=\"status\" type=\"xs:string\" />\t<xs:element name=\"phase\" type=\"xs:string\" />\t<xs:element name=\"description\" type=\"xs:string\" minOccurs=\"0\" />\t</xs:sequence></xs:complexType>  \t</xs:element></xs:schema>",
            "output"
         );
      } catch (Exception var12) {
         throw new ConnectorAdapterRuntimeException(var12);
      } finally {
         EbsConnectionWrapper.close(var2);
      }
   }

   public void executeConnectorFileUtf8(String var1, String var2, String var3) {
      String var4 = ConnectorExecutionSystem.readFileUTF8(var2);
      String var5 = this.executeEbsConnector(var1, var4);
      ConnectorExecutionSystem.writeFileUTF8(var3, var5);
   }

   private String executeEbsConnector(String var1, String var2) {
      EbsExecutionConnection var3 = EbsConnectionWrapper.getOpenExecutionConnection(this.adapter.getEbsConnectionProperties());

      try {
         EbsExecutionWrapper var4 = new EbsExecutionWrapper(var3);
         return var4.execute(var1, var2);
      } catch (AdapterExecutionException var12) {
         throw new ConnectorAdapterRuntimeException(var12);
      } catch (SAXException var13) {
         throw new ConnectorAdapterRuntimeException(var13);
      } catch (InterruptedException var14) {
         throw new ConnectorAdapterRuntimeException(var14);
      } finally {
         EbsConnectionWrapper.close(var3);
      }
   }

   public Map<String, String> getObjectList(String var1) {
      EbsDesignConnection var2 = EbsConnectionWrapper.getOpenDesignConnection(this.adapter.getEbsConnectionProperties());

      try {
         EbsDesignWrapper var3 = new EbsDesignWrapper(var2);
         return Strings.isNullOrEmpty(var1) ? var3.getCatalog() : var3.getPrograms(var1);
      } catch (Exception var8) {
         throw new ConnectorAdapterRuntimeException(var8);
      } finally {
         EbsConnectionWrapper.close(var2);
      }
   }
}
