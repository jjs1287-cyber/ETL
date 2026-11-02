package org.etlforge.integration.client.connect.xml2db.wizard;

import org.etlforge.integration.client.connect.wizard.ConnectWizardPageChain;
import org.etlforge.integration.client.connect.wizard.Folders;
import org.etlforge.integration.client.connect.wizard.xml.XmlPageChain;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.structure.editor.db.DatabaseStructureWizardPageChain;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.transformation.TransformationPipelineBrowser;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.data.structure.XmlStructure;

final class WizardPages extends ConnectWizardPageChain<XmlStructure, DatabaseStructure> {
   public WizardPages(XmlToDatabaseWizard var1, Folders var2, EntityExplorerSupport var3) {
      super(var1, var2, createSourcePages(var1, var3), new DatabaseStructureWizardPageChain(var1, var3, SourceTarget.Target));
   }

   private static XmlPageChain<Source> createSourcePages(XmlToDatabaseWizard var0, EntityExplorerSupport var1) {
      return new XmlPageChain<>(var0, var1, EntityType.Source, Source.class);
   }

   public XmlStructure getSourceStructure() {
      XmlPageChain var1 = (XmlPageChain)this.getSourcePages();
      return var1.getXmlStructure();
   }

   public DatabaseStructure getTargetStructure() {
      DatabaseStructureWizardPageChain var1 = (DatabaseStructureWizardPageChain)this.getTargetPages();
      return var1.getDefinedStructure();
   }

   public void doAdditionalConfiguration(Transformation var1) {
      this.applyInsertMode(var1);
   }

   private void applyInsertMode(Transformation var1) {
      DatabaseStructureWizardPageChain var2 = (DatabaseStructureWizardPageChain)this.getTargetPages();
      var2.applyInsertMode(var1);
   }

   public void doAdditionalConfiguration(OperationPipeline var1) {
      XmlPageChain var2 = (XmlPageChain)this.getSourcePages();
      Source var3 = (Source)var2.getLocation();
      TransformationPipelineBrowser var4 = new TransformationPipelineBrowser(var1);
      var4.setSource(var3);
   }
}
