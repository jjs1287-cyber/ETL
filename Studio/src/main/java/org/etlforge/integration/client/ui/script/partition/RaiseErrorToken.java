package org.etlforge.integration.client.ui.script.partition;

import org.apache.commons.lang.StringUtils;
import org.etlforge.ui.text.partition.OneOfToken;
import org.etlforge.ui.text.partition.Token;

class RaiseErrorToken extends OneOfToken implements Token {
   public RaiseErrorToken() {
      super(new String[]{"RaiseError("}, false);
   }

   @Override
   public String lexeme() {
      return StringUtils.removeEnd(super.lexeme(), "(");
   }
}
