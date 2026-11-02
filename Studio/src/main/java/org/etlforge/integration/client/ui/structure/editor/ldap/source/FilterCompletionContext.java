package org.etlforge.integration.client.ui.structure.editor.ldap.source;

import java.util.List;
import javax.swing.Icon;
import javax.swing.text.Document;
import org.etlforge.integration.ldap.structure.LdapStructureEntryNode;
import org.etlforge.ui.text.contentassist.CompletionContext;
import org.etlforge.ui.text.contentassist.CompletionToken;
import org.etlforge.ui.text.contentassist.TokenDecorator;
import org.etlforge.ui.text.contentassist.TokenListCompletionContext;
import org.etlforge.util.name.Nameables;

public final class FilterCompletionContext implements CompletionContext {
   private final TokenListCompletionContext delegate;

   public FilterCompletionContext(LdapStructureEntryNode var1) {
      List var2 = Nameables.getNames(var1.getSelectedAttributes());
      this.delegate = new TokenListCompletionContext(var2);
      this.delegate.setUseWordBoundaries(true);
      this.delegate.setTokenDecorator(new FilterCompletionContext.Decorator());
   }

   @Override
   public CompletionToken[] getAvailableTokens(Document var1, int var2) {
      return this.delegate.getAvailableTokens(var1, var2);
   }

   @Override
   public boolean delete(CompletionToken var1) {
      return this.delegate.delete(var1);
   }

   private static class Decorator implements TokenDecorator {
      private Decorator() {
      }

      @Override
      public String getDisplayName(String var1) {
         return "[A] " + var1;
      }

      @Override
      public Icon getIcon(String var1) {
         return null;
      }

      @Override
      public String getInfo(String var1) {
         return null;
      }
   }
}
