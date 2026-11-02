package org.etlforge.integration.server.implementation.webservice.interchange.xml.client;

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

public class XmlInfoProviderServiceLocator extends Service implements XmlInfoProviderService {
   private String konga_xml_infoprovider_address = "http://localhost/axis/konga_xml_infoprovider";
   private String konga_xml_infoproviderWSDDServiceName = "konga_xml_infoprovider";
   private HashSet ports = null;

   public XmlInfoProviderServiceLocator() {
   }

   public XmlInfoProviderServiceLocator(EngineConfiguration var1) {
      super(var1);
   }

   public XmlInfoProviderServiceLocator(String var1, QName var2) throws ServiceException {
      super(var1, var2);
   }

   @Override
   public String getkonga_xml_infoproviderAddress() {
      return this.konga_xml_infoprovider_address;
   }

   public String getkonga_xml_infoproviderWSDDServiceName() {
      return this.konga_xml_infoproviderWSDDServiceName;
   }

   public void setkonga_xml_infoproviderWSDDServiceName(String var1) {
      this.konga_xml_infoproviderWSDDServiceName = var1;
   }

   @Override
   public XmlInfoProvider getkonga_xml_infoprovider() throws ServiceException {
      URL var1;
      try {
         var1 = new URL(this.konga_xml_infoprovider_address);
      } catch (MalformedURLException var3) {
         throw new ServiceException(var3);
      }

      return this.getkonga_xml_infoprovider(var1);
   }

   @Override
   public XmlInfoProvider getkonga_xml_infoprovider(URL var1) throws ServiceException {
      try {
         Konga_xml_infoproviderSoapBindingStub var2 = new Konga_xml_infoproviderSoapBindingStub(var1, this);
         var2.setPortName(this.getkonga_xml_infoproviderWSDDServiceName());
         return var2;
      } catch (AxisFault var3) {
         return null;
      }
   }

   public void setkonga_xml_infoproviderEndpointAddress(String var1) {
      this.konga_xml_infoprovider_address = var1;
   }

   @Override
   public Remote getPort(Class var1) throws ServiceException {
      try {
         if (XmlInfoProvider.class.isAssignableFrom(var1)) {
            Konga_xml_infoproviderSoapBindingStub var2 = new Konga_xml_infoproviderSoapBindingStub(new URL(this.konga_xml_infoprovider_address), this);
            var2.setPortName(this.getkonga_xml_infoproviderWSDDServiceName());
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
      if ("konga_xml_infoprovider".equals(var3)) {
         return this.getkonga_xml_infoprovider();
      }

      Remote var4 = this.getPort(var2);
      ((Stub)var4).setPortName(var1);
      return var4;
   }

   @Override
   public QName getServiceName() {
      return new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.xml.XmlInfoProvider", "XmlInfoProviderService");
   }

   @Override
   public Iterator getPorts() {
      if (this.ports == null) {
         this.ports = new HashSet();
         this.ports.add(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.xml.XmlInfoProvider", "konga_xml_infoprovider"));
      }

      return this.ports.iterator();
   }

   public void setEndpointAddress(String var1, String var2) throws ServiceException {
      if ("konga_xml_infoprovider".equals(var1)) {
         this.setkonga_xml_infoproviderEndpointAddress(var2);
      } else {
         throw new ServiceException(" Cannot set Endpoint Address for Unknown Port" + var1);
      }
   }

   public void setEndpointAddress(QName var1, String var2) throws ServiceException {
      this.setEndpointAddress(var1.getLocalPart(), var2);
   }
}
