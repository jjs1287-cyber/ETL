package org.etlforge.integration.client.connect.db2xml.wizard;

import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.ui.widget.TextFieldInput;
import org.etlforge.application.ui.widget.TextFieldInputValidator;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.data.structure.database.DatabaseStructureTree;
import org.etlforge.integration.data.structure.database.RelationshipCardinality;
import org.etlforge.integration.database.info.DatabaseColumn;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.integration.database.info.ManualSqlStatement;
import org.etlforge.xml.ElementNameSanitizer;
import org.etlforge.xml.XmlStringBuilder;

final class DbToXsdFactory {
   private static final Map<Integer, String> typeMap = Collections.unmodifiableMap(typeMap());
   private final String rootName;
   private String targetNamespace;
   private boolean includeDefaultValues;
   private XmlStringBuilder xsd;
   private final ElementNameSanitizer nameSanitizer;
   private boolean illegalNameEncountered;

   public DbToXsdFactory(String var1) {
      this.rootName = var1.trim();
      this.nameSanitizer = new ElementNameSanitizer('_');
   }

   public void setTargetNamespace(String var1) {
      this.targetNamespace = var1;
   }

   public void setIncludeDefaultValues(boolean var1) {
      this.includeDefaultValues = var1;
   }

   public DbToXsdFactory.XSD buildXsd(DatabaseStructure var1) {
      this.illegalNameEncountered = false;
      this.xsd = new XmlStringBuilder();
      this.addSchemaElement();
      this.openRootElement();
      if (!this.addTables(var1)) {
         return new DbToXsdFactory.XSD(null, null, false);
      }

      this.closeRootElement();
      this.xsd.closeElement();
      return new DbToXsdFactory.XSD(this.rootName, this.xsd.getXml(), this.illegalNameEncountered);
   }

   private void addSchemaElement() {
      this.xsd.addElement("schema");
      this.xsd.addAttribute("xmlns", "http://www.w3.org/2001/XMLSchema");
      if (StringUtils.isNotEmpty(this.targetNamespace)) {
         this.xsd.addAttribute("targetNamespace", this.targetNamespace);
      }
   }

   private void openRootElement() {
      this.xsd.addElement("element");
      this.xsd.addAttribute("name", this.rootName);
      this.xsd.addElement("complexType");
      this.xsd.addElement("sequence");
   }

   private void closeRootElement() {
      this.xsd.closeElement();
      this.xsd.closeElement();
      this.xsd.closeElement();
   }

   private boolean addTables(DatabaseStructure var1) {
      DatabaseStructureTree var2 = new DatabaseStructureTree(var1);
      DatabaseStructureTree.Node var3 = var2.getRoot();
      return this.addTableDefinition(var3);
   }

   private boolean addTableDefinition(DatabaseStructureTree.Node var1) {
      DatabaseObject var2 = var1.getTable();
      String var3 = this.getTableName(var2);
      if (var3 == null) {
         return false;
      }

      this.xsd.addElement("element");
      String var4 = this.sanitizeName(var3);
      this.xsd.addAttribute("name", var4);
      this.applyCardinality(var1.getCardinality());
      this.xsd.addElement("complexType");
      this.xsd.addElement("sequence");

      for (DatabaseColumn var6 : var2.getAllColumns()) {
         this.addColumnDefinition(var6);
      }

      for (DatabaseStructureTree.Node var8 : var1.getChildren()) {
         this.addTableDefinition(var8);
      }

      this.xsd.closeElement();
      this.xsd.closeElement();
      this.xsd.closeElement();
      return true;
   }

   private String getTableName(DatabaseObject var1) {
      if (var1 instanceof ManualSqlStatement) {
         String var2 = ((ManualSqlStatement)var1).guessTableName();
         if (StringUtils.isBlank(var2)) {
            var2 = this.askUserForTableName();
         }

         return var2;
      } else {
         return var1.getName();
      }
   }

   private String askUserForTableName() {
      TextFieldInput var2 = new TextFieldInput("Table Name", "What is the name of the table you are querying?");
      var2.setValidator(new TextFieldInputValidator() {
         @Override
         public boolean validate(String var1) {
            return !var1.isEmpty();
         }

         @Override
         public String getErrorMessage(String var1) {
            return var1.isEmpty() ? "Must provide a value." : null;
         }
      });
      var2.setAutoTrimEnabled(true);
      return var2.showInDialog(null);
   }

   private void applyCardinality(RelationshipCardinality var1) {
      String var2 = "1";
      String var3 = "1";
      switch (var1) {
         case ZERO_TO_N:
            var2 = "0";
         case ONE_TO_N:
            var3 = "unbounded";
            break;
         case ZERO_OR_ONE:
            var2 = "0";
      }

      this.xsd.addAttribute("minOccurs", var2);
      this.xsd.addAttribute("maxOccurs", var3);
   }

   private void addColumnDefinition(DatabaseColumn var1) {
      this.xsd.addElement("element");
      String var2 = this.sanitizeName(var1.getName());
      this.xsd.addAttribute("name", var2);
      String var3 = this.getXsdType(var1);
      this.xsd.addAttribute("type", var3);
      if (var1.isNullable()) {
         this.xsd.addAttribute("minOccurs", "0");
         this.xsd.addAttribute("nillable", "true");
      }

      if (this.includeDefaultValues) {
         String var4 = var1.getDefaultValueString();
         if (StringUtils.isNotEmpty(var4)) {
            this.xsd.addAttribute("default", StringEscapeUtils.escapeXml(var4));
         }
      }

      this.xsd.closeElement();
   }

   private String getXsdType(DatabaseColumn var1) {
      int var2 = var1.getSqlDataType();
      String var3 = typeMap.get(var2);
      if (var3 == null) {
         System.err.println("No XSD type for the SQL type " + var2);
         var3 = "string";
      }

      return var3;
   }

   private String sanitizeName(String var1) {
      String var2 = this.nameSanitizer.getSanitizedName(var1);
      if (!var2.equals(var1)) {
         this.illegalNameEncountered = true;
      }

      return var2;
   }

   public boolean hadIllegalNames() {
      return this.illegalNameEncountered;
   }

   private static Map<Integer, String> typeMap() {
      HashMap var0 = Maps.newHashMap();
      var0.put(-5, "long");
      var0.put(-2, "base64Binary");
      var0.put(-7, "short");
      var0.put(2004, "base64Binary");
      var0.put(16, "boolean");
      var0.put(1, "string");
      var0.put(2005, "string");
      var0.put(70, "anyURI");
      var0.put(91, "date");
      var0.put(3, "decimal");
      var0.put(8, "double");
      var0.put(6, "float");
      var0.put(4, "int");
      var0.put(-4, "base64Binary");
      var0.put(-1, "string");
      var0.put(-16, "string");
      var0.put(-9, "string");
      var0.put(2, "decimal");
      var0.put(7, "float");
      var0.put(5, "short");
      var0.put(92, "dateTime");
      var0.put(93, "dateTime");
      var0.put(-6, "short");
      var0.put(-3, "base64Binary");
      var0.put(12, "string");
      return var0;
   }

   public static final class XSD {
      public final String rootName;
      public final String contents;
      public boolean hadIllegalNames;

      public XSD(String var1, String var2, boolean var3) {
         this.rootName = var1;
         this.contents = var2;
         this.hadIllegalNames = var3;
      }
   }
}
