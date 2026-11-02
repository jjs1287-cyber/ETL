package org.etlforge.integration.server.backend.webservice.json;

import java.net.URL;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

public interface JsonEngineService extends Service {
   String getkonga_json_engineAddress();

   JsonEngine getkonga_json_engine() throws ServiceException;

   JsonEngine getkonga_json_engine(URL var1) throws ServiceException;
}
