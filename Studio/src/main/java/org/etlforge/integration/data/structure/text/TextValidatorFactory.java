package org.etlforge.integration.data.structure.text;

public class TextValidatorFactory {
   public TextStructureValidator createSegmentValidator(Iterable<Segment> var1) {
      return new SegmentValidatorImpl(var1);
   }

   public TextStructureValidator createFieldValidator(Iterable<Field> var1) {
      return new FieldValidatorImpl(var1);
   }

   public TextStructureValidator createFixedFieldValidator(Iterable<Field> var1) {
      return new FixedFieldValidatorImpl(var1);
   }
}
