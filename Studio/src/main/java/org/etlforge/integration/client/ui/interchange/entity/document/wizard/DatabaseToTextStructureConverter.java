package org.etlforge.integration.client.ui.interchange.entity.document.wizard;

import java.util.Iterator;
import java.util.Stack;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeAttribute;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeElement;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapperNode;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapperNodeVisitor;
import org.etlforge.integration.data.structure.TextStructure;
import org.etlforge.integration.data.structure.text.DelimitedTextStructureBuilder;
import org.etlforge.integration.data.structure.text.FieldDataType;
import org.etlforge.integration.data.structure.text.SegmentOccurrence;

final class DatabaseToTextStructureConverter implements TreeMapperToTextStructureConverter {
   private final TreeMapperNode rootNode;

   public DatabaseToTextStructureConverter(TreeMapperNode var1) {
      assert var1 != null;
      this.rootNode = var1;
   }

   @Override
   public TextStructure convert() {
      DatabaseToTextStructureConverter.VisitorImpl var1 = new DatabaseToTextStructureConverter.VisitorImpl();
      Iterator var2 = this.rootNode.children();

      while (var2.hasNext()) {
         this.runVisitor((TreeMapperNode)var2.next(), var1);
         var1.popStackCompletely();
      }

      return var1.getResultingStructure();
   }

   private void runVisitor(TreeMapperNode var1, DatabaseToTextStructureConverter.VisitorImpl var2) {
      var1.accept(var2);
      Iterator var3 = var1.children();

      while (var3.hasNext()) {
         this.runVisitor((TreeMapperNode)var3.next(), var2);
      }
   }

   private static class VisitorImpl implements TreeMapperNodeVisitor {
      private final Stack<TreeElement> elements = new Stack<>();
      private final DelimitedTextStructureBuilder builder = new DelimitedTextStructureBuilder();

      public VisitorImpl() {
      }

      @Override
      public void visit(TreeAttribute var1) {
         String var2 = var1.getName();
         FieldDataType var3 = FieldDataType.String;
         String var4 = var1.getDefaultValue();
         this.builder.addField(var2, var3, var4, null);
      }

      @Override
      public void visit(TreeElement var1) {
         this.popStack(var1);
         this.pushStack(var1);
         String var2 = var1.getName();
         SegmentOccurrence var3 = ConversionUtils.segmentOccurrenceFromElement(var1);
         this.builder.addSegment(var2, var3);
      }

      private void popStack(TreeElement var1) {
         if (!this.elements.empty()) {
            for (TreeElement var2 = this.elements.peek(); var2 != var1.getParent(); var2 = this.elements.empty() ? null : this.elements.peek()) {
               this.builder.closeSegment();
               this.elements.pop();
            }
         }
      }

      private void pushStack(TreeElement var1) {
         this.elements.push(var1);
      }

      private void popStackCompletely() {
         while (!this.elements.empty()) {
            this.builder.closeSegment();
            this.elements.pop();
         }
      }

      public TextStructure getResultingStructure() {
         return this.builder.getStructure();
      }
   }
}
