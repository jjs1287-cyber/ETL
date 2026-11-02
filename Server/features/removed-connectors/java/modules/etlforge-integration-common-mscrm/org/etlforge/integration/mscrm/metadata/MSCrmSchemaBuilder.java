package org.etlforge.integration.mscrm.metadata;

import com.google.common.collect.Sets;
import java.io.IOException;
import java.util.Set;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.etlforge.integration.etlforgeconnector.SchemaData;
import org.etlforge.integration.mscrm.FunctionType;
import org.etlforge.xml.XmlStringBuilder;

@ParametersAreNonnullByDefault
public final class MSCrmSchemaBuilder {
   private XmlStringBuilder xml;
   private final FunctionType method;
   private final MSCrmObject object;
   private final Set<String> upsertXsdTypeSet;

   public MSCrmSchemaBuilder(FunctionType var1, MSCrmObject var2) {
      this.method = var1;
      this.object = var2;
      this.upsertXsdTypeSet = Sets.newHashSet();
   }

   public SchemaData buildRequestSchema() {
      this.upsertXsdTypeSet.clear();
      this.xml = new XmlStringBuilder();
      this.createSchemaElement();
      String var1 = this.method.name().toLowerCase();
      this.insertRequestRootElement(var1);
      this.buildRequestType();
      this.buildUpsertFieldsComplexTypes();
      this.xml.closeAllElements();

      try {
         return SchemaData.fromOriginalContents(var1, this.xml.getXml());
      } catch (IOException var3) {
         throw new RuntimeException(var3);
      }
   }

   private void createSchemaElement() {
      this.addElement("xs:schema");
      this.addAttribute("xmlns:xs", "http://www.w3.org/2001/XMLSchema");
   }

   private void insertRequestRootElement(String var1) {
      this.startElementDefinition(var1, "requestType", 1, 1);
      this.closeElement();
   }

   private int getFieldMinOccurs(String var1) {
      return (FunctionType.UPDATE == this.method || FunctionType.DELETE == this.method) && var1.equals(this.object.getPrimaryIdAttribute()) ? 1 : 0;
   }

   private void buildFieldElement(String var1, String var2) {
      boolean var3 = FunctionType.UPSERT == this.method;
      if (var3) {
         this.upsertXsdTypeSet.add(var2);
      }

      String var4 = var3 ? this.getUpsertFieldComplexTypeName(var2) : "xs:" + var2;
      this.startElementDefinition(var1, var4, this.getFieldMinOccurs(var1), 1);
      this.closeElement();
   }

   private String getUpsertFieldComplexTypeName(String var1) {
      return this.method.getName() + var1;
   }

   private void buildUpsertFieldsComplexTypes() {
      if (FunctionType.UPSERT == this.method) {
         for (String var2 : this.upsertXsdTypeSet) {
            this.buildUpsertFieldComplexType(var2);
         }
      }
   }

   private void buildUpsertFieldComplexType(String var1) {
      this.startComplexTypeDefinition(this.getUpsertFieldComplexTypeName(var1));
      this.addElement("xs:simpleContent");
      this.addElement("xs:extension");
      this.addAttribute("base", "xs:" + var1);
      this.addElement("xs:attribute");
      this.addAttribute("name", "upsertkey");
      this.addAttribute("type", "xs:boolean");
      this.addAttribute("default", "false");
      this.closeElement();
      this.closeElement();
      this.closeElement();
      this.closeElement();
   }

   private void buildRequestType() {
      this.startComplexTypeDefinition("requestType");
      this.addElement("xs:sequence");
      this.startElementDefinition(this.object.getName(), null, 0, Integer.MAX_VALUE);
      this.startComplexTypeDefinition(null);
      this.addElement("xs:sequence");

      for (MSCrmAttribute var2 : this.method.getMatchingAttributes(this.object)) {
         this.buildFieldElement(var2.getName(), var2.getXsdType());
      }

      this.closeElement();
      this.closeElement();
      this.closeElement();
      this.closeElement();
      this.closeElement();
   }

   private void addElement(String var1) {
      this.xml.addElement(var1);
   }

   private void addAttribute(String var1, String var2) {
      this.xml.addAttribute(var1, var2);
   }

   private void closeElement() {
      this.xml.closeElement();
   }

   private void startComplexTypeDefinition(@Nullable String var1) {
      this.addElement("xs:complexType");
      if (var1 != null) {
         this.addAttribute("name", var1);
      }
   }

   private void startElementDefinition(String var1, @Nullable String var2, int var3, int var4) {
      this.addElement("xs:element");
      this.addAttribute("name", var1);
      if (var2 != null) {
         this.addAttribute("type", var2);
      }

      if (var3 != 1) {
         this.addAttribute("minOccurs", Integer.toString(var3));
      }

      if (var4 != 1) {
         this.addAttribute("maxOccurs", var4 == Integer.MAX_VALUE ? "unbounded" : Integer.toString(var4));
      }
   }

   public SchemaData buildResponseSchema() {
      this.xml = new XmlStringBuilder();
      this.createSchemaElement();
      String var1 = this.method.name().toLowerCase() + "Responses";
      this.insertResponseRootElement(var1);
      this.buildResponseType();
      this.buildResultType();
      this.xml.closeAllElements();

      try {
         return SchemaData.fromOriginalContents(var1, this.xml.getXml());
      } catch (IOException var3) {
         throw new RuntimeException(var3);
      }
   }

   private void insertResponseRootElement(String var1) {
      this.startElementDefinition(var1, "responsesType", 1, 1);
      this.closeElement();
   }

   private void buildResponseType() {
      this.startComplexTypeDefinition("responsesType");
      this.addElement("xs:sequence");
      this.startElementDefinition("success", "xs:boolean", 1, 1);
      this.closeElement();
      this.startElementDefinition("message", "xs:string", 0, 1);
      this.closeElement();
      this.startElementDefinition(this.method.name().toLowerCase() + "Response", "result", 0, Integer.MAX_VALUE);
      this.closeElement();
      this.closeElement();
      this.closeElement();
   }

   private void buildResultType() {
      this.startComplexTypeDefinition("result");
      this.addElement("xs:sequence");
      this.startElementDefinition("success", "xs:boolean", 1, 1);
      this.closeElement();
      this.startElementDefinition("message", "xs:string", 0, 1);
      this.closeElement();
      if (this.method == FunctionType.CREATE) {
         this.startElementDefinition("id", "xs:string", 1, 1);
         this.closeElement();
      }

      if (this.method == FunctionType.UPSERT) {
         this.startElementDefinition("id", "xs:string", 0, 1);
         this.closeElement();
         this.startElementDefinition("created", "xs:boolean", 0, 1);
         this.closeElement();
      }

      this.closeElement();
      this.closeElement();
   }
}
