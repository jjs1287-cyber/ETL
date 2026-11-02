package org.etlforge.integration.server.implementation.webservice.filemanagement.client;

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

public class FileManagerServiceLocator extends Service implements FileManagerService {
   private String konga_file_management_address = "http://localhost/axis/konga_file_management";
   private String konga_file_managementWSDDServiceName = "konga_file_management";
   private HashSet ports = null;

   public FileManagerServiceLocator() {
   }

   public FileManagerServiceLocator(EngineConfiguration var1) {
      super(var1);
   }

   public FileManagerServiceLocator(String var1, QName var2) throws ServiceException {
      super(var1, var2);
   }

   @Override
   public String getkonga_file_managementAddress() {
      return this.konga_file_management_address;
   }

   public String getkonga_file_managementWSDDServiceName() {
      return this.konga_file_managementWSDDServiceName;
   }

   public void setkonga_file_managementWSDDServiceName(String var1) {
      this.konga_file_managementWSDDServiceName = var1;
   }

   @Override
   public FileManager getkonga_file_management() throws ServiceException {
      URL var1;
      try {
         var1 = new URL(this.konga_file_management_address);
      } catch (MalformedURLException var3) {
         throw new ServiceException(var3);
      }

      return this.getkonga_file_management(var1);
   }

   @Override
   public FileManager getkonga_file_management(URL var1) throws ServiceException {
      try {
         Konga_file_managementSoapBindingStub var2 = new Konga_file_managementSoapBindingStub(var1, this);
         var2.setPortName(this.getkonga_file_managementWSDDServiceName());
         return var2;
      } catch (AxisFault var3) {
         return null;
      }
   }

   public void setkonga_file_managementEndpointAddress(String var1) {
      this.konga_file_management_address = var1;
   }

   @Override
   public Remote getPort(Class var1) throws ServiceException {
      try {
         if (FileManager.class.isAssignableFrom(var1)) {
            Konga_file_managementSoapBindingStub var2 = new Konga_file_managementSoapBindingStub(new URL(this.konga_file_management_address), this);
            var2.setPortName(this.getkonga_file_managementWSDDServiceName());
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
      if ("konga_file_management".equals(var3)) {
         return this.getkonga_file_management();
      }

      Remote var4 = this.getPort(var2);
      ((Stub)var4).setPortName(var1);
      return var4;
   }

   @Override
   public QName getServiceName() {
      return new QName("urn:org.etlforge.integration.server.implementation.webservice.filemanagement.FileManager", "FileManagerService");
   }

   @Override
   public Iterator getPorts() {
      if (this.ports == null) {
         this.ports = new HashSet();
         this.ports.add(new QName("urn:org.etlforge.integration.server.implementation.webservice.filemanagement.FileManager", "konga_file_management"));
      }

      return this.ports.iterator();
   }

   public void setEndpointAddress(String var1, String var2) throws ServiceException {
      if ("konga_file_management".equals(var1)) {
         this.setkonga_file_managementEndpointAddress(var2);
      } else {
         throw new ServiceException(" Cannot set Endpoint Address for Unknown Port" + var1);
      }
   }

   public void setEndpointAddress(QName var1, String var2) throws ServiceException {
      this.setEndpointAddress(var1.getLocalPart(), var2);
   }
}
