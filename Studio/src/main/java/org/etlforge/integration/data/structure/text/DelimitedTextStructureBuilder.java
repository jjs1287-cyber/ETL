package org.etlforge.integration.data.structure.text;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import org.etlforge.integration.data.structure.ComplexTextStructure;
import org.etlforge.integration.data.structure.SimpleTextStructure;
import org.etlforge.integration.data.structure.TextStructure;

public final class DelimitedTextStructureBuilder {
   private final Segment rootSegment = Segment.newRootSegment();
   private final List<Segment> completedSegments = Lists.newArrayList();
   private final Stack<Segment> segmentStack;
   private Segment currentSegment = this.rootSegment;
   private String name;

   public DelimitedTextStructureBuilder() {
      this.segmentStack = new Stack<>();
      this.segmentStack.push(this.rootSegment);
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public void addField(String var1, FieldDataType var2, String var3, String var4) {
      if (this.currentSegment == null) {
         throw new IllegalStateException("Cannot add the field \"" + var1 + "\" because there is no open segment.");
      }

      String var5 = this.getValidatedName(var1);
      Field var6 = new Field(-1, var5, var2.toString(), var3 == null ? "" : var3, var4 == null ? "" : var4, 0, false);
      this.currentSegment.insertField(var6);
   }

   private String getValidatedName(String var1) {
      String var2 = var1.trim();
      Preconditions.checkArgument(!var2.isEmpty(), "name cannot be empty.");
      return TextStructureNameUtils.replaceIllegalCharacters(var2);
   }

   public void addSegment(String var1, SegmentOccurrence var2) {
      String var3 = this.getValidatedName(var1);
      String var4 = this.currentSegment == null ? "_Root" : this.currentSegment.getName();
      Segment var5 = new Segment(var3);
      var5.setParentName(var4);
      var5.setOccurrence(var2);
      this.completedSegments.add(var5);
      this.segmentStack.push(var5);
      this.currentSegment = var5;
   }

   public void closeSegment() {
      if (!this.segmentStack.isEmpty()) {
         this.segmentStack.pop();
      }

      this.currentSegment = this.segmentStack.isEmpty() ? null : this.segmentStack.peek();
   }

   public TextStructure getStructure() {
      TextStructure var1 = null;
      if (this.completedSegments.size() == 1) {
         var1 = this.createSimpleStructure();
      } else {
         var1 = this.createComplexStructure();
      }

      return var1;
   }

   private TextStructure createSimpleStructure() {
      assert this.completedSegments.size() == 1;
      SimpleTextStructure var1 = new SimpleTextStructure(this.name, false);
      Segment var2 = this.completedSegments.get(0);
      var2.setName("");
      var2.setParentName("");
      var1.setSegment(var2);
      return var1;
   }

   private TextStructure createComplexStructure() {
      this.generateSegmentIds();
      ComplexTextStructure var1 = new ComplexTextStructure(this.name, false);
      var1.addSegment(this.rootSegment);

      for (Segment var3 : this.completedSegments) {
         var1.addSegment(var3);
      }

      return var1;
   }

   private void generateSegmentIds() {
      DelimitedTextStructureBuilder.SegmentIdGenerator var1 = new DelimitedTextStructureBuilder.SegmentIdGenerator();
      var1.generate(this.completedSegments);
   }

   private static final class SegmentIdGenerator {
      private final Set<String> ids = Sets.newHashSet();

      private SegmentIdGenerator() {
      }

      public void generate(List<Segment> var1) {
         for (Segment var3 : var1) {
            List var4 = var3.getFieldList();
            var4.add(0, this.getIdField(var3));
         }
      }

      private Field getIdField(Segment var1) {
         String var2 = this.getIdName(var1);
         String var3 = this.getId(var1);
         return Field.newDelimitedIdentifier(var2, var3);
      }

      private String getIdName(Segment var1) {
         StringBuilder var2 = new StringBuilder("ID_");
         Set var3 = this.getFieldNames(var1);

         while (var3.contains(var2.toString())) {
            var2.append("_");
         }

         return var2.toString();
      }

      private Set<String> getFieldNames(Segment var1) {
         HashSet var2 = Sets.newHashSet();

         for (Field var4 : var1.getFieldList()) {
            var2.add(var4.getName());
         }

         return var2;
      }

      private String getId(Segment var1) {
         String var2 = var1.getName();
         return this.guaranteeUniqueness(var2);
      }

      private String guaranteeUniqueness(String var1) {
         if (!this.ids.contains(var1)) {
            this.ids.add(var1);
            return var1;
         }

         int var2 = 2;
         String var3 = null;

         do {
            var3 = var1 + var2;
            var2++;
         } while (this.ids.contains(var3));

         this.ids.add(var3);
         return var3;
      }
   }
}
