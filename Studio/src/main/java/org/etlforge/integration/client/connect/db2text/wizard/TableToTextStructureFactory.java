package org.etlforge.integration.client.connect.db2text.wizard;

import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.client.connect.text.TextStructureFactory;
import org.etlforge.integration.client.connect.wizard.Folders;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.EntityUtils;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.ProjectUtils;
import org.etlforge.integration.data.structure.ComplexTextStructure;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.data.structure.SimpleTextStructure;
import org.etlforge.integration.data.structure.TextStructure;
import org.etlforge.integration.data.structure.database.DatabaseStructureTree;
import org.etlforge.integration.data.structure.database.RelationshipCardinality;
import org.etlforge.integration.data.structure.text.Field;
import org.etlforge.integration.data.structure.text.FieldDataType;
import org.etlforge.integration.data.structure.text.FieldDelimiter;
import org.etlforge.integration.data.structure.text.QualifierMode;
import org.etlforge.integration.data.structure.text.Segment;
import org.etlforge.integration.data.structure.text.SegmentOccurrence;
import org.etlforge.integration.data.structure.text.TextStructureNameUtils;
import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.integration.database.SchemaAndTableNameSeparator;
import org.etlforge.integration.database.info.DatabaseColumn;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.integration.database.info.ManualSqlStatement;

final class TableToTextStructureFactory implements TextStructureFactory<DatabaseStructure> {
   private final Folders folders;
   private BeginEndQuote quotes;

   public TableToTextStructureFactory(Folders var1) {
      this.folders = var1;
   }

   public boolean isAvailableFor(DatabaseStructure var1) {
      return true;
   }

   public TextDocument createStructure(DatabaseStructure var1) {
      this.quotes = var1.getBeginEndQuote();
      Folder var2 = this.folders.getWizardFolder(EntityType.Document, true);
      DatabaseStructureTree var3 = new DatabaseStructureTree(var1);
      DatabaseStructureTree.Node var4 = var3.getRoot();
      DatabaseObject var5 = var4.getTable();
      TextDocument var6 = this.createEmptyDocument(var2, var5);
      TextStructure var7 = this.createTextStructure(var4);
      var6.setStructure(var7);
      this.insertIntoProject(var2, var6);
      return var6;
   }

   private TextStructure createTextStructure(DatabaseStructureTree.Node var1) {
      return var1.hasChildren() ? this.createComplexText(var1) : this.createFlatText(var1.getTable());
   }

   private TextDocument createEmptyDocument(Folder var1, DatabaseObject var2) {
      String var3 = this.getName(var1, var2);
      TextDocument var4 = new TextDocument(var3);
      var4.setDelimiter(FieldDelimiter.COMMA);
      var4.setStringQualifier("\"");
      var4.setQualifierMode(QualifierMode.WHEN_NEEDED);
      var4.setTransient(true);
      return var4;
   }

   private String getName(Folder var1, DatabaseObject var2) {
      String var3;
      if (var2 instanceof ManualSqlStatement) {
         var3 = ((ManualSqlStatement)var2).guessTableName();
         if (StringUtils.isBlank(var3)) {
            var3 = "manual_sql";
         }
      } else {
         var3 = var2.getName();
      }

      return EntityUtils.getAvailableChildName(var1, var3);
   }

   private ComplexTextStructure createComplexText(DatabaseStructureTree.Node var1) {
      ComplexTextStructure var2 = new ComplexTextStructure("", false);
      Segment var3 = Segment.newRootSegment();
      var2.addSegment(var3);
      this.addSegmentForTable(var1, var3.getName(), var2, new HashSet<>());
      return var2;
   }

   private void addSegmentForTable(DatabaseStructureTree.Node var1, String var2, ComplexTextStructure var3, Set<String> var4) {
      DatabaseObject var5 = var1.getTable();
      SchemaAndTableNameSeparator var6 = new SchemaAndTableNameSeparator(var5.getName(), this.quotes);
      String var7 = var6.getTable();
      String var8 = getSegmentId(var7, var4);
      Segment var9 = this.createSegment(var5, var8);
      String var10 = TextStructureNameUtils.replaceIllegalCharacters(var7);
      var9.setName(var10);
      var9.setParentName(var2);
      SegmentOccurrence var11 = getSegmentOccurrence(var1.getCardinality());
      var9.setOccurrence(var11);
      var3.addSegment(var9);

      for (DatabaseStructureTree.Node var13 : var1.getChildren()) {
         this.addSegmentForTable(var13, var10, var3, var4);
      }
   }

   private static String getSegmentId(String var0, Set<String> var1) {
      String var2 = var0;
      int var3 = 0;
      String var4 = var2;

      while (var1.contains(var4)) {
         var4 = var2 + ++var3;
      }

      var1.add(var4);
      return var4;
   }

   private Segment createSegment(DatabaseObject var1, String var2) {
      Segment var3 = new Segment("");
      if (var2 != null) {
         Field var4 = Field.newDelimitedIdentifier(var2);
         var3.fieldList.add(var4);
      }

      for (DatabaseColumn var5 : var1.getAllColumns()) {
         Field var6 = this.columnToField(var5);
         var3.fieldList.add(var6);
      }

      return var3;
   }

   private Field columnToField(DatabaseColumn var1) {
      String var2 = var1.getName();
      String var3 = TextStructureNameUtils.replaceIllegalCharacters(var2);
      return new Field(-1, var3, FieldDataType.String.name(), null, null, 1, false);
   }

   private static SegmentOccurrence getSegmentOccurrence(RelationshipCardinality var0) {
      switch (var0) {
         case ZERO_TO_N:
            return SegmentOccurrence.ZeroOrMore;
         case ONE_TO_N:
            return SegmentOccurrence.OneOrMore;
         case ZERO_OR_ONE:
            return SegmentOccurrence.ZeroOrOne;
         case ONE_TO_ONE:
            return SegmentOccurrence.OnlyOnce;
         default:
            throw new RuntimeException("Unexpected cardinality: " + var0);
      }
   }

   private TextStructure createFlatText(DatabaseObject var1) {
      Segment var2 = this.createSegment(var1, null);
      SimpleTextStructure var3 = new SimpleTextStructure("", false);
      var3.setSegment(var2);
      return var3;
   }

   private void insertIntoProject(Folder var1, TextDocument var2) {
      IntegrationProject var3 = ProjectUtils.getProject(var1);
      var3.insertEntity(var2, var1);
   }
}
