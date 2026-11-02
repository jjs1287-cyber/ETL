package org.etlforge.integration.server.engine.connector.sap.adapter;

import com.ubs.core.adapter.Record;
import com.ubs.core.adapter.exception.AdapterExecutionException;
import com.ubs.core.adapter.sap.common.schema.SapRecord;
import com.ubs.core.adapter.sap.idoc.connection.SapIDocExecutionConnection;
import com.ubs.core.adapter.sap.idoc.interaction.SapIDocExecutionInteraction;
import java.util.Properties;
import org.etlforge.integration.server.engine.connector.ConnectorExecutionSystem;

public class SapIDocEngineExecutionWrapper {
   final SapIDocExecutionConnection sec;

   public SapIDocEngineExecutionWrapper(SapIDocExecutionConnection var1) {
      this.sec = var1;
   }

   public void executeFunctionFile(String var1, String var2, String var3, String var4) throws AdapterExecutionException {
      String var5 = ConnectorExecutionSystem.readFileUTF8(var3);
      String var6 = this.executeFunction(var1, var2, var5);
      ConnectorExecutionSystem.writeFileUTF8(var4, var6);
   }

   private String executeFunction(String var1, String var2, String var3) throws AdapterExecutionException {
      Properties var4 = new Properties();
      var4.put("idoctype", var2);
      SapRecord var5 = new SapRecord();
      var5.setRecord(var3);
      SapIDocExecutionInteraction var6 = this.sec.createInteraction();
      Record var7 = var6.execute(var5, var4);
      if (var7 == null) {
         throw new AdapterExecutionException("No record returns");
      } else {
         return (String)var7.getRecord();
      }
   }
}
