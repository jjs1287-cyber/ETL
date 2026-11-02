package org.etlforge.integration.server.engine.connector.netsuite.ws;

import com.google.common.collect.Lists;
import java.util.List;
import org.etlforge.integration.server.engine.connector.exception.ConnectorServerRuntimeException;

public class NetSuiteResponseStatus {
   boolean isSuccess = true;
   List<NetSuiteResponseStatusDetail> statusDetails = Lists.newArrayList();

   void addResponseStatusDetail(NetSuiteResponseStatusDetail var1) {
      this.statusDetails.add(var1);
   }

   void throwExceptionOnError() {
      if (!this.isSuccess) {
         throw new ConnectorServerRuntimeException(this.toString());
      }
   }

   @Override
   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("isSuccess=");
      var1.append(this.isSuccess);
      var1.append('\n');
      if (this.statusDetails == null) {
         return var1.toString();
      }

      for (NetSuiteResponseStatusDetail var3 : this.statusDetails) {
         var1.append(var3);
         var1.append('\n');
      }

      return var1.toString();
   }
}
