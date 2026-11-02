package com.etlforge.integration.salesforce.wsdl;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.xml.namespace.NamespaceContext;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.salesforce.SObject;
import org.etlforge.integration.salesforce.SimpleTypes;
import org.etlforge.integration.salesforce.config.SalesforceConfig;
import org.etlforge.io.KongaIoUtils;
import org.etlforge.xml.Elements;
import org.etlforge.xml.ExpandedName;
import org.etlforge.xml.SimpleNamespaceContext;
import org.etlforge.xml.XmlNameFactory;
import org.etlforge.xml.dom.DomParser;
import org.etlforge.xml.xsd.DerivedTypesCollector;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

public final class SalesforceWsdlParser {
   private final Document dom;
   private final SalesforceConfig config;
   private static final String wsdlPrefix = "wsdl";
   private static final String xsdPrefix = "xs";
   private static final String soapPrefix = "soap";
   private final SimpleNamespaceContext nsContext;

   private SalesforceWsdlParser(Document var1, SalesforceConfig var2) {
      this.dom = var1;
      this.config = var2;
      this.nsContext = new SimpleNamespaceContext();
      this.nsContext.add("wsdl", "http://schemas.xmlsoap.org/wsdl/");
      this.nsContext.add("xs", "http://www.w3.org/2001/XMLSchema");
      this.nsContext.add("soap", "http://schemas.xmlsoap.org/wsdl/soap/");
   }

   public SalesforceWsdlParser(File var1, SalesforceConfig var2) throws SalesforceWsdlException {
      this(parseFile(var1), var2);
   }

   private static Document parseFile(File var0) {
      try {
         return DomParser.parseFile(var0, true);
      } catch (Exception var2) {
         throw new SalesforceWsdlException(var2);
      }
   }

   public SalesforceWsdlParser(URL var1, SalesforceConfig var2) throws SalesforceWsdlException {
      this(openUrlStream(var1), var2);
   }

   private static InputStream openUrlStream(URL var0) {
      try {
         return var0.openStream();
      } catch (IOException var2) {
         throw new SalesforceWsdlException(var2);
      }
   }

   public SalesforceWsdlParser(InputStream var1, SalesforceConfig var2) throws SalesforceWsdlException {
      this(parseStream(var1), var2);
   }

   private static Document parseStream(InputStream var0) {
      try {
         return DomParser.parseInputStream(var0, true);
      } catch (Exception var5) {
         throw new SalesforceWsdlException(var5);
      } finally {
         KongaIoUtils.close(var0);
      }
   }

   public SalesforceWsdlInfo getParsedInfo() {
      return !this.isSalesforce()
         ? SalesforceWsdlInfo.NOT_SALESFORCE
         : new SalesforceWsdlInfo(true, this.getWebServiceUrl(), this.listObjectDefinitions(), this.getNamespaceMappings(), this.getRequestHeaders());
   }

   public boolean isSalesforce() {
      if (WsdlUtils.isDefinitionsNode(this.dom.getDocumentElement())) {
         String var1 = this.dom.getDocumentElement().getAttribute("targetNamespace");
         return this.config.getSalesforceNamespace().equals(var1);
      } else {
         return false;
      }
   }

   private ObjectList listObjectDefinitions() throws SalesforceWsdlException {
      SalesforceWsdlParser.ObjectDetector var1 = new SalesforceWsdlParser.ObjectDetector(this.dom, this.nsContext, this.config);
      return var1.collect();
   }

   public SortedSet<ExpandedName> listObjectNames() throws SalesforceWsdlException {
      SalesforceWsdlParser.ObjectDetector var1 = new SalesforceWsdlParser.ObjectDetector(this.dom, this.nsContext, this.config);
      TreeSet var2 = Sets.newTreeSet(new Comparator<ExpandedName>() {
         public int compare(ExpandedName var1, ExpandedName var2x) {
            return var1.getLocalName().compareToIgnoreCase(var2x.getLocalName());
         }
      });

      for (SObject var4 : var1.collect()) {
         var2.add(var4.getName());
      }

      return var2;
   }

   public String getWebServiceUrl() throws SalesforceWsdlException {
      SalesforceWsdlParser.UrlFinder var1 = new SalesforceWsdlParser.UrlFinder(this.dom, this.nsContext);
      return var1.findUrl();
   }

   public Set<String> getNamespaceMappings() {
      HashSet var1 = Sets.newHashSet();
      Element var2 = this.dom.getDocumentElement();
      NamedNodeMap var3 = var2.getAttributes();
      int var4 = 0;

      for (int var5 = var3.getLength(); var4 < var5; var4++) {
         Attr var6 = (Attr)var3.item(var4);
         String var7 = var6.getName();
         if (var7.equals("xmlns") || var7.startsWith("xmlns:")) {
            var1.add(var7 + "=" + var6.getValue());
         }
      }

      return var1;
   }

   public Multimap<String, String> getRequestHeaders() {
      SalesforceWsdlParser.RequestHeadersCollector var1 = new SalesforceWsdlParser.RequestHeadersCollector(this.dom, this.nsContext);
      return var1.collect();
   }

   private static final class FieldExtractor {
      private final Document dom;
      private final NamespaceContext nsContext;
      private final SalesforceConfig config;
      private ExpandedName object;
      private Set<SObject.Field> fields;

      public FieldExtractor(Document var1, NamespaceContext var2, SalesforceConfig var3) {
         this.dom = var1;
         this.nsContext = var2;
         this.config = var3;
      }

      public Set<SObject.Field> listFields(ExpandedName var1) {
         this.object = var1;
         this.fields = Sets.newHashSet();
         Element var2 = this.locateSchema();
         if (var2 != null) {
            this.processObjectTypeDefinition(var2);
         }

         return this.fields;
      }

      private Element locateSchema() {
         String var1 = MessageFormat.format("/{0}:definitions/{0}:types/{1}:schema[@targetNamespace=\"{2}\"]", "wsdl", "xs", this.object.getNamespace());
         Elements var2 = Elements.matching(var1, this.dom, this.nsContext);
         return var2.getLength() == 1 ? var2.item(0) : null;
      }

      private void processObjectTypeDefinition(Element var1) {
         Element var2 = this.locateTypeDef(var1);
         if (var2 != null) {
            this.addFieldsFromBaseType();

            for (Element var4 : this.listFields(var2)) {
               this.checkField(var4);
            }
         }
      }

      private void addFieldsFromBaseType() {
         for (String var4 : this.config.getFieldsInheritedFromSObject()) {
            this.fields.add(new SObject.Field(var4, SimpleTypes.STRING));
         }
      }

      private Element locateTypeDef(Element var1) {
         String var2 = MessageFormat.format("{0}:complexType[@name=\"{1}\"]", "xs", this.object.getLocalName());
         Elements var3 = Elements.matching(var2, var1, this.nsContext);
         return var3.getLength() == 1 ? var3.item(0) : null;
      }

      private Elements listFields(Element var1) {
         String var2 = MessageFormat.format("{0}:complexContent/{0}:extension/{0}:sequence/{0}:element", "xs");
         return Elements.matching(var2, var1, this.nsContext);
      }

      private void checkField(Element var1) {
         String var2 = var1.getAttribute("type");
         if (!StringUtils.isEmpty(var2)) {
            String var3 = "";
            int var5 = var2.indexOf(":");
            String var4;
            if (var5 >= 0) {
               var3 = var2.substring(0, var5);
               var4 = var2.substring(var5 + 1);
            } else {
               var4 = var2;
            }

            String var6 = var1.lookupNamespaceURI(var3);
            ExpandedName var7 = StringUtils.isEmpty(var6) ? SimpleTypes.STRING : XmlNameFactory.nonConforming().newExpandedName(var6, var4);
            this.fields.add(new SObject.Field(var1.getAttribute("name"), var7));
         }
      }
   }

   private static final class ObjectDetector {
      private final Document dom;
      private final SalesforceConfig config;
      private final NamespaceContext nsContext;

      public ObjectDetector(Document var1, NamespaceContext var2, SalesforceConfig var3) {
         this.dom = var1;
         this.nsContext = var2;
         this.config = var3;
      }

      public ObjectList collect() {
         String var1 = this.getSchemaQuery();
         Elements var2 = Elements.matching(var1, this.dom, this.nsContext);
         DerivedTypesCollector var3 = new DerivedTypesCollector(var2);
         var3.setTargetNamespace(this.config.getSalesforceNamespace());
         ExpandedName var4 = XmlNameFactory.conforming().newExpandedName(this.config.getSObjectNamespace(), this.config.getSObjectLocalName());
         Set var5 = var3.collectDerivedTypes(var4);
         HashSet var6 = Sets.newHashSet();
         SalesforceWsdlParser.FieldExtractor var7 = new SalesforceWsdlParser.FieldExtractor(this.dom, this.nsContext, this.config);

         for (Object __etlItem249 : var5) {
            DerivedTypesCollector.TypeDef var9 = (DerivedTypesCollector.TypeDef) __etlItem249;
            Set var10 = var7.listFields(var9.name);
            var6.add(new SObject(var9.name, var10));
         }

         return new ObjectList(var6);
      }

      private String getSchemaQuery() {
         return MessageFormat.format("/{0}:definitions/{0}:types/{1}:schema", "wsdl", "xs");
      }
   }

   private static final class RequestHeadersCollector {
      private static final String OP_QUERY = getOperationQuery();
      private static final String HEADER_QUERY = getSoapHeaderQuery();
      private final Document dom;
      private final NamespaceContext nsContext;
      private Multimap<String, String> requestHeaders;

      public RequestHeadersCollector(Document var1, NamespaceContext var2) {
         this.dom = var1;
         this.nsContext = var2;
      }

      public Multimap<String, String> collect() {
         this.requestHeaders = HashMultimap.create();

         for (Element var2 : Elements.matching(OP_QUERY, this.dom, this.nsContext)) {
            this.parseOperationElementForRequestHeaders(var2);
         }

         return this.requestHeaders;
      }

      private void parseOperationElementForRequestHeaders(Element var1) {
         String var2 = var1.getAttribute("name");

         for (Element var4 : Elements.matching(HEADER_QUERY, var1, this.nsContext)) {
            String var5 = var4.getAttribute("part");
            this.requestHeaders.put(var2, var5);
         }
      }

      private static String getOperationQuery() {
         return MessageFormat.format("/{0}:definitions//{0}:binding/{0}:operation", "wsdl");
      }

      private static String getSoapHeaderQuery() {
         return MessageFormat.format("child::{0}:input/{1}:header", "wsdl", "soap");
      }
   }

   private static final class UrlFinder {
      private final Document dom;
      private final NamespaceContext nsContext;

      public UrlFinder(Document var1, NamespaceContext var2) {
         this.dom = var1;
         this.nsContext = var2;
      }

      public String findUrl() {
         String var1 = getAddressQuery();

         for (Element var3 : Elements.matching(var1, this.dom, this.nsContext)) {
            String var4 = var3.getAttribute("location");
            if (StringUtils.isNotEmpty(var4)) {
               return var4;
            }
         }

         return null;
      }

      private static String getAddressQuery() {
         return MessageFormat.format("/{0}:definitions/{0}:service/{0}:port/{1}:address", "wsdl", "soap");
      }
   }
}
