package org.etlforge.integration.client.ui.interchange.entity.transformation.page;

import javax.swing.Action;
import javax.swing.KeyStroke;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.mapping.MappingStructureFactory;
import org.etlforge.integration.client.ui.interchange.component.page.entity.EntityPageSaveJob;
import org.etlforge.integration.client.ui.interchange.entity.transformation.data.MappingTreeDataService;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapperContainer;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.formula.FormulaDisplayer;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.ProjectUtils;
import org.etlforge.ui.wait.WaitService;

final class TreeMapperContainerImpl implements TreeMapperContainer {
   private final TransformationPanel panel;
   private final MappingStructureFactory mappingStructureFactory;
   private final FormulaDisplayer formulaDisplayer;

   public TreeMapperContainerImpl(TransformationPanel var1, MappingStructureFactory var2, FormulaDisplayer var3) {
      this.panel = var1;
      this.mappingStructureFactory = var2;
      this.formulaDisplayer = var3;
   }

   @Override
   public void dispose() {
      this.formulaDisplayer.dispose();
   }

   @Override
   public void treeMapperLoaded() {
      this.panel.treeMapperLoaded();
   }

   @Override
   public IntegrationProject getProject() {
      Transformation var1 = this.panel.getPage().getObject();
      return ProjectUtils.getProject(var1);
   }

   @Override
   public MappingStructureFactory getMappingStructureFactory() {
      return this.mappingStructureFactory;
   }

   @Override
   public MappingTreeDataService getTreeDataService() {
      return this.panel.getPage().getTreeDataService();
   }

   @Override
   public void notifyOfChange(boolean var1) {
      if (var1) {
         this.panel.undoableEditHappened();
      }

      this.panel.inputChanged();
   }

   @Override
   public FormulaDisplayer getFormulaDisplayer() {
      return this.formulaDisplayer;
   }

   @Override
   public void startWait() {
      this.panel.startWait();
   }

   @Override
   public void stopWait() {
      this.panel.stopWait();
   }

   @Override
   public void startBulkChange() {
      this.panel.setSendUndoEvents(false);
   }

   @Override
   public void stopBulkChange() {
      this.panel.setSendUndoEvents(true);
      this.notifyOfChange(true);
   }

   @Override
   public void registerKeyboardShortCut(KeyStroke var1, String var2, Action var3) {
      this.panel.getUi().getInputMap(1).put(var1, var2);
      this.panel.getUi().getActionMap().put(var2, var3);
   }

   @Override
   public void save(Transformation var1) {
      try {
         boolean var2 = var1.save();
         if (!var2) {
            throw new InterchangeSavingException("A persistor could not be found. This is a bug.");
         }
      } catch (InterchangeSavingException var3) {
         ErrorLog.attention(TreeMapperContainerImpl.class, "The transformation \"" + var1.getName() + "\" could not be saved: " + var3.getMessage(), var3);
      }
   }

   @Override
   public WaitService getWaitService() {
      return this.panel.getPage().getWaitService();
   }

   @Override
   public void registerSaveJob(EntityPageSaveJob var1) {
      this.panel.getPage().addSaveJob(var1);
   }

   @Override
   public Action getPageAction(String var1) {
      TransformationPage var2 = this.panel.getPage();
      return var2.getPageAction(var1);
   }

   @Override
   public void showDataLoadWarning(String var1, String var2) {
      this.panel.getPage().showDataLoadWarning(var1, var2);
   }
}
