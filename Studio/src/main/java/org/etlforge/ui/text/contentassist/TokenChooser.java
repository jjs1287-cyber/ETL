package org.etlforge.ui.text.contentassist;

import javax.swing.text.JTextComponent;
import org.etlforge.system.callback.Receiver;

public interface TokenChooser {
   void activate(CompletionContext var1, JTextComponent var2, CompletionToken[] var3, Receiver<CompletionToken> var4);
}
