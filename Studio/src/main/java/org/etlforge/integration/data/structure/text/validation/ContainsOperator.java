package org.etlforge.integration.data.structure.text.validation;

import org.etlforge.integration.data.structure.text.FieldDataType;

final class ContainsOperator extends Operator {
   public ContainsOperator() {
      super("Contains", StringArgument.class);
   }

   @Override
   public boolean isApplicableToField(FieldDataType var1) {
      return var1 == FieldDataType.String;
   }
}
