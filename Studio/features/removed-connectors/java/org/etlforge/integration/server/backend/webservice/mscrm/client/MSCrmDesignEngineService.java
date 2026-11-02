package org.etlforge.integration.server.backend.webservice.mscrm.client;

import java.net.URL;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

public interface MSCrmDesignEngineService extends Service {
   String getkonga_mscrmdesign_engineAddress();

   MSCrmDesignEngine getkonga_mscrmdesign_engine() throws ServiceException;

   MSCrmDesignEngine getkonga_mscrmdesign_engine(URL var1) throws ServiceException;
}
