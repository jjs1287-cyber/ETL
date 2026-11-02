package org.etlforge.integration.client.connect.text2db.wizard;

import org.etlforge.application.ui.wizard.ApplicationWindowWizard;
import org.etlforge.application.ui.wizard.WizardPageList;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.entity.wizard.EntitySelectionPage;
import org.etlforge.integration.client.ui.entity.wizard.NewFileLocationCallback;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.integration.data.functors.FileBasedLocationFilter;
import org.etlforge.integration.data.structure.TextStructure;

final class SourcePages extends WizardPageList {
   private final EntitySelectionPage<TextDocument> documentSelectionPage;
   private final EntitySelectionPage<Source> sourceSelectionPage;

   public SourcePages(ApplicationWindowWizard<?> var1, EntityExplorerSupport var2) {
      this.documentSelectionPage = this.createDocumentSelectionPage(var1, var2);
      this.sourceSelectionPage = this.createSourceSelectionPage(var1, var2);
      this.addPage(this.documentSelectionPage);
      this.addPage(this.sourceSelectionPage);
   }

   private EntitySelectionPage<TextDocument> createDocumentSelectionPage(ApplicationWindowWizard<?> var1, EntityExplorerSupport var2) {
      EntitySelectionPage var3 = new EntitySelectionPage<>(var1, "Select a Text Document", var2, EntityType.Document, TextDocument.class, null);
      var3.setMainCaption("Select a Text Document Definition");
      var3.setSubCaption("Select the text document that defines the structure you want to use in this connect operation");
      return var3;
   }

   private EntitySelectionPage<Source> createSourceSelectionPage(ApplicationWindowWizard<?> var1, EntityExplorerSupport var2) {
      EntitySelectionPage var3 = new EntitySelectionPage<>(var1, "Select a Source", var2, EntityType.Source, Source.class, new FileBasedLocationFilter(true));
      var3.setNewEntityCallback(new NewFileLocationCallback());
      var3.setMainCaption("Select a File Source");
      var3.setSubCaption("Select the location of the source file that will be used in this connect operation.");
      return var3;
   }

   public TextStructure getTextStructure() {
      TextDocument var1 = this.getTextDocument();
      return var1 != null ? var1.getDataStructure() : null;
   }

   public TextDocument getTextDocument() {
      return this.documentSelectionPage.getSelectedEntity();
   }

   public Source getSource() {
      return this.sourceSelectionPage.getSelectedEntity();
   }
}
