package org.etlforge.integration.client.ui.interchange.entity.document.page;

import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.ui.input.InputPanel;

public abstract class TextDocumentDefiner extends InputPanel {
   public abstract void applyToTextDocument(TextDocument var1) throws TextDocumentDefinitionException;

   public abstract void stopEditing();
}
