package org.etlforge.integration.client.ui.script.partition;

import org.etlforge.integration.data.script.ScriptConstants;
import org.etlforge.ui.text.partition.AbstractTokenScanner;
import org.etlforge.ui.text.partition.Token;

public class ScriptTokenScanner extends AbstractTokenScanner {
   private static final Token[] WITHIN_TRANS = new Token[]{
      Tokens.END_TRANS,
      Tokens.SINGLE_LINE_COMMENT,
      Tokens.BLOCK_COMMENT,
      Tokens.STRING,
      Tokens.RAISE_ERROR,
      Tokens.FUNCTION,
      Tokens.OPERATOR,
      Tokens.BOOLEAN,
      Tokens.GROUPING,
      Tokens.NUMERIC_CONSTANT,
      Tokens.GLOBAL_DATA_ELEMENT,
      Tokens.SOURCE_PATH,
      Tokens.WORD_TOKEN
   };
   private static final Token[] OUTSIDE_OF_TRANS = new Token[]{Tokens.BEGIN_TRANS, Tokens.NO_CODE};
   private static final Token[] WITHIN_JAVASCRIPT = new Token[]{Tokens.END_TRANS,Tokens.SINGLE_LINE_COMMENT,Tokens.BLOCK_COMMENT,Tokens.STRING,Tokens.JAVASCRIPT_KEYWORD,Tokens.FUNCTION,Tokens.OPERATOR,Tokens.BOOLEAN,Tokens.GROUPING,Tokens.NUMERIC_CONSTANT,Tokens.GLOBAL_DATA_ELEMENT,Tokens.WORD_TOKEN};
   private static final Token[] JAVASCRIPT_BODY = java.util.Arrays.copyOfRange(WITHIN_JAVASCRIPT,1,WITHIN_JAVASCRIPT.length);
   private final boolean requireTransTags;
   private boolean isInTrans;
   private boolean isJavaScript;

   public ScriptTokenScanner() {
      this(true);
   }

   public ScriptTokenScanner(boolean var1) {
      this("", var1);
   }

   public ScriptTokenScanner(String var1) {
      this(var1, true);
   }

   public ScriptTokenScanner(String var1, boolean var2) {
      super(var1);
      this.requireTransTags = var2;
      this.isInTrans = !var2;
      this.setWhitespaceAware(true);
   }

   @Override
   public void setPosition(int var1) {
      if (this.requireTransTags) {
         super.setPosition(0);this.isInTrans=false;this.isJavaScript=false;
         while(this.getPosition()<var1){
            Token token=super.next();if(token==null)break;
            if(token==Tokens.BEGIN_TRANS){this.isInTrans=true;this.isJavaScript=token.lexeme().equals("<javascript>");}else if(token==Tokens.END_TRANS){this.isInTrans=false;this.isJavaScript=false;}
         }
      }
      super.setPosition(var1);
   }

   @Override
   protected Token[] getPossibleTokens() {
      if(this.isInTrans&&this.isJavaScript){
         java.util.List<int[]> blocks=org.etlforge.integration.data.script.NativeJavaScript.cached(this.getInput());
         if(blocks!=null){for(int[] block:blocks)if(block[0]==1&&this.getPosition()>=block[2]&&this.getPosition()<block[3])return JAVASCRIPT_BODY;}
      }
      return this.isInTrans ? (this.isJavaScript?WITHIN_JAVASCRIPT:WITHIN_TRANS) : OUTSIDE_OF_TRANS;
   }

   @Override
   public Token next() {
      Token var1 = super.next();
      if (this.requireTransTags) {
         if (var1 == Tokens.BEGIN_TRANS) {
            this.isInTrans = true;
            this.isJavaScript = var1.lexeme().equals("<javascript>");
         } else if (var1 == Tokens.END_TRANS) {
            this.isInTrans = false;
            this.isJavaScript = false;
         }
      }

      return var1;
   }
}
