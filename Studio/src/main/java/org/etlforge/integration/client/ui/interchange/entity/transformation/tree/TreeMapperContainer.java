package org.etlforge.integration.client.ui.interchange.entity.transformation.tree;

import javax.swing.Action;
import javax.swing.KeyStroke;
import org.etlforge.integration.client.mapping.MappingStructureFactory;
import org.etlforge.integration.client.ui.interchange.component.page.entity.EntityPageSaveJob;
import org.etlforge.integration.client.ui.interchange.entity.transformation.data.MappingTreeDataService;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.formula.FormulaDisplayer;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.system.Disposable;
import org.etlforge.ui.wait.WaitService;

public interface TreeMapperContainer extends Disposable {
   void treeMapperLoaded();

   MappingStructureFactory getMappingStructureFactory();

   MappingTreeDataService getTreeDataService();

   IntegrationProject getProject();

   void notifyOfChange(boolean var1);

   FormulaDisplayer getFormulaDisplayer();

   void startWait();

   void stopWait();

   void startBulkChange();

   void stopBulkChange();

   void registerKeyboardShortCut(KeyStroke var1, String var2, Action var3);

   void save(Transformation var1);

   void registerSaveJob(EntityPageSaveJob var1);

   WaitService getWaitService();

   Action getPageAction(String var1);

   void showDataLoadWarning(String var1, String var2);
}
