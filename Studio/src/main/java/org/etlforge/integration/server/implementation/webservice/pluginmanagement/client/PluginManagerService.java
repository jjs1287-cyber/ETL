package org.etlforge.integration.server.implementation.webservice.pluginmanagement.client;

import java.net.URL;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

public interface PluginManagerService extends Service {
   String getkonga_plugin_managementAddress();

   PluginManager getkonga_plugin_management() throws ServiceException;

   PluginManager getkonga_plugin_management(URL var1) throws ServiceException;
}
