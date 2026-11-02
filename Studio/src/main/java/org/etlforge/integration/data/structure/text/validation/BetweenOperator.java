package org.etlforge.integration.data.structure.text.validation;

import org.etlforge.integration.data.structure.text.FieldDataType;

final class BetweenOperator extends Operator {
   public BetweenOperator() {
      super("Between", Range.class);
   }

   @Override
   public boolean isApplicableToField(FieldDataType var1) {
      return var1.isNumeric() || var1 == FieldDataType.Date;
   }
}
