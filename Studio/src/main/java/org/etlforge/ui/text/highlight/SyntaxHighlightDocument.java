package org.etlforge.ui.text.highlight;

import javax.swing.text.StyledDocument;
import org.etlforge.ui.text.partition.TextPartitioner;

public interface SyntaxHighlightDocument extends StyledDocument {
   void setPartitioner(TextPartitioner var1);
}
