package org.etlforge.ui.text.contentassist;

import com.google.common.base.Preconditions;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.text.JTextComponent;
import org.etlforge.ui.util.KeyBindingInstaller;
import org.etlforge.ui.util.KeyUtils;

public class TokenListCompletionSupport {
   private final AutoCompleter autoCompleter;
   private final TokenListCompletionContext completionContext;
   private final KeyBindingInstaller refresher;

   public TokenListCompletionSupport(JTextComponent var1) {
      this(var1, new String[0]);
   }

   public TokenListCompletionSupport(JTextComponent var1, String[] var2) {
      Preconditions.checkNotNull(var1, "tc");
      Preconditions.checkNotNull(var2, "tokens");
      this.completionContext = this.createCompletionContext(var2);
      this.autoCompleter = this.createAutoCompleter(this.completionContext);
      this.autoCompleter.install(var1);
      this.refresher = this.bindRefresher(var1);
   }

   public void setTokens(String[] var1) {
      this.completionContext.setTokens(var1);
   }

   public void refresh() {
   }

   public void setCaseSensitive(boolean var1) {
      this.completionContext.setCaseSensitive(var1);
   }

   public void dispose() {
      this.autoCompleter.uninstall();
      this.refresher.remove();
   }

   protected TokenListCompletionContext createCompletionContext(String[] var1) {
      return new TokenListCompletionContext(var1);
   }

   protected AutoCompleter createAutoCompleter(CompletionContext var1) {
      CodeEditorAutoCompleter var2 = new CodeEditorAutoCompleter();
      var2.setGrabFocusAfterCompletion(true);
      TokenChooser var3 = this.createTokenChooser();
      var2.setTokenChooser(var3);
      var2.setCompletionContext(var1);
      return var2;
   }

   protected TokenChooser createTokenChooser() {
      return new DefaultTokenChooser();
   }

   private KeyBindingInstaller bindRefresher(JTextComponent var1) {
      assert this.completionContext != null;
      KeyBindingInstaller var2 = new KeyBindingInstaller(var1);
      var2.addBinding(KeyUtils.REFRESH, "refresh_tokens", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            TokenListCompletionSupport.this.refresh();
         }
      });
      var2.install();
      return var2;
   }
}
