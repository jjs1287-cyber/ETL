package org.etlforge.integration.client.ui.structure.editor.ldap.source;

import java.util.List;
import org.etlforge.integration.ldap.structure.LdapStructureEntryNode;
import org.etlforge.ui.text.partition.OneOfToken;
import org.etlforge.ui.text.partition.Token;
import org.etlforge.util.name.Nameables;

public class FilterExpressionTokens {
   private final Token attributeToken;
   private final Token operatorToken;
   private final Token everythingElseToken;

   public FilterExpressionTokens(LdapStructureEntryNode var1) {
      this.attributeToken = this.createAttributeToken(var1);
      this.operatorToken = this.createOperatorToken();
      this.everythingElseToken = new FilterExpressionTokens.EverythingElseToken();
   }

   public Token getAttributeToken() {
      return this.attributeToken;
   }

   public Token getOperatorToken() {
      return this.operatorToken;
   }

   public Token[] getAllTokens() {
      return new Token[]{this.attributeToken, this.operatorToken, this.everythingElseToken};
   }

   private Token createAttributeToken(LdapStructureEntryNode var1) {
      List var2 = Nameables.getNames(var1.getAttributes());
      return new OneOfToken(var2, true, true);
   }

   private Token createOperatorToken() {
      String[] var1 = new String[]{"|", "&", "!", "=", "~=", "<=", ">="};
      return new OneOfToken(var1, true, false);
   }

   private static class EverythingElseToken implements Token {
      private String lexeme;
      private int offset;

      private EverythingElseToken() {
      }

      @Override
      public String lexeme() {
         return this.lexeme;
      }

      @Override
      public boolean matches(String var1, int var2) {
         this.offset = var2;
         this.lexeme = var1.substring(var2, var2 + 1);
         return true;
      }

      @Override
      public int offset() {
         return this.offset;
      }

      @Override
      public boolean isWhitespace() {
         return false;
      }

      @Override
      public String toString() {
         return this.lexeme + " @" + this.offset;
      }
   }
}
