package org.etlforge.integration.data.structure.text.validation;

import org.etlforge.integration.data.structure.text.FieldDataType;

final class NotBetweenOperator extends Operator {
   public NotBetweenOperator() {
      super("Not Between", Range.class);
   }

   @Override
   public boolean isApplicableToField(FieldDataType var1) {
      return var1.isNumeric() || var1 == FieldDataType.Date;
   }
}
