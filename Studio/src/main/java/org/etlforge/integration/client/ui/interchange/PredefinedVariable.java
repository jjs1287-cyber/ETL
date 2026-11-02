package org.etlforge.integration.client.ui.interchange;

import org.etlforge.ui.text.contentassist.CompletionToken;

public interface PredefinedVariable {
   char START_MARKER = '[';
   char END_MARKER = ']';

   String getName();

   CompletionToken asCompletionToken(int var1, int var2);
}
