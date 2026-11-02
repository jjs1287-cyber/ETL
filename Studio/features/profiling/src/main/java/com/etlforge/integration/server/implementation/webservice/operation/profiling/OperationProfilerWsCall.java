package com.etlforge.integration.server.implementation.webservice.operation.profiling;

import com.etlforge.integration.operation.profiling.data.ProfilingResult;
import com.etlforge.integration.operation.profiling.server.OperationProfiler;
import com.etlforge.integration.operation.profiling.server.OperationProfilerCallback;
import java.rmi.RemoteException;
import javax.xml.rpc.ServiceException;
import org.etlforge.integration.client.server.IntegrationServerException;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.server.webservice.CppWebServiceConfiguration;
import org.etlforge.integration.client.server.webservice.CppWebServiceServerCall;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.server.implementation.webservice.operation.profiling.client.WsOperationProfiler;
import org.etlforge.integration.server.implementation.webservice.operation.profiling.client.WsOperationProfilerServiceLocator;
import org.etlforge.integration.server.implementation.webservice.operation.profiling.client.WsProfilingResult;
import org.etlforge.lang.Arrays2;

public final class OperationProfilerWsCall extends CppWebServiceServerCall implements OperationProfiler {
   private static final String SERVICE_NAME = "konga_operation_deployer";

   public OperationProfilerWsCall(CppWebServiceConfiguration var1) {
      super(var1);
   }

   public void profileOperation(OperationId var1, OperationProfilerCallback var2) {
      try {
         WsOperationProfiler var3 = this.getProfiler(var2);
         if (var3 == null) {
            return;
         }

         WsProfilingResult[] var4 = var3.runProfiler("", "", var1.toString());
         var2.profilingComplete(var1, Arrays2.transform(var4, ProfilingResult.class, new ProfilingResultConverter()));
      } catch (RemoteException var5) {
         var2.caught(this.convert(var5));
      } catch (IntegrationServerException var6) {
         var2.caught(var6);
      }
   }

   private WsOperationProfiler getProfiler(ServerCallback var1) throws IntegrationServerException {
      try {
         WsOperationProfilerServiceLocator var2 = new WsOperationProfilerServiceLocator();
         StringBuilder var3 = new StringBuilder();
         if (!this.getConfiguration().configureWebservice(var2, "konga_operation_deployer", var3, var1)) {
            return null;
         }

         var2.setkonga_operation_profilerEndpointAddress(var3.toString());
         return var2.getkonga_operation_profiler();
      } catch (ServiceException var4) {
         throw new IntegrationServerException("Failed to call webservice: " + var4.getMessage(), var4);
      }
   }
}
