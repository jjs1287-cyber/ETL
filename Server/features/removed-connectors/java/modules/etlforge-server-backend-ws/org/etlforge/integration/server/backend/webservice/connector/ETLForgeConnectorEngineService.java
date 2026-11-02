package org.etlforge.integration.server.backend.webservice.connector;

import java.net.URL;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

public interface ETLForgeConnectorEngineService extends Service {
   String getkonga_connector_engineAddress();

   ETLForgeConnectorEngine getkonga_connector_engine() throws ServiceException;

   ETLForgeConnectorEngine getkonga_connector_engine(URL var1) throws ServiceException;
}
