package org.etlforge.integration.server.implementation.webservice.interchange.checkout.client;

import java.net.URL;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

public interface CheckoutInterchangeDataService extends Service {
   String getkonga_checkout_interchange_dataAddress();

   CheckoutInterchangeData getkonga_checkout_interchange_data() throws ServiceException;

   CheckoutInterchangeData getkonga_checkout_interchange_data(URL var1) throws ServiceException;
}
