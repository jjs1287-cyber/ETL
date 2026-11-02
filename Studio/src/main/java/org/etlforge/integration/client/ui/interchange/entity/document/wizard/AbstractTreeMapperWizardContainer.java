package org.etlforge.integration.client.ui.interchange.entity.document.wizard;

import com.google.common.base.Preconditions;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactory;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactoryCallback;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.integration.data.structure.TextStructure;
import org.etlforge.integration.data.structure.text.FieldDelimiter;
import org.etlforge.system.ArgCheck;

abstract class AbstractTreeMapperWizardContainer implements TreeMapperWizardContainer {
   private TreeMapperToTextStructureWizard wizard;
   private final ProjectPersistor persistor;
   private final UiEntityFactory docFactory;

   public AbstractTreeMapperWizardContainer(UiEntityFactory var1, ProjectPersistor var2) {
      this.docFactory = Preconditions.checkNotNull(var1, "docFactory");
      this.persistor = Preconditions.checkNotNull(var2, "persistor");
   }

   @Override
   public void setWizard(Wizard var1) {
      ArgCheck.checkInstance(var1, TreeMapperToTextStructureWizard.class);
      this.wizard = (TreeMapperToTextStructureWizard)var1;
      this.wizard.setDisplayer(this);
   }

   @Override
   public final void handleNewDocumentRequest(TextStructure var1, IntegrationEntity var2) {
      AbstractTreeMapperWizardContainer.FactoryCallback var3 = new AbstractTreeMapperWizardContainer.FactoryCallback(var1);
      this.docFactory.createEntity(EntityType.Document, var3);
   }

   protected final TreeMapperToTextStructureWizard getWizard() {
      return this.wizard;
   }

   private class FactoryCallback extends UiEntityFactoryCallback.Adapter {
      private final TextStructure structure;
      private TextDocument doc;

      public FactoryCallback(TextStructure var2) {
         this.structure = var2;
      }

      @Override
      public void postProjectInsertion(IntegrationEntity var1) {
         this.doc = (TextDocument)var1;
         this.applyStructure();
         this.setDelimiterAndQualifier();
         this.saveDocument();
      }

      private void applyStructure() {
         this.doc.setStructure(this.structure);
         this.doc.setName(this.structure.getName());
      }

      private void setDelimiterAndQualifier() {
         this.doc.setDelimiter(FieldDelimiter.COMMA);
         if (!this.doc.isFixedField()) {
            this.doc.setStringQualifier("\"");
         }
      }

      private void saveDocument() {
         try {
            AbstractTreeMapperWizardContainer.this.persistor.save(this.doc);
            AbstractTreeMapperWizardContainer.this.persistor.saveProjectRelations();
         } catch (InterchangeSavingException var2) {
            ErrorLog.attention(this.getClass(), "An error occurred while saving the new text document", var2);
         }
      }
   }
}
