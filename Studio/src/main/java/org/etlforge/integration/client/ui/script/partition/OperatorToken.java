package org.etlforge.integration.client.ui.script.partition;

import org.etlforge.ui.text.partition.OneOfToken;
import org.etlforge.ui.text.partition.Token;

class OperatorToken extends OneOfToken implements Token {
   private static final String[] TOKENS = new String[]{"<=", ">=", "==", "!=", ">", "<", "&", "|", "^", "+", "-", "*", "/", ";"};

   public OperatorToken() {
      super(TOKENS, true);
   }
}
