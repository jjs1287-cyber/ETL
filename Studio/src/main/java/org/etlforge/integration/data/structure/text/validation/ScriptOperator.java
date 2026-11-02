package org.etlforge.integration.data.structure.text.validation;

import org.etlforge.integration.data.structure.text.FieldDataType;

final class ScriptOperator extends Operator {
   public ScriptOperator() {
      super("Script", ScriptArgument.class);
   }

   @Override
   public boolean isApplicableToField(FieldDataType var1) {
      return true;
   }
}
