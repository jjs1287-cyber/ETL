package org.etlforge.integration.client.ui.script.text;

import org.etlforge.integration.client.ui.script.partition.ScriptTokenStyles;
import org.etlforge.ui.text.highlight.DefaultSyntaxHighlightDocument;

public class ScriptDocument extends DefaultSyntaxHighlightDocument {
   public ScriptDocument() {
      super(ScriptTokenStyles.STYLES);
   }
}
