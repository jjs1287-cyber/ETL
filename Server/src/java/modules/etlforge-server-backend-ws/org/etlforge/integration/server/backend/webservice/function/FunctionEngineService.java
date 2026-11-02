package org.etlforge.integration.server.backend.webservice.function;

import java.net.URL;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

public interface FunctionEngineService extends Service {
   String getkonga_function_engineAddress();

   FunctionEngine getkonga_function_engine() throws ServiceException;

   FunctionEngine getkonga_function_engine(URL var1) throws ServiceException;
}
