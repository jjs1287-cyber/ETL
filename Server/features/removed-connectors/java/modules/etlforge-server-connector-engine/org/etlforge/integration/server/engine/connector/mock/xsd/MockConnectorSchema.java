package org.etlforge.integration.server.engine.connector.mock.xsd;

public class MockConnectorSchema {
   public static final String dependSchemaFileName = "depends.xsd";
   public static final String requestRoot = "mockrequest";
   public static final String responseRoot = "mockresponse";
   static final String SchemaStartTag = "<?xml version=\"1.0\" encoding=\"utf-8\"?><xsd:schema xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">";
   static final String SchemaEndTag = "</xsd:schema>";
   static final String importDepends = "<xsd:import schemaLocation=\"depends.xsd\"/>";
   static final String schemaElements = "  <xsd:element name=\"mockrequest\">    <xsd:complexType><xsd:sequence>        <xsd:element name=\"items\" type=\"items_type\"/>    </xsd:sequence></xsd:complexType>  </xsd:element>  <xsd:element name=\"mockresponse\">    <xsd:complexType><xsd:sequence>        <xsd:element name=\"endpoints\" type=\"items_type\" minOccurs=\"0\"/>        <xsd:element name=\"selectedobjects\" type=\"items_type\" minOccurs=\"0\"/>        <xsd:element name=\"items\" type=\"items_type\" minOccurs=\"0\"/>      </xsd:sequence>    </xsd:complexType>  </xsd:element>";
   static final String schemaType = "  <xsd:complexType name=\"items_type\">    <xsd:sequence>      <xsd:element name=\"item\" type=\"key_value_type\" minOccurs=\"0\" maxOccurs=\"unbounded\"/>    </xsd:sequence>  </xsd:complexType>  <xsd:complexType name=\"key_value_type\">    <xsd:sequence>      <xsd:element name=\"key\" type=\"xsd:string\" minOccurs=\"0\"/>      <xsd:element name=\"value\" type=\"xsd:string\" minOccurs=\"0\"/>    </xsd:sequence>  </xsd:complexType>";
   public static final String schemaSingle = "<?xml version=\"1.0\" encoding=\"utf-8\"?><xsd:schema xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">  <xsd:element name=\"mockrequest\">    <xsd:complexType><xsd:sequence>        <xsd:element name=\"items\" type=\"items_type\"/>    </xsd:sequence></xsd:complexType>  </xsd:element>  <xsd:element name=\"mockresponse\">    <xsd:complexType><xsd:sequence>        <xsd:element name=\"endpoints\" type=\"items_type\" minOccurs=\"0\"/>        <xsd:element name=\"selectedobjects\" type=\"items_type\" minOccurs=\"0\"/>        <xsd:element name=\"items\" type=\"items_type\" minOccurs=\"0\"/>      </xsd:sequence>    </xsd:complexType>  </xsd:element>  <xsd:complexType name=\"items_type\">    <xsd:sequence>      <xsd:element name=\"item\" type=\"key_value_type\" minOccurs=\"0\" maxOccurs=\"unbounded\"/>    </xsd:sequence>  </xsd:complexType>  <xsd:complexType name=\"key_value_type\">    <xsd:sequence>      <xsd:element name=\"key\" type=\"xsd:string\" minOccurs=\"0\"/>      <xsd:element name=\"value\" type=\"xsd:string\" minOccurs=\"0\"/>    </xsd:sequence>  </xsd:complexType></xsd:schema>";
   public static final String schemaMultiple = "<?xml version=\"1.0\" encoding=\"utf-8\"?><xsd:schema xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><xsd:import schemaLocation=\"depends.xsd\"/>  <xsd:element name=\"mockrequest\">    <xsd:complexType><xsd:sequence>        <xsd:element name=\"items\" type=\"items_type\"/>    </xsd:sequence></xsd:complexType>  </xsd:element>  <xsd:element name=\"mockresponse\">    <xsd:complexType><xsd:sequence>        <xsd:element name=\"endpoints\" type=\"items_type\" minOccurs=\"0\"/>        <xsd:element name=\"selectedobjects\" type=\"items_type\" minOccurs=\"0\"/>        <xsd:element name=\"items\" type=\"items_type\" minOccurs=\"0\"/>      </xsd:sequence>    </xsd:complexType>  </xsd:element></xsd:schema>";
   public static final String dependschema = "<?xml version=\"1.0\" encoding=\"utf-8\"?><xsd:schema xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">  <xsd:complexType name=\"items_type\">    <xsd:sequence>      <xsd:element name=\"item\" type=\"key_value_type\" minOccurs=\"0\" maxOccurs=\"unbounded\"/>    </xsd:sequence>  </xsd:complexType>  <xsd:complexType name=\"key_value_type\">    <xsd:sequence>      <xsd:element name=\"key\" type=\"xsd:string\" minOccurs=\"0\"/>      <xsd:element name=\"value\" type=\"xsd:string\" minOccurs=\"0\"/>    </xsd:sequence>  </xsd:complexType></xsd:schema>";

   private static String getExtraLongXsdFileName() {
      String var0 = "";

      for (int var1 = 0; var1 < 6; var1++) {
         var0 = var0 + "schemaLongFileName" + var1;
      }

      return var0 + ".xsd";
   }
}
