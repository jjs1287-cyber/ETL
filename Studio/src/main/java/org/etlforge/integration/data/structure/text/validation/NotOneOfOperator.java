package org.etlforge.integration.data.structure.text.validation;

import org.etlforge.integration.data.structure.text.FieldDataType;

final class NotOneOfOperator extends Operator {
   public NotOneOfOperator() {
      super("Not One Of", StringList.class);
   }

   @Override
   public boolean isApplicableToField(FieldDataType var1) {
      return var1 == FieldDataType.String;
   }
}
