package org.etlforge.integration.client.connect.db2text.wizard;

import org.etlforge.integration.client.connect.wizard.ConnectWizardPageChain;
import org.etlforge.integration.client.connect.wizard.Folders;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.structure.editor.db.DatabaseStructureWizardPageChain;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.transformation.TransformationPipelineBrowser;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.data.structure.TextStructure;

final class WizardPages extends ConnectWizardPageChain<DatabaseStructure, TextStructure> {
   public WizardPages(DatabaseToTextWizard var1, Folders var2, EntityExplorerSupport var3) {
      super(var1, var2, new DatabaseStructureWizardPageChain(var1, var3, SourceTarget.Source), new TargetPages(var1, var3));
   }

   public DatabaseStructure getSourceStructure() {
      DatabaseStructureWizardPageChain var1 = (DatabaseStructureWizardPageChain)this.getSourcePages();
      return var1.getDefinedStructure();
   }

   public TextStructure getTargetStructure() {
      TargetPages var1 = (TargetPages)this.getTargetPages();
      return var1.getTextStructure();
   }

   public void doAdditionalConfiguration(Transformation var1) {
      this.setTargetDocumentId(var1);
   }

   private void setTargetDocumentId(Transformation var1) {
      TargetPages var2 = (TargetPages)this.getTargetPages();
      TextDocument var3 = var2.getTextDocument();
      var1.setTargetTextDocumentId(var3.getID());
   }

   public void doAdditionalConfiguration(OperationPipeline var1) {
      TargetPages var2 = (TargetPages)this.getTargetPages();
      Target var3 = var2.getTarget();
      TransformationPipelineBrowser var4 = new TransformationPipelineBrowser(var1);
      var4.setTarget(var3);
   }

   @Override
   protected void prepareTargetChain() {
      DatabaseStructure var1 = this.getSourceStructure();
      TargetPages var2 = (TargetPages)this.getTargetPages();
      var2.setSourceStructure(var1);
   }
}
