package org.etlforge.integration.server.engine.connector.mock;

import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.etlforge.integration.server.engine.connector.ConnectorExecutionSystem;
import org.etlforge.integration.server.engine.connector.exception.ConnectorServerRuntimeException;
import org.etlforge.integration.server.engine.connector.framework.ConnectorEngine;
import org.etlforge.integration.server.engine.connector.framework.ConnectorObjectList;
import org.etlforge.integration.server.engine.connector.framework.ConnectorXsdSchemaFiles;
import org.etlforge.integration.server.engine.connector.mock.xsd.MockConnectorRequest;
import org.etlforge.integration.server.engine.connector.mock.xsd.MockConnectorResponse;

public class ConnectorEngineMock implements ConnectorEngine {
   final ConnectorEndpointMock endpoint;
   static String myfolder = "Z:/etlforge/connectors/etlforgeconnectorsample/xsd/mockSchemas/";
   private static int instance = 0;
   private static int mockwork = 0;

   ConnectorEngineMock(ConnectorEndpointMock var1) {
      this.endpoint = var1;
   }

   private static String toShortValue(String var0) {
      return var0.length() < 3 ? var0 : var0.substring(0, 3);
   }

   private static String toValue(List<String> var0) {
      StringBuilder var1 = new StringBuilder();

      for (int var2 = 0; var2 < var0.size(); var2++) {
         var1.append(toShortValue((String)var0.get(var2))).append('_');
      }

      return var1.length() > 60 ? var1.substring(0, 60) : var1.toString();
   }

   private void put(String var1, String var2, Map<String, String> var3) {
      var3.put(var2 + var1, var2 + " for mocking connector");
   }

   @Override
   public ConnectorObjectList getConnectorObjectList(List<String> var1, String var2) {
      ConnectorObjectList var3 = new ConnectorObjectList();
      Map var4 = var3.getObjectList();
      String var5 = this.endpoint.getEndpointTypeName();
      String var6 = "i" + var1.size() + toValue(var1) + var5;
      this.put(var6, "Company", var4);
      this.put(var6, "Address", var4);
      this.put(var6, "Contact", var4);
      return var3;
   }

   @Override
   public ConnectorXsdSchemaFiles getConnectorSchemaFiles(boolean var1, List<String> var2) {
      return this.getConnectorSchemaFilesResource();
   }

   private ConnectorXsdSchemaFiles getConnectorSchemaFiles(boolean var1) {
      HashMap var2 = Maps.newHashMap();
      if (var1) {
         var2.put(
            "mockSchema.xsd",
            "<?xml version=\"1.0\" encoding=\"utf-8\"?><xsd:schema xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">  <xsd:element name=\"mockrequest\">    <xsd:complexType><xsd:sequence>        <xsd:element name=\"items\" type=\"items_type\"/>    </xsd:sequence></xsd:complexType>  </xsd:element>  <xsd:element name=\"mockresponse\">    <xsd:complexType><xsd:sequence>        <xsd:element name=\"endpoints\" type=\"items_type\" minOccurs=\"0\"/>        <xsd:element name=\"selectedobjects\" type=\"items_type\" minOccurs=\"0\"/>        <xsd:element name=\"items\" type=\"items_type\" minOccurs=\"0\"/>      </xsd:sequence>    </xsd:complexType>  </xsd:element>  <xsd:complexType name=\"items_type\">    <xsd:sequence>      <xsd:element name=\"item\" type=\"key_value_type\" minOccurs=\"0\" maxOccurs=\"unbounded\"/>    </xsd:sequence>  </xsd:complexType>  <xsd:complexType name=\"key_value_type\">    <xsd:sequence>      <xsd:element name=\"key\" type=\"xsd:string\" minOccurs=\"0\"/>      <xsd:element name=\"value\" type=\"xsd:string\" minOccurs=\"0\"/>    </xsd:sequence>  </xsd:complexType></xsd:schema>"
         );
      } else {
         var2.put(
            "mockSchema.xsd",
            "<?xml version=\"1.0\" encoding=\"utf-8\"?><xsd:schema xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><xsd:import schemaLocation=\"depends.xsd\"/>  <xsd:element name=\"mockrequest\">    <xsd:complexType><xsd:sequence>        <xsd:element name=\"items\" type=\"items_type\"/>    </xsd:sequence></xsd:complexType>  </xsd:element>  <xsd:element name=\"mockresponse\">    <xsd:complexType><xsd:sequence>        <xsd:element name=\"endpoints\" type=\"items_type\" minOccurs=\"0\"/>        <xsd:element name=\"selectedobjects\" type=\"items_type\" minOccurs=\"0\"/>        <xsd:element name=\"items\" type=\"items_type\" minOccurs=\"0\"/>      </xsd:sequence>    </xsd:complexType>  </xsd:element></xsd:schema>"
         );
         var2.put(
            "depends.xsd",
            "<?xml version=\"1.0\" encoding=\"utf-8\"?><xsd:schema xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">  <xsd:complexType name=\"items_type\">    <xsd:sequence>      <xsd:element name=\"item\" type=\"key_value_type\" minOccurs=\"0\" maxOccurs=\"unbounded\"/>    </xsd:sequence>  </xsd:complexType>  <xsd:complexType name=\"key_value_type\">    <xsd:sequence>      <xsd:element name=\"key\" type=\"xsd:string\" minOccurs=\"0\"/>      <xsd:element name=\"value\" type=\"xsd:string\" minOccurs=\"0\"/>    </xsd:sequence>  </xsd:complexType></xsd:schema>"
         );
      }

      return new ConnectorXsdSchemaFiles("mockSchema.xsd", "mockrequest", "mockSchema.xsd", "mockresponse", var2);
   }

   private ConnectorXsdSchemaFiles getConnectorSchemaFilesResource() {
      HashMap var1 = Maps.newHashMap();
      this.putMockFile("mockrequest.xsd", var1);
      this.putMockFile("mockresponse.xsd", var1);
      this.putMockFile("mockdep.xsd", var1);
      return new ConnectorXsdSchemaFiles("mockrequest.xsd", "mockrequest", "mockresponse.xsd", "mockresponse", var1);
   }

   private void putMockFile(String var1, Map<String, String> var2) {
      String var3 = ConnectorExecutionSystem.readFileUTF8(myfolder + var1);
      var2.put(var1, var3);
   }

   private static synchronized void throwExceptionOnExecution() {
      instance++;
      if (instance < 5) {
         System.out.println("===Fail mock at instance=" + instance);
         throw new ConnectorServerRuntimeException("Mock Execution Fail");
      }

      System.out.println("___Execute mock at instance=" + instance);
   }

   private static void doWork() {
      try {
         mockwork++;
         int var0 = 3000 - mockwork * 200;
         if (var0 > 0) {
            Thread.sleep(var0);
         }
      } catch (InterruptedException var1) {
         var1.printStackTrace();
      }
   }

   @Override
   public void executeConnectorFileUtf8(List<String> var1, String var2, String var3) {
      doWork();
      throwExceptionOnExecution();
      String var4 = ConnectorExecutionSystem.readFileUTF8(var2);
      String var5 = this.executeConnectorMock(var1, var4);
      ConnectorExecutionSystem.writeFileUTF8(var3, var5);
   }

   public String executeConnectorMock(List<String> var1, String var2) {
      Map var3 = MockConnectorRequest.parseRequestXmlString(var2);
      return MockConnectorResponse.buildResponse(this.endpoint, var1, var3);
   }
}
