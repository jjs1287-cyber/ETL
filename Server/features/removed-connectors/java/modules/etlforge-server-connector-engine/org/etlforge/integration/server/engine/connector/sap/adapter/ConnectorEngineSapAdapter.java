package org.etlforge.integration.server.engine.connector.sap.adapter;

import com.google.common.base.Strings;
import com.ubs.core.adapter.exception.AdapterExecutionException;
import com.ubs.core.adapter.exception.AdapterSchemaException;
import com.ubs.core.adapter.sap.idoc.connection.SapIDocDesignConnection;
import com.ubs.core.adapter.sap.idoc.connection.SapIDocExecutionConnection;
import com.ubs.core.adapter.sap.rfc.connection.SapDesignConnection;
import com.ubs.core.adapter.sap.rfc.connection.SapExecutionConnection;
import java.util.HashMap;
import java.util.Map;
import org.etlforge.integration.server.engine.connector.exception.ConnectorAdapterRuntimeException;
import org.etlforge.integration.server.engine.connector.framework.ConnectorObjectList;
import org.etlforge.integration.server.engine.connector.framework.ConnectorXsdSchemaFiles;
import org.etlforge.integration.server.engine.connector.sap.ConnectorEndpointSap;

public class ConnectorEngineSapAdapter {
   final ConnectorEndpointSapAdapter adapter;

   public ConnectorEngineSapAdapter(ConnectorEndpointSap var1) {
      this.adapter = var1.getConnectorEndpointSapAdapter();
   }

   public static ConnectorObjectList getConnectorObjectListSapSystems() {
      HashMap var0 = new HashMap();
      var0.put("BAPI", "BAPI");
      var0.put("RFC", "RFC");
      var0.put("IDOC", "IDOC");
      return new ConnectorObjectList(false, "SAP Systems", var0);
   }

   public ConnectorObjectList getConnectorObjectList_RFC(boolean var1, boolean var2, String var3) {
      SapDesignConnection var4 = this.adapter.getOpenDesignConnection();

      try {
         SapEngineDesignWrapper var5 = new SapEngineDesignWrapper(var4);
         Map var6 = var5.getObjectList(var1, var2, var3);
         return new ConnectorObjectList(true, "Function Modules", var6);
      } catch (Exception var11) {
         throw new ConnectorAdapterRuntimeException(var11);
      } finally {
         SapConnectorConnectionAdapter.closeConnection(var4);
      }
   }

   public ConnectorObjectList getConnectorObjectList_IDoc(String var1, String var2) {
      SapIDocDesignConnection var3 = this.adapter.getOpenSapIDocDesignConnectionn();

      try {
         SapIDocEngineDesignWrapper var4 = new SapIDocEngineDesignWrapper(var3);
         if (!Strings.isNullOrEmpty(var1)) {
            Map var12 = var4.getIDocTypeList(var1, var2);
            return new ConnectorObjectList(true, "IDoc Type", var12);
         } else {
            Map var5 = var4.getMessageTypeList(var2);
            return new ConnectorObjectList(true, "Message Type", var5);
         }
      } catch (Exception var10) {
         throw new ConnectorAdapterRuntimeException(var10);
      } finally {
         SapConnectorConnectionAdapter.closeConnection(var3);
      }
   }

   public ConnectorXsdSchemaFiles getConnectorXsdSchemas_RFC(String var1) {
      SapDesignConnection var2 = this.adapter.getOpenDesignConnection();

      try {
         SapEngineDesignWrapper var3 = new SapEngineDesignWrapper(var2);
         var3.setSchemaManager(var1);
         return new ConnectorXsdSchemaFiles(
            var3.getRequestSchema(), var3.getRequestRootElementExpandedName(), var3.getResponseSchema(), var3.getResponseRootElementExpandedName()
         );
      } catch (AdapterSchemaException var9) {
         throw new ConnectorAdapterRuntimeException(var9);
      } finally {
         SapConnectorConnectionAdapter.closeConnection(var2);
      }
   }

   public ConnectorXsdSchemaFiles getConnectorXsdSchemas_IDOC(String var1, String var2) {
      SapIDocDesignConnection var3 = this.adapter.getOpenSapIDocDesignConnectionn();

      try {
         SapIDocEngineDesignWrapper var4 = new SapIDocEngineDesignWrapper(var3);
         var4.setSchemaManager(var1, var2);
         return new ConnectorXsdSchemaFiles(
            var4.getRequestSchema(), var4.getRequestRootElementExpandedName(), var4.getResponseSchema(), var4.getResponseRootElementExpandedName()
         );
      } catch (AdapterSchemaException var11) {
         throw new ConnectorAdapterRuntimeException(var11);
      } catch (Exception var12) {
         throw new ConnectorAdapterRuntimeException(var12);
      } finally {
         SapConnectorConnectionAdapter.closeConnection(var3);
      }
   }

   public void executeConnector_RFC(String var1, String var2, String var3) {
      SapExecutionConnection var4 = this.adapter.getOpenExecutionConnection();

      try {
         SapEngineExecutionWrapper var5 = new SapEngineExecutionWrapper(var4);
         var5.executeFunctionFile(var1, var2, var3);
      } catch (AdapterExecutionException var9) {
         throw new ConnectorAdapterRuntimeException(var9);
      } finally {
         SapConnectorConnectionAdapter.closeConnection(var4);
      }
   }

   public void executeConnector_IDOC(String var1, String var2, String var3, String var4) {
      SapIDocExecutionConnection var5 = this.adapter.getOpenSapIDocExecutionConnection();

      try {
         SapIDocEngineExecutionWrapper var6 = new SapIDocEngineExecutionWrapper(var5);
         var6.executeFunctionFile(var1, var2, var3, var4);
      } catch (AdapterExecutionException var11) {
         throw new ConnectorAdapterRuntimeException(var11);
      } catch (Exception var12) {
         throw new ConnectorAdapterRuntimeException(var12);
      } finally {
         SapConnectorConnectionAdapter.closeConnection(var5);
      }
   }
}
