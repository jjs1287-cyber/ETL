package com.etlforge.integration.server.implementation.webservice.operation.profiling;

import com.google.common.base.Function;
import com.etlforge.integration.operation.profiling.data.DefaultProfilingResult;
import com.etlforge.integration.operation.profiling.data.ProfilingResult;
import com.etlforge.integration.operation.profiling.data.TimingData;
import com.etlforge.integration.operation.profiling.data.xml.TimingDataXmlParser;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.server.implementation.webservice.operation.profiling.client.WsProfilingResult;
import org.etlforge.util.zip.ZipUtils;

final class ProfilingResultConverter implements Function<WsProfilingResult, ProfilingResult> {
   public ProfilingResult apply(WsProfilingResult var1) {
      try {
         OperationId var2 = new OperationId(var1.getOperationGuid());
         DefaultProfilingResult var3 = new DefaultProfilingResult(var2);
         var3.setErrors(unpack(var1.getOperationErrors()));
         var3.setWarnings(unpack(var1.getOperationWarnings()));

         for (String var7 : var1.getProfilingData()) {
            var3.addTimingData(unpackProfilingData(var7));
         }

         return var3;
      } catch (Exception var8) {
         var8.printStackTrace();
         return null;
      }
   }

   private static String unpack(String var0) throws Exception {
      return ZipUtils.base64DecodeAndInflateString(var0);
   }

   private static TimingData unpackProfilingData(String var0) throws Exception {
      String var1 = unpack(var0);
      return TimingDataXmlParser.parseXmlAsString(var1);
   }
}
