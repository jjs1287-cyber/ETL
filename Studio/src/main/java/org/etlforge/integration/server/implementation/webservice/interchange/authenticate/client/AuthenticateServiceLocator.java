package org.etlforge.integration.server.implementation.webservice.interchange.authenticate.client;

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

public class AuthenticateServiceLocator extends Service implements AuthenticateService {
   private String konga_authenticate_address = "http://localhost/axis/konga_authenticate";
   private String konga_authenticateWSDDServiceName = "konga_authenticate";
   private HashSet ports = null;

   public AuthenticateServiceLocator() {
   }

   public AuthenticateServiceLocator(EngineConfiguration var1) {
      super(var1);
   }

   public AuthenticateServiceLocator(String var1, QName var2) throws ServiceException {
      super(var1, var2);
   }

   @Override
   public String getkonga_authenticateAddress() {
      return this.konga_authenticate_address;
   }

   public String getkonga_authenticateWSDDServiceName() {
      return this.konga_authenticateWSDDServiceName;
   }

   public void setkonga_authenticateWSDDServiceName(String var1) {
      this.konga_authenticateWSDDServiceName = var1;
   }

   @Override
   public Authenticate getkonga_authenticate() throws ServiceException {
      URL var1;
      try {
         var1 = new URL(this.konga_authenticate_address);
      } catch (MalformedURLException var3) {
         throw new ServiceException(var3);
      }

      return this.getkonga_authenticate(var1);
   }

   @Override
   public Authenticate getkonga_authenticate(URL var1) throws ServiceException {
      try {
         Konga_authenticateSoapBindingStub var2 = new Konga_authenticateSoapBindingStub(var1, this);
         var2.setPortName(this.getkonga_authenticateWSDDServiceName());
         return var2;
      } catch (AxisFault var3) {
         return null;
      }
   }

   public void setkonga_authenticateEndpointAddress(String var1) {
      this.konga_authenticate_address = var1;
   }

   @Override
   public Remote getPort(Class var1) throws ServiceException {
      try {
         if (Authenticate.class.isAssignableFrom(var1)) {
            Konga_authenticateSoapBindingStub var2 = new Konga_authenticateSoapBindingStub(new URL(this.konga_authenticate_address), this);
            var2.setPortName(this.getkonga_authenticateWSDDServiceName());
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
      if ("konga_authenticate".equals(var3)) {
         return this.getkonga_authenticate();
      }

      Remote var4 = this.getPort(var2);
      ((Stub)var4).setPortName(var1);
      return var4;
   }

   @Override
   public QName getServiceName() {
      return new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.authenticate.Authenticate", "AuthenticateService");
   }

   @Override
   public Iterator getPorts() {
      if (this.ports == null) {
         this.ports = new HashSet();
         this.ports
            .add(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.authenticate.Authenticate", "konga_authenticate"));
      }

      return this.ports.iterator();
   }

   public void setEndpointAddress(String var1, String var2) throws ServiceException {
      if ("konga_authenticate".equals(var1)) {
         this.setkonga_authenticateEndpointAddress(var2);
      } else {
         throw new ServiceException(" Cannot set Endpoint Address for Unknown Port" + var1);
      }
   }

   public void setEndpointAddress(QName var1, String var2) throws ServiceException {
      this.setEndpointAddress(var1.getLocalPart(), var2);
   }
}
