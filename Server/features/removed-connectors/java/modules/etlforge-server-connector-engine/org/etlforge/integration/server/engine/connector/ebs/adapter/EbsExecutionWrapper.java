package org.etlforge.integration.server.engine.connector.ebs.adapter;

import com.ubs.core.adapter.ebs.common.schema.EbsRecord;
import com.ubs.core.adapter.ebs.connection.EbsExecutionConnection;
import com.ubs.core.adapter.ebs.interaction.EbsExecutionInteraction;
import com.ubs.core.adapter.exception.AdapterExecutionException;
import java.util.Properties;
import org.xml.sax.SAXException;

public class EbsExecutionWrapper {
   private final EbsExecutionConnection connection;

   public EbsExecutionWrapper(EbsExecutionConnection var1) {
      this.connection = var1;
   }

   public String execute(String var1, String var2) throws AdapterExecutionException, SAXException, InterruptedException {
      String var3 = this.executeRequest(var1, var2);
      int var4 = EbsRequestIdParser.obtainEbsRequestId(var3);
      EbsMonitorWrapper var5 = new EbsMonitorWrapper(this.connection, var4);
      EbsMonitorDelayStrategy var6 = new EbsMonitorDelayStrategy();

      while (true) {
         var5.execute();
         if (var5.isDone()) {
            return var5.toXmlString();
         }

         var6.delay();
      }
   }

   String executeRequest(String var1, String var2) throws AdapterExecutionException {
      Properties var3 = new Properties();
      var3.put("programname", var1);
      EbsExecutionInteraction var4 = new EbsExecutionInteraction(this.connection);
      EbsRecord var5 = new EbsRecord();
      var5.setRecord(var2);
      return var4.execute(var5, var3).getRecord().toString();
   }
}
