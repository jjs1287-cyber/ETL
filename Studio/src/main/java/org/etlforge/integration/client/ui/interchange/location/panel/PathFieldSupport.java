package org.etlforge.integration.client.ui.interchange.location.panel;

import java.awt.event.ActionEvent;
import java.util.Collection;
import javax.swing.AbstractAction;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.etlforge.application.ui.widget.AutoCompleterFactory;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.ui.text.contentassist.AutoCompleter;
import org.etlforge.ui.text.contentassist.CompletionContext;
import org.etlforge.ui.text.contentassist.CompletionToken;
import org.etlforge.ui.text.contentassist.PathCompletionContext;
import org.etlforge.ui.util.KeyBindingInstaller;
import org.etlforge.ui.util.KeyUtils;

public final class PathFieldSupport {
   private final AutoCompleter autoCompleter;
   private final PathFieldSupport.CompletionContextImpl completionContext;
   private final KeyBindingInstaller refresher;

   public PathFieldSupport(PathFieldSupport.PathProvider var1, JTextComponent var2) {
      this.completionContext = new PathFieldSupport.CompletionContextImpl(var1);
      this.autoCompleter = this.installAutoComplete(this.completionContext, var2);
      this.refresher = this.bindRefresher(var2);
   }

   private AutoCompleter installAutoComplete(CompletionContext var1, JTextComponent var2) {
      AutoCompleterFactory var3 = AutoCompleterFactory.defaultFactory();
      var3.setListFactory(ComponentFactories.monoSpacedListFactory());
      var3.setMaxCharsWidth(75);
      AutoCompleter var4 = var3.createAutoCompleter(var1);
      var4.install(var2);
      return var4;
   }

   private KeyBindingInstaller bindRefresher(JTextComponent var1) {
      assert this.completionContext != null;
      KeyBindingInstaller var2 = new KeyBindingInstaller(var1);
      var2.addBinding(KeyUtils.REFRESH, "refresh_tokens", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            PathFieldSupport.this.completionContext.refresh();
         }
      });
      var2.install();
      return var2;
   }

   public void dispose() {
      if (this.autoCompleter != null) {
         this.autoCompleter.uninstall();
      }

      if (this.refresher != null) {
         this.refresher.remove();
      }
   }

   private static final class CompletionContextImpl implements CompletionContext {
      private final PathFieldSupport.PathProvider pathProvider;
      private final PathCompletionContext delegate;

      CompletionContextImpl(PathFieldSupport.PathProvider var1) {
         this.delegate = new PathCompletionContext(var1.getPaths(), var1.getSeparator(), var1.getPrefix());
         this.pathProvider = var1;
      }

      @Override
      public CompletionToken[] getAvailableTokens(Document var1, int var2) {
         return this.delegate.getAvailableTokens(var1, var2);
      }

      @Override
      public boolean delete(CompletionToken var1) {
         return this.delegate.delete(var1);
      }

      public void refresh() {
         this.pathProvider.refresh();
         Collection var1 = this.pathProvider.getPaths();
         this.delegate.refresh(var1);
      }
   }

   public interface PathProvider {
      Collection<String> getPaths();

      void refresh();

      String getSeparator();

      String getPrefix();
   }
}
