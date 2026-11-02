package org.etlforge.integration.salesforce.config;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.io.KongaIoUtils;
import org.etlforge.xml.Elements;
import org.etlforge.xml.KongaXmlUtils;
import org.etlforge.xml.dom.DomParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

final class SalesforceXmlConfig extends SalesforceConfig {
   private Document dom;
   private final Map<String, String> wsdlProperties;
   private final List<String> sObjectFields;
   private final Map<String, String> treeMapperPaths;
   private final EnumMap<SfdcMethod, SfdcMethodConfig> methods;
   private int chunkSize;

   public SalesforceXmlConfig(String var1) throws Exception {
      this(SalesforceXmlConfig.class.getClassLoader().getResourceAsStream(var1));
   }

   public SalesforceXmlConfig(InputStream var1) throws Exception {
      this.dom = this.loadXml(var1);
      this.wsdlProperties = Maps.newHashMap();
      this.sObjectFields = Lists.newArrayList();
      this.treeMapperPaths = Maps.newHashMap();
      this.methods = Maps.newEnumMap(SfdcMethod.class);
      this.chunkSize = 200;
      this.parseWsdlSection();
      this.parseChunkSize();
      this.parseTreeMapperSection();
      this.parseMethodConfigs();
      this.dom = null;
   }

   private Document loadXml(InputStream var1) throws Exception {
      try {
         return DomParser.parseInputStream(var1);
      } finally {
         KongaIoUtils.close(var1);
      }
   }

   private void parseWsdlSection() {
      for (Element var2 : Elements.matching("/SalesforceConfig/Wsdl/*", this.dom)) {
         String var3 = var2.getNodeName();
         if ("SObjectFields".equals(var3)) {
            this.parseSObjectFields(var2);
         } else {
            String var4 = KongaXmlUtils.getElementValue(var2);
            this.wsdlProperties.put(var3, var4 == null ? "" : var4);
         }
      }
   }

   private void parseSObjectFields(Element var1) {
      for (Element var3 : Elements.under(var1)) {
         String var4 = KongaXmlUtils.getElementValue(var3);
         this.sObjectFields.add(var4);
      }
   }

   private void parseChunkSize() {
      Element var1 = Elements.firstMatch("/SalesforceConfig/ChunkSize", this.dom);
      if (var1 != null) {
         this.chunkSize = Integer.parseInt(KongaXmlUtils.getElementValue(var1));
      }
   }

   private void parseTreeMapperSection() throws Exception {
      Element var1 = Elements.firstMatch("/SalesforceConfig/TreeMapper", this.dom);

      for (Element var3 : Elements.under(var1)) {
         String var4 = var3.getNodeName();
         String var5 = KongaXmlUtils.getElementValue(var3);
         this.treeMapperPaths.put(var4, var5);
      }
   }

   private void parseMethodConfigs() throws Exception {
      for (Element var2 : Elements.matching("/SalesforceConfig/Methods/Method", this.dom)) {
         String var3 = var2.getAttribute("enum");
         if (StringUtils.isBlank(var3)) {
            throw new Exception("<Method> element without enum attribute");
         }

         SfdcMethod var4 = SfdcMethod.valueOf(var3);
         SalesforceXmlConfig.MethodXmlConfig var5 = new SalesforceXmlConfig.MethodXmlConfig(var2);
         this.methods.put(var4, var5);
      }
   }

   @Override
   public String getBindingName() {
      return this.wsdlProperties.get("BindingName");
   }

   @Override
   public String getPortName() {
      return this.wsdlProperties.get("PortName");
   }

   @Override
   public String getSObjectLocalName() {
      return this.wsdlProperties.get("SObjectLocalName");
   }

   @Override
   public String getSObjectNamespace() {
      return this.wsdlProperties.get("SObjectNamespace");
   }

   @Override
   public String getSalesforceNamespace() {
      return this.wsdlProperties.get("Namespace");
   }

   @Override
   public String getServiceName() {
      return this.wsdlProperties.get("ServiceName");
   }

   @Override
   public String getSoapAction() {
      return this.wsdlProperties.get("SoapAction");
   }

   @Override
   public String getSoapBodyUse() {
      return this.wsdlProperties.get("SoapBodyUse");
   }

   @Override
   public String getSoapStyle() {
      return this.wsdlProperties.get("SoapStyle");
   }

   @Override
   public String[] getFieldsInheritedFromSObject() {
      return this.sObjectFields.toArray(new String[this.sObjectFields.size()]);
   }

   @Override
   public SfdcMethodConfig getMethodConfig(SfdcMethod var1) {
      return this.methods.get(var1);
   }

   @Override
   public int getChunkSize() {
      return this.chunkSize;
   }

   @Override
   public String getMappingTreePath(MappingTreePath var1) {
      return this.treeMapperPaths.get(var1.getKey());
   }

   private final class MethodXmlConfig implements SfdcMethodConfig {
      private String name;
      private String inputMessage;
      private String outputMessage;
      private String simpleRootPathInRequest;
      private String simpleRootPathInResponse;
      private boolean requestHasSimpleMappingTreeRep;
      private boolean responseHasSimpleMappingTreeRep;
      private final Map<String, String> properties = Maps.newHashMap();
      private final Map<String, ConstantHeaderMapping> constantHeaderMappings = Maps.newHashMap();

      public MethodXmlConfig(Element var2) throws Exception {
         for (Element var4 : Elements.under(var2)) {
            String var5 = var4.getNodeName();
            if (var5.equals("Name")) {
               this.name = KongaXmlUtils.getElementValue(var4);
            } else if (var5.equals("InputMessage")) {
               this.inputMessage = KongaXmlUtils.getElementValue(var4);
            } else if (var5.equals("OutputMessage")) {
               this.outputMessage = KongaXmlUtils.getElementValue(var4);
            } else if (var5.equals("SimpleRootPath")) {
               this.parseSimpleRootPaths(var4);
            } else if (var5.equals("SimpleMappingTreeRepresentation")) {
               this.parseSimpleMappingTreeRepresentations(var4);
            } else if (var5.equals("Paths")) {
               this.parsePaths(var4);
            } else if (var5.equals("ConstantHeaderMappings")) {
               this.parseConstantHeaderMappings(var4);
            }
         }

         Preconditions.checkArgument(StringUtils.isNotBlank(this.name), "Invalid SFDC configuration: Method without a name");
         Preconditions.checkArgument(StringUtils.isNotBlank(this.inputMessage), "Invalid SFDC configuration: Method without an input message name");
         Preconditions.checkArgument(StringUtils.isNotBlank(this.outputMessage), "Invalid SFDC configuration: Method without an output message name");
      }

      private void parseSimpleRootPaths(Element var1) {
         for (Element var3 : Elements.under(var1)) {
            String var4 = var3.getNodeName();
            if (var4.equals("Request")) {
               this.simpleRootPathInRequest = this.getSimpleRootPathPath(var3, "RequestBody");
            } else if (var4.equals("Response")) {
               this.simpleRootPathInResponse = this.getSimpleRootPathPath(var3, "ResponseBody");
            }
         }
      }

      private String getSimpleRootPathPath(Element var1, String var2) {
         String var3 = KongaXmlUtils.getElementValue(var1);
         String var4 = var1.getAttribute("RelativeTo");
         if (StringUtils.isEmpty(var4)) {
            var4 = var2;
         }

         String var5 = SalesforceXmlConfig.this.treeMapperPaths.get(var4);
         return var5 + var3;
      }

      private void parseSimpleMappingTreeRepresentations(Element var1) {
         for (Element var3 : Elements.under(var1)) {
            String var4 = var3.getNodeName();
            boolean var5 = KongaXmlUtils.isTrue(KongaXmlUtils.getElementValue(var3));
            if (var4.equals("Request")) {
               this.requestHasSimpleMappingTreeRep = var5;
            } else if (var4.equals("Response")) {
               this.responseHasSimpleMappingTreeRep = var5;
            }
         }
      }

      private void parsePaths(Element var1) {
         for (Element var3 : Elements.under(var1)) {
            String var4 = var3.getAttribute("name") + "Path";
            String var5 = var3.getAttribute("RelativeTo");
            String var6 = SalesforceXmlConfig.this.treeMapperPaths.get(var5);
            String var7 = var6 + KongaXmlUtils.getElementValue(var3);
            this.properties.put(var4, var7);
         }
      }

      private void parseConstantHeaderMappings(Element var1) {
         for (Element var3 : Elements.under(var1)) {
            this.parseConstantHeaderMapping(var3);
         }
      }

      private void parseConstantHeaderMapping(Element var1) {
         String var2 = null;
         String var3 = null;
         String var4 = null;

         for (Element var6 : Elements.under(var1)) {
            String var7 = var6.getNodeName();
            if (var7.equals("Path")) {
               var2 = KongaXmlUtils.getElementValue(var6);
            } else if (var7.equals("Type")) {
               var3 = KongaXmlUtils.getElementValue(var6).toLowerCase();
            } else if (var7.equals("Value")) {
               var4 = KongaXmlUtils.getElementValue(var6);
            }
         }

         if (!StringUtils.isEmpty(var2) && !StringUtils.isEmpty(var3) && var4 != null) {
            ConstantHeaderMapping var8 = this.createConstantHeaderMapping(var2, var3, var4);
            this.constantHeaderMappings.put(var8.getTargetPath(), var8);
         } else {
            throw new RuntimeException("Invalid ConstantHeaderMapping");
         }
      }

      private ConstantHeaderMapping createConstantHeaderMapping(String var1, String var2, String var3) {
         String var4 = SalesforceXmlConfig.this.treeMapperPaths.get("RequestHeader") + var1;
         Object var5 = null;
         if (var2.equals("string")) {
            var5 = var3;
         } else if (var2.equals("boolean")) {
            var5 = KongaXmlUtils.isTrue(var3);
         } else {
            if (!var2.equals("integer")) {
               throw new RuntimeException("Unsupported value type for ConstantHeaderMapping: " + var2);
            }

            var5 = Integer.parseInt(var3);
         }

         return new ConstantHeaderMapping(var4, var5);
      }

      @Override
      public String getMethodName() {
         return this.name;
      }

      @Override
      public String getInputMessageName() {
         return this.inputMessage;
      }

      @Override
      public String getOutputMessageName() {
         return this.outputMessage;
      }

      @Override
      public String getSimpleRootPathInRequest() {
         return this.simpleRootPathInRequest;
      }

      @Override
      public String getSimpleRootPathInResponse() {
         return this.simpleRootPathInResponse;
      }

      @Override
      public boolean requestHasSimpleMappingTreeRepresentation() {
         return this.requestHasSimpleMappingTreeRep;
      }

      @Override
      public boolean responseHasSimpleMappingTreeRepresentation() {
         return this.responseHasSimpleMappingTreeRep;
      }

      @Override
      public String getProperty(String var1) {
         return this.properties.get(var1);
      }

      @Override
      public Iterable<ConstantHeaderMapping> getConstantHeaderMappings() {
         return this.constantHeaderMappings.values();
      }
   }
}
