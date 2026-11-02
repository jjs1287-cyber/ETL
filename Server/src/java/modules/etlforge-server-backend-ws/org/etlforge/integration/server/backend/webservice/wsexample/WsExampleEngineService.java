package org.etlforge.integration.server.backend.webservice.wsexample;

import java.net.URL;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

public interface WsExampleEngineService extends Service {
   String getkonga_wsexample_engineAddress();

   WsExampleEngine getkonga_wsexample_engine() throws ServiceException;

   WsExampleEngine getkonga_wsexample_engine(URL var1) throws ServiceException;
}
