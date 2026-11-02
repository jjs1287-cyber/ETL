package org.etlforge.integration.client.ui.structure.editor.text;

import org.etlforge.integration.client.ui.structure.editor.resources.StructureEditorResources;
import org.etlforge.integration.data.entity.EntityType;

final class PackageResources {
   private PackageResources() {
   }

   static class DelimiterField {
      static final String NAME = StructureEditorResources.getString("TextStructure.DelimiterField.Name");
      static final String MISSING_DELIMITER = StructureEditorResources.getString("TextStructure.DelimiterField.Missing");
      static final String INVALID_DELIMITER = StructureEditorResources.getString("TextStructure.DelimiterField.Invalid");
   }

   static class DocumentSelectionWizard {
      static final String TITLE = StructureEditorResources.formatString("TextDocument.SelectionWizard.PageTitle", EntityType.Document.getDisplayName());
      static final String COMBO_LABEL = StructureEditorResources.formatString("TextDocument.SelectionWizard.ComboLabel", EntityType.Document.getDisplayName());
      static final String MAIN_CAPTION = StructureEditorResources.formatString(
         "TextDocument.SelectionWizard.MainCaption", EntityType.Document.getIndefiniteArticle(), EntityType.Document.getDisplayName()
      );
      static final String SUB_CAPTION = StructureEditorResources.formatString("TextDocument.SelectionWizard.SubCaption", EntityType.Document.getDisplayName());
   }

   static class FileTemplateWizard {
      static final String TITLE = StructureEditorResources.getString("TextStructure.FileTemplateWizard.Title");
      static final String TYPE_SELECTOR = StructureEditorResources.getString("TextStructure.FileTemplateWizard.TypeSelector");
      static final String FILE_SELECTOR = StructureEditorResources.getString("TextStructure.FileTemplateWizard.FileSelector");
      static final String FILE_CONTENT = StructureEditorResources.getString("TextStructure.FileTemplateWizard.FileContent");
   }
}
