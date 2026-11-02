package org.etlforge.integration.client.ui.crom.xml;

import java.util.Stack;
import javax.swing.JComponent;
import org.etlforge.integration.client.ui.crom.tree.CromTreeWidget;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.integration.structure.crom.CROM_Tree;

public final class SubstitutionGroupsSelector extends AbstractXmlExtensionSelector {
   public static void open(CromTreeWidget var0, CROM var1, JComponent var2) {
      SubstitutionGroupsSelector var3 = new SubstitutionGroupsSelector(var0, var1);
      var3.open(var2, "Select Substitution Group", "Select Substitution Group");
   }

   private SubstitutionGroupsSelector(CromTreeWidget var1, CROM var2) {
      super(var1, var2, var2.getSubstitutionGroup());
   }

   @Override
   protected CROM lookupCromNode(CROM_Tree var1, String var2) {
      return var1.getElements().get(var2);
   }

   @Override
   protected String getQualifiedName(CROM var1) {
      return var1.getName();
   }

   @Override
   protected void addExtension(CROM_Tree var1, CROM var2, CROM var3) {
      Stack var4 = new Stack();
      boolean var5 = true;
      CROM var6 = new CROM(var2, var3, var5, var1, var4, "");
      var6.setType(1L);
   }
}
