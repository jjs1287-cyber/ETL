package org.etlforge.integration.data.structure.text.validation;

import org.etlforge.integration.data.structure.text.FieldDataType;

final class OneOfOperator extends Operator {
   public OneOfOperator() {
      super("One Of", StringList.class);
   }

   @Override
   public boolean isApplicableToField(FieldDataType var1) {
      return var1 == FieldDataType.String;
   }
}
