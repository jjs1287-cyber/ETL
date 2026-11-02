package org.etlforge.integration.client.ui.crom.xml;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import javax.swing.JComponent;
import org.etlforge.integration.client.ui.crom.tree.CromTreeWidget;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.integration.structure.crom.CROM_Tree;

public final class DerivedTypesSelector extends AbstractXmlExtensionSelector {
   public static void open(CromTreeWidget var0, CROM var1, JComponent var2) {
      List var3 = collectDerivedTypes(var0, var1);
      if (!var3.isEmpty()) {
         DerivedTypesSelector var4 = new DerivedTypesSelector(var0, var1, var3);
         var4.open(var2, "Select Derived Type", "Select Derived Type");
      }
   }

   private static List<String> collectDerivedTypes(CromTreeWidget var0, CROM var1) {
      Map var2 = var0.getCromTree().getElementTypeMap();
      ArrayList var3 = Lists.newArrayList(var1.getTotalTypeExtentions(var2));
      if (!var1.isAbstract()) {
         String var4 = var1.getSchemaTypeExpandedName();
         CROM var5 = (CROM)var2.get(var4);
         if (var5 != null && !var5.isAbstract()) {
            var3.add(var4);
         }
      }

      return var3;
   }

   private DerivedTypesSelector(CromTreeWidget var1, CROM var2, Collection<String> var3) {
      super(var1, var2, var3);
   }

   @Override
   protected CROM lookupCromNode(CROM_Tree var1, String var2) {
      return var1.getElementTypeMap().get(var2);
   }

   @Override
   protected String getQualifiedName(CROM var1) {
      return var1.getExtension().getSchemaTypeExpandedName();
   }

   @Override
   protected void addExtension(CROM_Tree var1, CROM var2, CROM var3) {
      Stack var4 = new Stack();
      boolean var5 = true;
      new CROM(var2, var3, var5, var1, var4, "");
   }
}
