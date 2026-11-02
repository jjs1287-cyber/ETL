package org.etlforge.integration.client.wsdl;

import org.etlforge.integration.data.entity.webservicecall.WebServiceInfo;
import org.etlforge.system.Disposable;

public interface WsdlParser extends Disposable {
   WebServiceInfo parse(byte[] var1) throws WsdlParserException;

   @Override
   void dispose();
}
