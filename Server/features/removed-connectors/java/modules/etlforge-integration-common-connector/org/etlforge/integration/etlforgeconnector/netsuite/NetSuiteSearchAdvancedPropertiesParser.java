package org.etlforge.integration.etlforgeconnector.netsuite;

import com.google.common.base.Strings;
import org.etlforge.xml.sax.EndElementHandler;
import org.etlforge.xml.sax.KongaSaxParser;
import org.etlforge.xml.sax.RelativePathRoot;
import org.etlforge.xml.sax.StartElementHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class NetSuiteSearchAdvancedPropertiesParser {
   public static NetSuiteSearchAdvancedProperties fromXml(String var0) {
      try {
         return fromXmlImpl(var0);
      } catch (SAXException var2) {
         throw new RuntimeException(var2);
      }
   }

   private static NetSuiteSearchAdvancedProperties fromXmlImpl(String var0) throws SAXException {
      return Strings.isNullOrEmpty(var0) ? new NetSuiteSearchAdvancedProperties() : parse(var0);
   }

   private static NetSuiteSearchAdvancedProperties parse(String var0) throws SAXException {
      KongaSaxParser var1 = new KongaSaxParser();
      NetSuiteSearchAdvancedPropertiesParser.NetSuiteSearchAdvancedPropertiesHandler var2 = new NetSuiteSearchAdvancedPropertiesParser.NetSuiteSearchAdvancedPropertiesHandler(
         
      );
      NetSuiteSearchAdvancedPropertiesParser.NetSuiteSearchRowPropertyiesHandler var3 = new NetSuiteSearchAdvancedPropertiesParser.NetSuiteSearchRowPropertyiesHandler(
         
      );
      var1.registerHandler(var2);
      var1.registerHandler(var3);
      var1.parseString(var0);
      if (var3.properties != null) {
         var2.properties.setNetSuiteSearchRowPropertyies(var3.properties);
      }

      return var2.properties;
   }

   @RelativePathRoot("advancedproperty")
   private static class NetSuiteSearchAdvancedPropertiesHandler {
      private final NetSuiteSearchAdvancedProperties properties = new NetSuiteSearchAdvancedProperties();

      private NetSuiteSearchAdvancedPropertiesHandler() {
      }

      @EndElementHandler("searchtype")
      public void searchtype(String var1) {
         this.properties.setNetSuiteSearchType(NetSuiteSearchCategory.fromName(var1));
      }

      @EndElementHandler("customsearchobjects/name")
      public void objectName(String var1) {
         this.properties.addCustomSearchObject(var1);
      }
   }

   @RelativePathRoot("advancedproperty/searchrows")
   private static class NetSuiteSearchRowPropertyiesHandler {
      private NetSuiteSearchRowPropertyies properties = null;
      private String category = null;
      private String netsuiteid = null;
      private String netsuitetype = null;

      private NetSuiteSearchRowPropertyiesHandler() {
      }

      @EndElementHandler("wsdlversion")
      public void wsdlversion(String var1) {
         this.properties = new NetSuiteSearchRowPropertyies(var1, null);
      }

      @EndElementHandler("searchrow/name")
      public void objectName(String var1) {
         this.category = var1;
      }

      @EndElementHandler("searchrow")
      public void searchRow() {
         this.category = null;
      }

      @EndElementHandler("searchrow/field")
      public void field(String var1) {
         this.properties.putField(this.category, var1);
      }

      @StartElementHandler("searchrow/customfield")
      public void customfieldStart(Attributes var1) {
         this.netsuiteid = var1.getValue("netsuiteid");
         this.netsuitetype = var1.getValue("netsuitetype");
      }

      @EndElementHandler("searchrow/customfield")
      public void customfieldEnd(String var1) {
         String var2 = var1;
         this.properties.putCustomField(this.category, var2, this.netsuiteid, this.netsuitetype);
         this.netsuiteid = null;
         this.netsuitetype = null;
      }
   }
}
