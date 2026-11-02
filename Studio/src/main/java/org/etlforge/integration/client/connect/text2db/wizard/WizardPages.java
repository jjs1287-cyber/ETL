package org.etlforge.integration.client.connect.text2db.wizard;

import org.etlforge.integration.client.connect.wizard.ConnectWizardPageChain;
import org.etlforge.integration.client.connect.wizard.Folders;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.structure.editor.db.DatabaseStructureWizardPageChain;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.transformation.TransformationPipelineBrowser;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.data.structure.TextStructure;

final class WizardPages extends ConnectWizardPageChain<TextStructure, DatabaseStructure> {
   public WizardPages(TextToDatabaseWizard var1, Folders var2, EntityExplorerSupport var3) {
      super(var1, var2, new SourcePages(var1, var3), new DatabaseStructureWizardPageChain(var1, var3, SourceTarget.Target));
   }

   public TextStructure getSourceStructure() {
      SourcePages var1 = (SourcePages)this.getSourcePages();
      return var1.getTextStructure();
   }

   public DatabaseStructure getTargetStructure() {
      DatabaseStructureWizardPageChain var1 = (DatabaseStructureWizardPageChain)this.getTargetPages();
      return var1.getDefinedStructure();
   }

   public void doAdditionalConfiguration(Transformation var1) {
      this.applyInsertMode(var1);
      this.setSourceDocumentId(var1);
   }

   private void applyInsertMode(Transformation var1) {
      DatabaseStructureWizardPageChain var2 = (DatabaseStructureWizardPageChain)this.getTargetPages();
      var2.applyInsertMode(var1);
   }

   private void setSourceDocumentId(Transformation var1) {
      SourcePages var2 = (SourcePages)this.getSourcePages();
      TextDocument var3 = var2.getTextDocument();
      var1.setSourceTextDocumentId(var3.getID());
   }

   public void doAdditionalConfiguration(OperationPipeline var1) {
      SourcePages var2 = (SourcePages)this.getSourcePages();
      Source var3 = var2.getSource();
      TransformationPipelineBrowser var4 = new TransformationPipelineBrowser(var1);
      var4.setSource(var3);
   }
}
