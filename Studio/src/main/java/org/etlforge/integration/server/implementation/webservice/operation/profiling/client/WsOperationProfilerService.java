package org.etlforge.integration.server.implementation.webservice.operation.profiling.client;

import java.net.URL;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

public interface WsOperationProfilerService extends Service {
   String getkonga_operation_profilerAddress();

   WsOperationProfiler getkonga_operation_profiler() throws ServiceException;

   WsOperationProfiler getkonga_operation_profiler(URL var1) throws ServiceException;
}
