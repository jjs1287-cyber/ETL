package com.etlforge.integration.server.implementation.webservice.license;

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

public class WsLicenseServiceServiceLocator extends Service implements WsLicenseServiceService {
   private String konga_licenseservice_address = "http://localhost/axis/konga_licenseservice";
   private String konga_licenseserviceWSDDServiceName = "konga_licenseservice";
   private HashSet ports = null;

   public WsLicenseServiceServiceLocator() {
   }

   public WsLicenseServiceServiceLocator(EngineConfiguration var1) {
      super(var1);
   }

   public WsLicenseServiceServiceLocator(String var1, QName var2) throws ServiceException {
      super(var1, var2);
   }

   @Override
   public String getkonga_licenseserviceAddress() {
      return this.konga_licenseservice_address;
   }

   public String getkonga_licenseserviceWSDDServiceName() {
      return this.konga_licenseserviceWSDDServiceName;
   }

   public void setkonga_licenseserviceWSDDServiceName(String var1) {
      this.konga_licenseserviceWSDDServiceName = var1;
   }

   @Override
   public WsLicenseService getkonga_licenseservice() throws ServiceException {
      URL var1;
      try {
         var1 = new URL(this.konga_licenseservice_address);
      } catch (MalformedURLException var3) {
         throw new ServiceException(var3);
      }

      return this.getkonga_licenseservice(var1);
   }

   @Override
   public WsLicenseService getkonga_licenseservice(URL var1) throws ServiceException {
      try {
         Konga_licenseserviceSoapBindingStub var2 = new Konga_licenseserviceSoapBindingStub(var1, this);
         var2.setPortName(this.getkonga_licenseserviceWSDDServiceName());
         return var2;
      } catch (AxisFault var3) {
         return null;
      }
   }

   public void setkonga_licenseserviceEndpointAddress(String var1) {
      this.konga_licenseservice_address = var1;
   }

   @Override
   public Remote getPort(Class var1) throws ServiceException {
      try {
         if (WsLicenseService.class.isAssignableFrom(var1)) {
            Konga_licenseserviceSoapBindingStub var2 = new Konga_licenseserviceSoapBindingStub(new URL(this.konga_licenseservice_address), this);
            var2.setPortName(this.getkonga_licenseserviceWSDDServiceName());
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
      if ("konga_licenseservice".equals(var3)) {
         return this.getkonga_licenseservice();
      }

      Remote var4 = this.getPort(var2);
      ((Stub)var4).setPortName(var1);
      return var4;
   }

   @Override
   public QName getServiceName() {
      return new QName("http://license.webservice.implementation.server.integration.etlforge.invalid", "WsLicenseServiceService");
   }

   @Override
   public Iterator getPorts() {
      if (this.ports == null) {
         this.ports = new HashSet();
         this.ports.add(new QName("http://license.webservice.implementation.server.integration.etlforge.invalid", "konga_licenseservice"));
      }

      return this.ports.iterator();
   }

   public void setEndpointAddress(String var1, String var2) throws ServiceException {
      if ("konga_licenseservice".equals(var1)) {
         this.setkonga_licenseserviceEndpointAddress(var2);
      } else {
         throw new ServiceException(" Cannot set Endpoint Address for Unknown Port" + var1);
      }
   }

   public void setEndpointAddress(QName var1, String var2) throws ServiceException {
      this.setEndpointAddress(var1.getLocalPart(), var2);
   }
}
