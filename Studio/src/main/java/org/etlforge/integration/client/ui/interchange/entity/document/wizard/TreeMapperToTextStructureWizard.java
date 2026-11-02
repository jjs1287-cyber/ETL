package org.etlforge.integration.client.ui.interchange.entity.document.wizard;

import com.google.common.base.Preconditions;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardPageList;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactory;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapperNode;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.ProjectUtils;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.DataStructureType;
import org.etlforge.integration.data.structure.TextStructure;
import org.etlforge.util.exception.NotSupportedException;

public final class TreeMapperToTextStructureWizard extends Wizard<Void> {
   private final Transformation transformation;
   private final TreeMapperNode sourceRoot;
   private final TreeMapperNode targetRoot;
   private final IntegrationEntity documentOwner;
   private final TreeMapperWizardContainer container;
   private TreeMapperWizardStartPage startPage;
   private TreeMapperToTextStructureConverter sourceStructureConverter;
   private TreeMapperToTextStructureConverter targetStructureConverter;

   public TreeMapperToTextStructureWizard(UiEntityFactory var1, ProjectPersistor var2, Transformation var3, TreeMapperNode var4, TreeMapperNode var5) {
      this.transformation = Preconditions.checkNotNull(var3, "tf");
      this.sourceRoot = Preconditions.checkNotNull(var4, "sourceRoot");
      this.targetRoot = Preconditions.checkNotNull(var5, "targetRoot");
      this.documentOwner = this.getDocumentOwner(ProjectUtils.getProject(var3));
      this.container = new TreeMapperWizardDialogContainer(var1, var2);
      this.container.setWizard(this);
      this.setTitle("Text Document Wizard");
   }

   public boolean isSourceStructureAvailable() {
      return isStructureAvailable(this.transformation.getInputStructure());
   }

   public boolean isTargetStructureAvailable() {
      return isStructureAvailable(this.transformation.getOutputStructure());
   }

   private static boolean isStructureAvailable(DataStructure var0) {
      return var0 != null && var0.getStructureType() != DataStructureType.None;
   }

   @Override
   public void start() {
      this.startPage = this.createStartPage();
      WizardPageList var1 = new WizardPageList();
      var1.addPage(this.startPage);
      this.setPageChain(var1);
      this.createStructureConverters(this.startPage);
      this.startPage.setInitialSelection();
      super.start();
   }

   private TreeMapperWizardStartPage createStartPage() {
      return new TreeMapperWizardStartPage(this, this.documentOwner);
   }

   private void createStructureConverters(TreeMapperWizardStartPage var1) {
      TreeMapperToTextStructureConverterFactory var2 = new TreeMapperToTextStructureConverterFactory();
      this.createSourceStructureConverter(var2, var1);
      this.createTargetStructureConverter(var2, var1);
   }

   private void createSourceStructureConverter(TreeMapperToTextStructureConverterFactory var1, TreeMapperWizardStartPage var2) {
      try {
         this.sourceStructureConverter = var1.newSourceStructureConverter(this.transformation, this.sourceRoot);
      } catch (NotSupportedException var4) {
         var2.setSourceStructureConversionNotSupported(var4.getMessage());
      }
   }

   private void createTargetStructureConverter(TreeMapperToTextStructureConverterFactory var1, TreeMapperWizardStartPage var2) {
      try {
         this.targetStructureConverter = var1.newTargetStructureConverter(this.transformation, this.targetRoot);
      } catch (NotSupportedException var4) {
         var2.setTargetStructureConversionNotSupported(var4.getMessage());
      }
   }

   private IntegrationEntity getDocumentOwner(IntegrationProject var1) {
      return var1.getRootFolder(EntityType.Document);
   }

   @Override
   protected boolean onWizardCancelled() {
      return true;
   }

   protected Void finishWizard() {
      if (this.startPage.doSourceStructureConversion()) {
         this.convertSourceStructure();
      }

      if (this.startPage.doTargetStructureConversion()) {
         this.convertTargetStructure();
      }

      return null;
   }

   private void convertSourceStructure() {
      assert this.sourceStructureConverter != null;
      TextStructure var1 = this.sourceStructureConverter.convert();
      this.startPage.applyToSourceStructure(var1);
      this.container.handleNewDocumentRequest(var1, this.documentOwner);
   }

   private void convertTargetStructure() {
      assert this.targetStructureConverter != null;
      TextStructure var1 = this.targetStructureConverter.convert();
      this.startPage.applyToTargetStructure(var1);
      this.container.handleNewDocumentRequest(var1, this.documentOwner);
   }
}
