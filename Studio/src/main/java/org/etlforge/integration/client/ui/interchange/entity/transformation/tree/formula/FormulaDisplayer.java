package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.formula;

import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapperMode;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.mappings.NodeMapping;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.integration.debug.client.TestResult;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;
import org.etlforge.system.Disposable;

public interface FormulaDisplayer extends Disposable {
   void showMapping(TreeMapper var1, String var2, NodeMapping var3, String var4, TestResult var5);

   void showTestResult(TreeMapper var1, Node var2, TestResult var3);

   void insertExpression(String var1);

   void closeIfBlocking();

   void applyCurrentFormulas();

   void refreshSourceTree(MappingTreeStructure var1, String var2);

   void updateSourceTreeDisplayMode(TreeMapperMode var1);

   void setShortValueDisplay(boolean var1);
}
