package org.etlforge.integration.server.engine.connector.sap.adapter;

import com.ubs.core.adapter.Record;
import com.ubs.core.adapter.exception.AdapterExecutionException;
import com.ubs.core.adapter.sap.rfc.connection.SapExecutionConnection;
import com.ubs.core.adapter.sap.rfc.interaction.SapExecutionInteraction;
import java.util.Properties;

public class SapEngineExecutionWrapper {
   final SapExecutionConnection sec;

   public SapEngineExecutionWrapper(SapExecutionConnection var1) {
      this.sec = var1;
   }

   public void executeFunctionFile(String var1, String var2, String var3) throws AdapterExecutionException {
      Properties var4 = new Properties();
      var4.put("function", var1);
      var4.put("inputfile", var2);
      var4.put("outputfile", var3);
      SapExecutionInteraction var5 = this.sec.createInteraction();
      Record var6 = var5.execute(null, var4);
   }
}
