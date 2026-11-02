package org.etlforge.integration.server.implementation.webservice.sfdc.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Remote;
import java.util.HashSet;
import java.util.Iterator;
import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import org.apache.axis.AxisFault;
import org.apache.axis.EngineConfiguration;
import org.apache.axis.client.Service;
import org.apache.axis.client.Stub;

public class SalesforceInfoProviderServiceLocator extends Service implements SalesforceInfoProviderService {
   private String konga_salesforce_infoprovider_address = "http://localhost/axis/konga_salesforce_infoprovider";
   private String konga_salesforce_infoproviderWSDDServiceName = "konga_salesforce_infoprovider";
   private HashSet ports = null;

   public SalesforceInfoProviderServiceLocator() {
   }

   public SalesforceInfoProviderServiceLocator(EngineConfiguration var1) {
      super(var1);
   }

   public SalesforceInfoProviderServiceLocator(String var1, QName var2) throws ServiceException {
      super(var1, var2);
   }

   @Override
   public String getkonga_salesforce_infoproviderAddress() {
      return this.konga_salesforce_infoprovider_address;
   }

   public String getkonga_salesforce_infoproviderWSDDServiceName() {
      return this.konga_salesforce_infoproviderWSDDServiceName;
   }

   public void setkonga_salesforce_infoproviderWSDDServiceName(String var1) {
      this.konga_salesforce_infoproviderWSDDServiceName = var1;
   }

   @Override
   public SalesforceInfoProvider getkonga_salesforce_infoprovider() throws ServiceException {
      URL var1;
      try {
         var1 = new URL(this.konga_salesforce_infoprovider_address);
      } catch (MalformedURLException var3) {
         throw new ServiceException(var3);
      }

      return this.getkonga_salesforce_infoprovider(var1);
   }

   @Override
   public SalesforceInfoProvider getkonga_salesforce_infoprovider(URL var1) throws ServiceException {
      try {
         Konga_salesforce_infoproviderSoapBindingStub var2 = new Konga_salesforce_infoproviderSoapBindingStub(var1, this);
         var2.setPortName(this.getkonga_salesforce_infoproviderWSDDServiceName());
         return var2;
      } catch (AxisFault var3) {
         return null;
      }
   }

   public void setkonga_salesforce_infoproviderEndpointAddress(String var1) {
      this.konga_salesforce_infoprovider_address = var1;
   }

   @Override
   public Remote getPort(Class var1) throws ServiceException {
      try {
         if (SalesforceInfoProvider.class.isAssignableFrom(var1)) {
            Konga_salesforce_infoproviderSoapBindingStub var2 = new Konga_salesforce_infoproviderSoapBindingStub(
               new URL(this.konga_salesforce_infoprovider_address), this
            );
            var2.setPortName(this.getkonga_salesforce_infoproviderWSDDServiceName());
            return var2;
         }
      } catch (Throwable var3) {
         throw new ServiceException(var3);
      }

      throw new ServiceException("There is no stub implementation for the interface:  " + (var1 == null ? "null" : var1.getName()));
   }

   @Override
   public Remote getPort(QName var1, Class var2) throws ServiceException {
      if (var1 == null) {
         return this.getPort(var2);
      }

      String var3 = var1.getLocalPart();
      if ("konga_salesforce_infoprovider".equals(var3)) {
         return this.getkonga_salesforce_infoprovider();
      }

      Remote var4 = this.getPort(var2);
      ((Stub)var4).setPortName(var1);
      return var4;
   }

   @Override
   public QName getServiceName() {
      return new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "SalesforceInfoProviderService");
   }

   @Override
   public Iterator getPorts() {
      if (this.ports == null) {
         this.ports = new HashSet();
         this.ports.add(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "konga_salesforce_infoprovider"));
      }

      return this.ports.iterator();
   }

   public void setEndpointAddress(String var1, String var2) throws ServiceException {
      if ("konga_salesforce_infoprovider".equals(var1)) {
         this.setkonga_salesforce_infoproviderEndpointAddress(var2);
      } else {
         throw new ServiceException(" Cannot set Endpoint Address for Unknown Port" + var1);
      }
   }

   public void setEndpointAddress(QName var1, String var2) throws ServiceException {
      this.setEndpointAddress(var1.getLocalPart(), var2);
   }
}
