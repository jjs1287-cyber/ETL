package org.etlforge.ui.text.contentassist;

import javax.swing.text.JTextComponent;

public interface AutoCompleter {
   void install(JTextComponent var1);

   void uninstall();

   void setCompletionContext(CompletionContext var1);
}
