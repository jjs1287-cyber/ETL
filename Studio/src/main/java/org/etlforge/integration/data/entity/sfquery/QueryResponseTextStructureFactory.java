package org.etlforge.integration.data.entity.sfquery;

import com.google.common.base.Preconditions;
import org.etlforge.integration.data.structure.ComplexTextStructure;
import org.etlforge.integration.data.structure.SimpleTextStructure;
import org.etlforge.integration.data.structure.TextStructure;
import org.etlforge.integration.data.structure.text.Field;
import org.etlforge.integration.data.structure.text.FieldDataType;
import org.etlforge.integration.data.structure.text.Segment;
import org.etlforge.integration.data.structure.text.SegmentOccurrence;
import org.etlforge.integration.data.structure.text.TextStructureNameUtils;
import org.etlforge.integration.salesforce.parser.ParsedQuery;
import org.etlforge.lang.KongaStringUtils;

public final class QueryResponseTextStructureFactory {
   public static TextStructure buildStructure(ParsedQuery var0) {
      Preconditions.checkArgument(var0.isValid(), "Invalid query");
      return var0.hasSubQueries() ? createComplexStructure(var0) : createFlatStructure(var0);
   }

   private static ComplexTextStructure createComplexStructure(ParsedQuery var0) {
      ComplexTextStructure var1 = new ComplexTextStructure("", false);
      Segment var2 = Segment.newRootSegment();
      var1.addSegment(var2);
      populateComplexTextStructure(var1, var2, var0);
      return var1;
   }

   private static void populateComplexTextStructure(ComplexTextStructure var0, Segment var1, ParsedQuery var2) {
      String var3 = getSegmentName(var2);
      Segment var4 = new Segment(var3);
      Field var5 = Field.newDelimitedIdentifier("Segment_ID", var3);
      var4.fieldList.add(var5);
      addFieldsToSegment(var2, var4);
      var4.setParentName(var1.getName());
      var4.setOccurrence(SegmentOccurrence.ZeroOrMore);
      var0.addSegment(var4);

      for (ParsedQuery var7 : var2.getSubQueries()) {
         populateComplexTextStructure(var0, var4, var7);
      }
   }

   private static String getSegmentName(ParsedQuery var0) {
      String var1 = var0.getObjectName();
      return KongaStringUtils.substringAfterLastIfPresent(var1, ".");
   }

   private static SimpleTextStructure createFlatStructure(ParsedQuery var0) {
      Segment var1 = new Segment("");
      addFieldsToSegment(var0, var1);
      SimpleTextStructure var2 = new SimpleTextStructure("", false);
      var2.setSegment(var1);
      return var2;
   }

   private static void addFieldsToSegment(ParsedQuery var0, Segment var1) {
      for (String var3 : var0.getFieldNames()) {
         String var4 = getTextFieldName(var3);
         Field var5 = new Field(-1, var4, FieldDataType.String.name(), null, null, 1, false);
         var1.fieldList.add(var5);
      }
   }

   public static String getTextFieldName(String var0) {
      return TextStructureNameUtils.replaceIllegalCharacters(var0);
   }

   private QueryResponseTextStructureFactory() {
   }
}
