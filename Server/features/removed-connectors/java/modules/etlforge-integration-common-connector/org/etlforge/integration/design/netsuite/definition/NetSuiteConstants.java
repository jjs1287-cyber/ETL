package org.etlforge.integration.design.netsuite.definition;

import com.google.common.collect.Sets;
import java.util.Set;

public class NetSuiteConstants {
   public static final String ENVELOPENAMESPACE = "http://schemas.xmlsoap.org/soap/envelope/";
   public static final String ENVELOPENAMESPACEPREFIX = "soapenv";
   public static final String XSITYPENAMESPACE = "http://www.w3.org/2001/XMLSchema-instance";
   public static final String XSITYPENAMESPACEPREFIX = "xsi";
   public static final String XSITYPEQNAME = "xsi:type";
   public static final String ENVELOPENAME = "Envelope";
   public static final String ENVELOPEHEADER = "Header";
   public static final String ENVELOPEBODY = "Body";
   public static final String ENVELOPEFAULT = "Fault";
   public static final String TARGETNAMESPACEATTRIBUTE = "targetNamespace";
   public static final NetSuiteConstants instance = new NetSuiteConstants();
   public static final String xsdNamespace = "http://www.w3.org/2001/XMLSchema";
   public String[] supportedOperations = new String[]{"search", "deleteList", "upsertList", "updateList", "addList"};
   Set<String> standardNamespaces = Sets.newHashSet();

   private NetSuiteConstants() {
      this.standardNamespaces.add("http://www.w3.org/2001/XMLSchema");
      this.standardNamespaces.add("http://schemas.xmlsoap.org/wsdl/soap/");
      this.standardNamespaces.add("http://schemas.xmlsoap.org/wsdl/soap");
      this.standardNamespaces.add("http://schemas.xmlsoap.org/wsdl/");
      this.standardNamespaces.add("http://schemas.xmlsoap.org/wsdl");
   }

   public boolean isNetSuiteSearchOperation(String var1) {
      return var1.equals("search");
   }

   public boolean isStandardNamespace(String var1) {
      return this.standardNamespaces.contains(var1);
   }

   public boolean isXsdNamespace(String var1) {
      return "http://www.w3.org/2001/XMLSchema".equals(var1);
   }
}
