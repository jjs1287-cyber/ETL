package org.etlforge.integration.client.ui.script.builder;

import javax.swing.Icon;
import org.etlforge.system.Disposable;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiProvider;

public interface ScriptBuilderSection extends Disposable, UiProvider {
   ScriptBuilderSection.Title getTitleDecoration();

   void collectArgumentSuggestions(FunctionArgumentSuggestions var1);

   void setReceiver(Receiver<InsertionToken> var1);

   void setMode(ScriptBuilderMode var1);

   final class Title {
      private final String text;
      private final Icon icon;

      public Title(String var1, Icon var2) {
         this.text = var1;
         this.icon = var2;
      }

      public String getText() {
         return this.text;
      }

      public Icon getIcon() {
         return this.icon;
      }
   }
}
