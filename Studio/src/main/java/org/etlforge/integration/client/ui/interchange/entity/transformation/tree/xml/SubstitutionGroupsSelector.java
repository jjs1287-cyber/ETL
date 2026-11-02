package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.xml;

import java.util.Collection;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.TreeComponent;
import org.etlforge.integration.structure.crom.CROM;

public final class SubstitutionGroupsSelector extends AbstractXmlExtensionSelector {
   public SubstitutionGroupsSelector(TreeComponent var1, Node var2, Collection<String> var3) {
      super(var1, var2, var3);
   }

   @Override
   protected void addExtension(Node var1, CROM var2) {
      var1.insertSubstitutionGroupNode(var2, null);
   }

   @Override
   protected CROM lookupCromNode(TreeComponent var1, String var2) {
      return var1.get_global_element(var2);
   }

   @Override
   protected String getQualifiedName(CROM var1) {
      return var1.getName();
   }
}
