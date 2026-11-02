package com.etlforge.integration.client.ui.sfdc.wizard.file;

import com.google.common.base.Preconditions;
import org.etlforge.integration.data.entity.TextDocument;

public final class SourceFileStructure {
   private final TextDocument document;
   private final boolean hasHeader;

   public SourceFileStructure(TextDocument var1, boolean var2) {
      this.document = Preconditions.checkNotNull(var1);
      this.hasHeader = var2;
   }

   public TextDocument getDocument() {
      return this.document;
   }

   public void markAsTransient() {
      this.document.setTransient(true);
   }

   public boolean hasHeader() {
      return this.hasHeader;
   }
}
