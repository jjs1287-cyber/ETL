package org.etlforge.integration.server.backend.webservice.netsuite;

import java.net.URL;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

public interface NetSuiteDesignEngineService extends Service {
   String getkonga_netsuitedesign_engineAddress();

   NetSuiteDesignEngine getkonga_netsuitedesign_engine() throws ServiceException;

   NetSuiteDesignEngine getkonga_netsuitedesign_engine(URL var1) throws ServiceException;
}
