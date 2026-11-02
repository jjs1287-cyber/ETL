package org.etlforge.integration.salesforce.wsdl.partner.crom;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Map;
import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.integration.structure.crom.CROMHeader;
import org.etlforge.integration.structure.crom.CROM_Tree;
import org.etlforge.integration.structure.crom.CROM_Xml;
import org.etlforge.integration.structure.mapping.CromBasedMappingTreeStructure;
import org.etlforge.integration.structure.mapping.CromToMappingStructureWriter;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;
import org.etlforge.integration.structure.mapping.MappingTreeStructureException;

public abstract class AbstractCallStructureBuilder {
   private final CromHeaderElement[] headers;
   private CROM root;
   private CROM parent;
   private CROM transaction;
   private Map<String, CROM> typeMap;
   private final AbstractCallStructureBuilder.Mode mode;

   protected AbstractCallStructureBuilder(AbstractCallStructureBuilder.Mode var1, CromHeaderElement... var2) {
      this.mode = var1;
      this.headers = var2;
   }

   public final void setParent(CROM var1) {
      this.parent = var1;
   }

   public final void setTypeMap(Map<String, CROM> var1) {
      this.typeMap = var1;
   }

   final Map<String, CROM> getTypeMap() {
      return this.typeMap;
   }

   protected static final CROM createElementInPartnerNamespace(CROM var0, String var1) {
      return CromUtils.createElementInNamespace(var0, var1, "urn:partner.soap.sforce.com");
   }

   protected static final CROM createElementInSObjectNamespace(CROM var0, String var1) {
      return CromUtils.createElementInNamespace(var0, var1, "urn:sobject.partner.soap.sforce.com");
   }

   protected static final CROM createElementInETLForgeNamespace(CROM var0, String var1) {
      return CromUtils.createElementInNamespace(var0, var1, "http://www.etlforge.invalid/XsdFromWsdl");
   }

   protected static final CROM createValueNode(CROM var0, String var1, String var2) {
      CROM_Xml var3 = (CROM_Xml)var0.getExtension();
      var3.setSchemaType(var1);
      var3.setSchemaTypeNamespace("http://www.w3.org/2001/XMLSchema");
      return var0.createValueCrom(var2);
   }

   protected static final CROM createStringValueNode(CROM var0) {
      return createValueNode(var0, "string", "String");
   }

   protected static final CROM createBooleanValueNode(CROM var0) {
      return createValueNode(var0, "boolean", "Boolean");
   }

   protected static final CROM createIntValueNode(CROM var0) {
      return createValueNode(var0, "integer", "Integer");
   }

   protected static final void buildResultStructure(CROM var0, SfdcMethod var1) {
      CROM var2 = createElementInPartnerNamespace(var0, "result");
      CromUtils.makeOptionalLoop(var2);
      if (var1 == SfdcMethod.UPSERT) {
         CROM var3 = createElementInPartnerNamespace(var2, "created");
         createBooleanValueNode(var3);
      }

      buildErrorsElement(var2, var1);
      CROM var5 = createElementInPartnerNamespace(var2, "id");
      CromUtils.makeOptional(var5);
      createStringValueNode(var5);
      CROM var4 = createElementInPartnerNamespace(var2, "success");
      createStringValueNode(var4);
   }

   private static void buildErrorsElement(CROM var0, SfdcMethod var1) {
      CROM var2 = createElementInPartnerNamespace(var0, "errors");
      CromUtils.makeOptionalLoop(var2);
      if (var1 == SfdcMethod.DELETE) {
         CromUtils.makeNillable(var2);
      }

      CROM var3 = createElementInPartnerNamespace(var2, "fields");
      CromUtils.makeOptionalLoop(var3);
      CromUtils.makeNillable(var3);
      CROM var4 = createStringValueNode(var3);
      CromUtils.makeOptional(var4);
      CROM var5 = createElementInPartnerNamespace(var2, "message");
      createStringValueNode(var5);
      CROM var6 = createElementInPartnerNamespace(var2, "statusCode");
      createStringValueNode(var6);
   }

   public final MappingTreeStructure getMappingTreeStructure() throws MappingTreeStructureException {
      CROM var1 = this.getRoot();
      CromBasedMappingTreeStructure var2 = new CromBasedMappingTreeStructure(var1, CROMHeader.XML);
      var2.setTypeMap(this.typeMap);
      return var2;
   }

   public final void writeJtr(Writer var1) throws MappingTreeStructureException, IOException {
      CROM var2 = this.getRoot();
      CromToMappingStructureWriter var3 = new CromToMappingStructureWriter(var2, CROMHeader.XML);
      var3.setTypeMap(this.typeMap);
      var3.writeXml(var1);
   }

   public final CROM getRoot() throws MappingTreeStructureException {
      if (this.root == null) {
         this.createTree();
      }

      return this.root;
   }

   private void createTree() throws MappingTreeStructureException {
      CROM var1;
      if (this.parent == null) {
         this.root = SfdcCromUtils.createElement("root", null, false, false);
         this.root.addType(32768L);
         this.transaction = SfdcCromUtils.createElement("transaction", this.root, true, true);
         if (this.mode == AbstractCallStructureBuilder.Mode.REQUEST) {
            var1 = this.transaction;
         } else {
            CROM var2 = SfdcCromUtils.createElement("response", this.transaction, false, false);
            var1 = var2;
         }
      } else {
         this.root = this.parent;
         this.transaction = null;
         var1 = this.root;
      }

      if (this.headers.length > 0) {
         this.populateHeader(var1, this.headers);
      }

      CROM var3 = SfdcCromUtils.createElement("body", var1, false, false);
      this.populateBody(var3);
   }

   private void populateHeader(CROM var1, CromHeaderElement... var2) {
      CROM var3 = SfdcCromUtils.createSoapHeaderRoot(var1);

      for (CromHeaderElement var7 : var2) {
         var7.insertUnder(var3);
      }
   }

   protected abstract void populateBody(CROM var1) throws MappingTreeStructureException;

   public final void writeJtrToConsole() {
      PrintWriter var1 = new PrintWriter(System.out, true);

      try {
         this.writeJtr(var1);
      } catch (MappingTreeStructureException var7) {
         var7.printStackTrace();
      } catch (IOException var8) {
         var8.printStackTrace();
      } finally {
         var1.close();
      }
   }

   public final CROM_Tree getCromTree() throws MappingTreeStructureException {
      CROM var1 = this.getRoot();
      CROM_Tree var2 = new CROM_Tree(var1);
      if (this.typeMap != null) {
         var2.addElementTypes(this.typeMap);
      }

      return var2;
   }

   protected final CROM getTransactionNode() {
      return this.transaction;
   }

   protected enum Mode {
      REQUEST,
      RESPONSE;
   }
}
