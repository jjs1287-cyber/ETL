package org.etlforge.integration.data.entity.document;

import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.integration.data.structure.ComplexTextStructure;
import org.etlforge.integration.data.structure.SimpleTextStructure;
import org.etlforge.integration.data.structure.text.Field;
import org.etlforge.integration.data.structure.text.Segment;

public class PatchForBug1969251 {
   private final TextDocument doc;

   public PatchForBug1969251(TextDocument var1) {
      this.doc = var1;
   }

   public void apply() {
      try {
         if (this.doc.isFixedField()) {
            if (this.doc.isSimpleDoc()) {
               this.updateFlatStructure();
            } else {
               this.updateComplexStructure();
            }
         }
      } catch (Exception var2) {
         var2.printStackTrace();
      }
   }

   private void updateComplexStructure() {
      ComplexTextStructure var1 = (ComplexTextStructure)this.doc.getDataStructure();

      for (Segment var3 : var1.getSegmentList()) {
         for (Field var5 : var3.getFieldList()) {
            var5.makeFixedWidth();
         }
      }
   }

   private void updateFlatStructure() {
      SimpleTextStructure var1 = (SimpleTextStructure)this.doc.getDataStructure();

      for (Field var3 : var1.getSegment().getFieldList()) {
         var3.makeFixedWidth();
      }
   }
}
