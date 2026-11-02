package org.etlforge.integration.client.connect.db2xml.wizard;

import org.etlforge.integration.client.connect.wizard.ConnectWizardPageChain;
import org.etlforge.integration.client.connect.wizard.Folders;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.structure.editor.db.DatabaseStructureWizardPageChain;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.transformation.TransformationPipelineBrowser;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.data.structure.XmlStructure;
import org.etlforge.system.ThreeValuedBool;

final class WizardPages extends ConnectWizardPageChain<DatabaseStructure, XmlStructure> {
   public WizardPages(DatabaseToXmlWizard var1, Folders var2, EntityExplorerSupport var3) {
      super(var1, var2, createSourceChain(var1, var3), new TargetPages(var1, var3));
   }

   private static DatabaseStructureWizardPageChain createSourceChain(DatabaseToXmlWizard var0, EntityExplorerSupport var1) {
      DatabaseStructureWizardPageChain var2 = new DatabaseStructureWizardPageChain(var0, var1, SourceTarget.Source);
      var2.setUpdateSourceTableAvailable(ThreeValuedBool.FALSE);
      return var2;
   }

   public DatabaseStructure getSourceStructure() {
      DatabaseStructureWizardPageChain var1 = (DatabaseStructureWizardPageChain)this.getSourcePages();
      return var1.getDefinedStructure();
   }

   public XmlStructure getTargetStructure() {
      TargetPages var1 = (TargetPages)this.getTargetPages();
      return var1.getXmlStructure();
   }

   public void doAdditionalConfiguration(OperationPipeline var1) {
      TargetPages var2 = (TargetPages)this.getTargetPages();
      Target var3 = var2.getLocation();
      TransformationPipelineBrowser var4 = new TransformationPipelineBrowser(var1);
      var4.setTarget(var3);
   }

   @Override
   protected void prepareTargetChain() {
      DatabaseStructure var1 = this.getSourceStructure();
      TargetPages var2 = (TargetPages)this.getTargetPages();
      var2.setDatabaseStructure(var1);
   }
}
