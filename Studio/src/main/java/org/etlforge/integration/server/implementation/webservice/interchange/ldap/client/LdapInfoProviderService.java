package org.etlforge.integration.server.implementation.webservice.interchange.ldap.client;

import java.net.URL;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

public interface LdapInfoProviderService extends Service {
   String getkonga_ldap_infoproviderAddress();

   LdapInfoProvider getkonga_ldap_infoprovider() throws ServiceException;

   LdapInfoProvider getkonga_ldap_infoprovider(URL var1) throws ServiceException;
}
