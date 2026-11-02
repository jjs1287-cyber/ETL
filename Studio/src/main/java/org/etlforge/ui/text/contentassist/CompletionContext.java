package org.etlforge.ui.text.contentassist;

import javax.swing.text.Document;

public interface CompletionContext {
   CompletionToken[] getAvailableTokens(Document var1, int var2);

   boolean delete(CompletionToken var1);
}
