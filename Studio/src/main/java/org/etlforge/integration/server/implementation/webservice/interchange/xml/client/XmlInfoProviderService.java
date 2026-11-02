package org.etlforge.integration.server.implementation.webservice.interchange.xml.client;

import java.net.URL;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

public interface XmlInfoProviderService extends Service {
   String getkonga_xml_infoproviderAddress();

   XmlInfoProvider getkonga_xml_infoprovider() throws ServiceException;

   XmlInfoProvider getkonga_xml_infoprovider(URL var1) throws ServiceException;
}
