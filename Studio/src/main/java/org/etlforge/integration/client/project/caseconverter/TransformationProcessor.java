package org.etlforge.integration.client.project.caseconverter;

import java.io.File;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.script.Transform;
import org.etlforge.integration.data.structure.DataStructureType;
import org.etlforge.util.string.Case;
import org.etlforge.xml.Elements;
import org.etlforge.xml.KongaXmlUtils;
import org.etlforge.xml.XmlFileWriter;
import org.etlforge.xml.dom.DomParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public final class TransformationProcessor {
   private final DbSchemaTransformProvider transformProvider;
   private DbSchemaTransform transform;
   private DbSchemaTransform inputTransform;
   private DbSchemaTransform outputTransform;
   private File xmlFile;
   private Document dom;
   private boolean lastTransformationWasAffected;
   private boolean validTransform;

   public TransformationProcessor(DbSchemaTransformProvider var1) {
      this.transformProvider = var1;
   }

   public boolean wasLastTransformationAffected() {
      return this.lastTransformationWasAffected;
   }

   public void process(File var1) throws Exception {
      this.xmlFile = var1;
      this.reset();
      this.dom = DomParser.parseFile(var1);
      this.convertStructure("Input");
      if (this.validTransform) {
         this.convertStructure("Output");
      }

      if (this.lastTransformationWasAffected) {
         this.convertMappings();
         this.save();
      }
   }

   private void reset() {
      this.validTransform = true;
      this.lastTransformationWasAffected = false;
   }

   private void convertStructure(String var1) throws Exception {
      Element var2 = this.findDatabaseStructureElement(var1);
      if (var2 != null) {
         this.transform = this.lookupTransform(var2, var1);
         if (this.validateTransformPreRequisites(var2, this.transform)) {
            if ("Input".equals(var1)) {
               this.inputTransform = this.transform;
            } else {
               this.outputTransform = this.transform;
            }

            this.lastTransformationWasAffected = true;
            this.convertDatabaseStructure(var2);
            this.checkIncludeSchemaNameProperty(var2);
            this.convertBatchNodePath();
         } else {
            this.validTransform = false;
            this.lastTransformationWasAffected = false;
         }
      }
   }

   private Element findDatabaseStructureElement(String var1) {
      Elements var2 = Elements.matching("Entity/" + var1 + "Structure", this.dom);
      if (var2.getLength() == 1) {
         Element var3 = var2.item(0);
         String var4 = var3.getAttribute("type");
         if (DataStructureType.Database.name().equals(var4)) {
            return var3;
         }
      }

      return null;
   }

   private DbSchemaTransform lookupTransform(Element var1, String var2) throws Exception {
      SourceTarget var3 = "Input".equals(var2) ? SourceTarget.Source : SourceTarget.Target;
      return this.transformProvider.getTransform(var1, var3);
   }

   private boolean validateTransformPreRequisites(Element var1, DbSchemaTransform var2) {
      return new TransformValidator(var2).isValid(var1);
   }

   private void convertDatabaseStructure(Element var1) throws Exception {
      for (Element var3 : Elements.under(var1)) {
         String var4 = var3.getNodeName();
         if ("Objects".equals(var4)) {
            this.processDatabaseObjects(var3);
         } else if ("konga.string".equals(var4)) {
            if ("DbTables".equals(var3.getAttribute("name"))) {
               this.processDbTables(var3);
            }
         } else if ("DbFields".equals(var4)) {
            this.processDbFields(var3);
         } else if ("Relations".equals(var4)) {
            this.processRelations(var3);
         } else if ("SqlScript".equals(var4)) {
            this.processSqlScript(var3);
         } else if ("Filters".equals(var4)) {
            this.processFilters(var3);
         }
      }
   }

   private void processDatabaseObjects(Element var1) throws Exception {
      DbObjectsConverter var2 = new DbObjectsConverter(this.transform);
      var2.convert(var1);
   }

   private void processDbTables(Element var1) {
      DbTablesConverter var2 = new DbTablesConverter(this.transform);
      var2.convert(var1);
   }

   private void processDbFields(Element var1) {
      DbFieldsConverter var2 = new DbFieldsConverter(this.transform);
      var2.convert(var1);
   }

   private void processRelations(Element var1) {
      DbRelationsConverter var2 = new DbRelationsConverter(this.transform);
      var2.convertRelations(var1);
   }

   private void processSqlScript(Element var1) throws Exception {
      SqlScriptConverter var2 = new SqlScriptConverter(this.transform);
      var2.convertSql(var1);
   }

   private void processFilters(Element var1) throws Exception {
      DbFiltersConverter var2 = new DbFiltersConverter(this.transform);
      var2.convertFilters(var1);
   }

   private void convertMappings() throws Exception {
      String var1 = null;
      String var2 = null;
      Case var3 = null;
      if (this.inputTransform != null) {
         var1 = this.inputTransform.getNewSchemaName();
         var3 = this.inputTransform.getNewCase();
      }

      if (this.outputTransform != null) {
         var2 = this.outputTransform.getNewSchemaName();
         assert var3 == null || var3 == this.outputTransform.getNewCase();
         var3 = this.outputTransform.getNewCase();
      }

      if (var3 == null) {
         throw new RuntimeException("One of inputTransform or outputTransform must be set");
      }

      for (Element var5 : Elements.matching("Entity/Mappings/Mapping", this.dom)) {
         Element var6 = KongaXmlUtils.getFirstElementChildOfName(var5, "konga.string");
         if (var6 != null) {
            String var7 = KongaXmlUtils.getElementValue(var6);
            String var8 = Transform.switchCaseOwnerForDbDataElement(var7, var3, var3.convert(var1), var3.convert(var2));
            var6.setTextContent(var8);
         }
      }
   }

   private void convertBatchNodePath() throws Exception {
   }

   private void checkIncludeSchemaNameProperty(Element var1) throws Exception {
      XPath var2 = XPathFactory.newInstance().newXPath();
      NodeList var3 = (NodeList)var2.evaluate("Properties/Item[@key='includeSchemaName']", var1, XPathConstants.NODESET);
      if (var3.getLength() > 0) {
         Element var4 = (Element)var3.item(0);
         boolean var5 = StringUtils.isNotEmpty(this.transform.getNewSchemaName());
         var4.setAttribute("value", var5 ? "1" : "0");
      }
   }

   private void save() throws Exception {
      new XmlFileWriter().write(this.dom, this.xmlFile);
   }
}
