package org.etlforge.ui.xml;

import org.w3c.dom.Element;

public final class ElementTreeNode extends XmlTreeNode {
   public ElementTreeNode(Element var1) {
      super(var1, true);
   }

   public Element getXmlElement() {
      return (Element)this.getUserObject();
   }

   @Override
   public String getDisplayString() {
      StringBuffer var1 = new StringBuffer("[E] ");
      var1.append(this.getXmlElement().getNodeName());
      String var2 = this.getXmlElement().getAttribute("name");
      if (var2.length() > 0) {
         var1.append(" (");
         var1.append(var2);
         var1.append(")");
      }

      return var1.toString();
   }
}
