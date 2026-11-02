package com.etlforge.integration.client.sfdc.data;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.integration.data.project.IntegrationProject;

public abstract class AbstractTextDocumentProvider {
   private final IntegrationProject project;
   private Folder parent;

   public AbstractTextDocumentProvider(IntegrationProject var1) {
      this.project = var1;
      this.parent = var1.getRootFolder(EntityType.Document);
   }

   public final void setParent(Folder var1) {
      this.parent = Preconditions.checkNotNull(var1);
   }

   public final Folder getParent() {
      return this.parent;
   }

   public final ImplementingObject<TextDocument> getDocument() {
      Optional<TextDocument> var1 = this.findExistingDocument();
      return var1.isPresent()
         ? ImplementingObject.forExistingObject(var1.get())
         : ImplementingObject.newObject(this.parent, this.createNewDocument());
   }

   private Optional<TextDocument> findExistingDocument() {
      AbstractTextDocumentProvider.ExistingDocumentFilter var1 = new AbstractTextDocumentProvider.ExistingDocumentFilter();
      return this.project.getAllEntitiesOfClass(TextDocument.class).firstMatch(var1);
   }

   protected abstract boolean isSuitable(TextDocument var1);

   protected abstract TextDocument createNewDocument();

   private class ExistingDocumentFilter implements Predicate<TextDocument> {
      private ExistingDocumentFilter() {
      }

      public boolean apply(TextDocument var1) {
         return var1.isPartOfSalesforceWizard() && AbstractTextDocumentProvider.this.isSuitable(var1);
      }
   }
}
