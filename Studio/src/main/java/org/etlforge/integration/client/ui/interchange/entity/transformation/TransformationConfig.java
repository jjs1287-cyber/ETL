package org.etlforge.integration.client.ui.interchange.entity.transformation;

import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.mapping.MappingStructureFactory;
import org.etlforge.integration.client.ui.entity.wizard.EntityWizardFactory;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.component.page.entity.PageConfig;
import org.etlforge.integration.client.ui.interchange.entity.AbstractEntityConfig;
import org.etlforge.integration.client.ui.interchange.entity.TransformationToOperationFactory;
import org.etlforge.integration.client.ui.interchange.entity.transformation.data.MappingTreeDataServiceFactory;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.FormulaDisplayerFactory;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TestActionsFactory;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPageConfig;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.formula.FormulaBuilderUiFactory;
import org.etlforge.integration.client.ui.structure.editor.StructureEditorFactories;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.integration.debug.serverapi.DataFileProvider;
import org.etlforge.integration.uidelegate.entity.tooltip.IntegrationEntityToolTip;

public final class TransformationConfig extends AbstractEntityConfig {
   private final TransformationWizardFactory wizardFactory;
   private final MappingStructureFactory mappingStructureFactory;
   private final MappingTreeDataServiceFactory dataServiceFactory;
   private final DataFileProvider dataFileProvider;
   private final TestActionsFactory testActionsFactory;
   private final FormulaBuilderUiFactory formulaBuilderUiFactory;
   private final StructureEditorFactories structureEditorFactories;
   private final TransformationToOperationFactory operationFactory;
   private boolean includeNameField = true;
   private boolean useToolKitMenu = true;

   public TransformationConfig(
      TransformationWizardFactory var1,
      MappingStructureFactory var2,
      MappingTreeDataServiceFactory var3,
      DataFileProvider var4,
      TestActionsFactory var5,
      FormulaBuilderUiFactory var6,
      StructureEditorFactories var7,
      TransformationToOperationFactory var8
   ) {
      super(EntityType.Transformation);
      this.wizardFactory = var1;
      this.mappingStructureFactory = var2;
      this.dataServiceFactory = var3;
      this.dataFileProvider = var4;
      this.testActionsFactory = var5;
      this.formulaBuilderUiFactory = var6;
      this.structureEditorFactories = var7;
      this.operationFactory = var8;
      this.setSearchAndReplaceMetaData(new TransformationSearchAndReplaceProperties());
      this.setSearchValuesCollector(new TransformationSearchValuesCollector());
   }

   @Override
   protected IntegrationEntityPage createNewPage(IntegrationEntity var1) {
      return new TransformationPage((Transformation)var1);
   }

   public void setIncludeNameField(boolean var1) {
      this.includeNameField = var1;
   }

   public void setUseToolKitMenu(boolean var1) {
      this.useToolKitMenu = var1;
   }

   @Override
   protected PageConfig createPageConfig(ApplicationWindow var1, IntegrationEntity var2, ProjectPersistor var3) {
      TransformationPageConfig var4 = new TransformationPageConfig(
         var1,
         var3,
         this.getEntityFactory(),
         this.mappingStructureFactory,
         this.dataServiceFactory,
         this.dataFileProvider,
         this.testActionsFactory,
         new FormulaDisplayerFactory(this.formulaBuilderUiFactory),
         this.structureEditorFactories,
         this.operationFactory
      );
      var4.setIncludeNameField(this.includeNameField);
      var4.setUseToolKitMenu(this.useToolKitMenu);
      return var4;
   }

   @Override
   public EntityWizardFactory getEntityWizardFactory() {
      return this.wizardFactory;
   }

   @Override
   public IntegrationEntityToolTip getToolTip(IntegrationEntity var1) {
      return new TransformationToolTip((Transformation)var1);
   }
}
