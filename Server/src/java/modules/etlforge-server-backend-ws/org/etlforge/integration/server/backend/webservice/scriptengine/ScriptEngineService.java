package org.etlforge.integration.server.backend.webservice.scriptengine;

import java.net.URL;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

public interface ScriptEngineService extends Service {
   String getkonga_script_engineAddress();

   ScriptEngine getkonga_script_engine() throws ServiceException;

   ScriptEngine getkonga_script_engine(URL var1) throws ServiceException;
}
