package org.etlforge.integration.data.structure.text.validation;

import org.etlforge.integration.data.structure.text.FieldDataType;

final class NotContainsOperator extends Operator {
   public NotContainsOperator() {
      super("Does Not Contain", StringArgument.class);
   }

   @Override
   public boolean isApplicableToField(FieldDataType var1) {
      return var1 == FieldDataType.String;
   }
}
