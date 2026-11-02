package org.etlforge.integration.data.entity.transformation;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.id.TextDocumentId;
import org.etlforge.integration.data.entity.link.EntityLink;
import org.etlforge.integration.data.entity.link.LinkUtils;
import org.etlforge.integration.data.entity.link.StrongEntityLink;

class TransformationDocumentLinkFactory {
   private final Transformation transformation;
   private final IntegrationEntityLookup dataLookup;

   public TransformationDocumentLinkFactory(Transformation var1, IntegrationEntityLookup var2) {
      this.transformation = var1;
      this.dataLookup = var2;
   }

   public EntityLink getInputAndOutputLink() {
      return this.getInputLink();
   }

   public EntityLink getInputLink() {
      TextDocumentId var1 = this.transformation.getSourceTextDocumentId();
      if (var1 != null) {
         TextDocument var2 = this.dataLookup.getEntity(var1, TextDocument.class);
         if (var2 != null) {
            return new StrongEntityLink(var2, this.transformation);
         }

         LinkUtils.logMissingLink(this.transformation, EntityType.Document, var1);
      }

      return null;
   }

   public EntityLink getOutputLink() {
      TextDocumentId var1 = this.transformation.getTargetTextDocumentId();
      if (var1 != null) {
         TextDocument var2 = this.dataLookup.getEntity(var1, TextDocument.class);
         if (var2 != null) {
            return new StrongEntityLink(var2, this.transformation);
         }

         LinkUtils.logMissingLink(this.transformation, EntityType.Document, var1);
      }

      return null;
   }
}
