package org.etlforge.integration.data.script;

import javax.swing.text.JTextComponent;
import org.etlforge.ui.text.contentassist.CompletionToken;

public interface FunctionInserter {
   void insertFunction(JTextComponent var1);

   CompletionToken getCompletionToken(int var1, int var2, JTextComponent var3);
}
