package com.etlforge.integration.server.implementation.webservice.license.client;

import java.net.URL;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

public interface WsLicenseServiceService extends Service {
   String getkonga_licenseserviceAddress();

   WsLicenseService getkonga_licenseservice() throws ServiceException;

   WsLicenseService getkonga_licenseservice(URL var1) throws ServiceException;
}
