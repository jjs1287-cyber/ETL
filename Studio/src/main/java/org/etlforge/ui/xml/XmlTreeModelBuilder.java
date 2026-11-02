package org.etlforge.ui.xml;

import org.etlforge.ui.widget.tree.DefaultKongaTreeModel;
import org.etlforge.ui.widget.tree.KongaTreeModel;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public final class XmlTreeModelBuilder {
   public static KongaTreeModel createModel(Element var0) {
      XmlTreeNode var1 = createTreeNode(var0, null);
      return new DefaultKongaTreeModel(var1);
   }

   private static XmlTreeNode createTreeNode(Node var0, XmlTreeNode var1) {
      XmlTreeNode var2 = null;
      switch (var0.getNodeType()) {
         case 1:
            var2 = createElementNode(var0);
            break;
         case 2:
            var2 = new AttributeTreeNode((Attr)var0);
            break;
         case 3:
            var2 = createTextNode(var0, var2);
      }

      if (var2 != null) {
         if (var1 != null) {
            var1.add(var2);
         }

         NodeList var3 = var0.getChildNodes();
         int var4 = 0;

         for (int var5 = var3.getLength(); var4 < var5; var4++) {
            createTreeNode(var3.item(var4), var2);
         }
      }

      return var2;
   }

   private static XmlTreeNode createElementNode(Node var0) {
      Element var2 = (Element)var0;
      ElementTreeNode var1 = new ElementTreeNode(var2);
      NamedNodeMap var3 = var2.getAttributes();
      int var4 = 0;

      for (int var5 = var3.getLength(); var4 < var5; var4++) {
         Attr var6 = (Attr)var3.item(var4);
         var1.add(new AttributeTreeNode(var6));
      }

      return var1;
   }

   private static XmlTreeNode createTextNode(Node var0, XmlTreeNode var1) {
      String var2 = var0.getNodeValue().trim();
      if (!var2.equals("") && !var2.equals("\n") && !var2.equals("\r\n")) {
         var1 = new ValueTreeNode(var0.getNodeValue().trim());
      }

      return var1;
   }

   private XmlTreeModelBuilder() {
   }
}
