package org.etlforge.integration.server.implementation.webservice.interchange.connection.client;

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

public class TestConnectionServiceLocator extends Service implements TestConnectionService {
   private String konga_test_connection_address = "http://localhost/axis/konga_test_connection";
   private String konga_test_connectionWSDDServiceName = "konga_test_connection";
   private HashSet ports = null;

   public TestConnectionServiceLocator() {
   }

   public TestConnectionServiceLocator(EngineConfiguration var1) {
      super(var1);
   }

   public TestConnectionServiceLocator(String var1, QName var2) throws ServiceException {
      super(var1, var2);
   }

   @Override
   public String getkonga_test_connectionAddress() {
      return this.konga_test_connection_address;
   }

   public String getkonga_test_connectionWSDDServiceName() {
      return this.konga_test_connectionWSDDServiceName;
   }

   public void setkonga_test_connectionWSDDServiceName(String var1) {
      this.konga_test_connectionWSDDServiceName = var1;
   }

   @Override
   public TestConnection getkonga_test_connection() throws ServiceException {
      URL var1;
      try {
         var1 = new URL(this.konga_test_connection_address);
      } catch (MalformedURLException var3) {
         throw new ServiceException(var3);
      }

      return this.getkonga_test_connection(var1);
   }

   @Override
   public TestConnection getkonga_test_connection(URL var1) throws ServiceException {
      try {
         Konga_test_connectionSoapBindingStub var2 = new Konga_test_connectionSoapBindingStub(var1, this);
         var2.setPortName(this.getkonga_test_connectionWSDDServiceName());
         return var2;
      } catch (AxisFault var3) {
         return null;
      }
   }

   public void setkonga_test_connectionEndpointAddress(String var1) {
      this.konga_test_connection_address = var1;
   }

   @Override
   public Remote getPort(Class var1) throws ServiceException {
      try {
         if (TestConnection.class.isAssignableFrom(var1)) {
            Konga_test_connectionSoapBindingStub var2 = new Konga_test_connectionSoapBindingStub(new URL(this.konga_test_connection_address), this);
            var2.setPortName(this.getkonga_test_connectionWSDDServiceName());
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
      if ("konga_test_connection".equals(var3)) {
         return this.getkonga_test_connection();
      }

      Remote var4 = this.getPort(var2);
      ((Stub)var4).setPortName(var1);
      return var4;
   }

   @Override
   public QName getServiceName() {
      return new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.connection.TestConnection", "TestConnectionService");
   }

   @Override
   public Iterator getPorts() {
      if (this.ports == null) {
         this.ports = new HashSet();
         this.ports
            .add(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.connection.TestConnection", "konga_test_connection"));
      }

      return this.ports.iterator();
   }

   public void setEndpointAddress(String var1, String var2) throws ServiceException {
      if ("konga_test_connection".equals(var1)) {
         this.setkonga_test_connectionEndpointAddress(var2);
      } else {
         throw new ServiceException(" Cannot set Endpoint Address for Unknown Port" + var1);
      }
   }

   public void setEndpointAddress(QName var1, String var2) throws ServiceException {
      this.setEndpointAddress(var1.getLocalPart(), var2);
   }
}
