package org.etlforge.ui.xml;

import org.w3c.dom.Attr;

public final class AttributeTreeNode extends XmlTreeNode {
   public AttributeTreeNode(Attr var1) {
      super(var1, true);
   }

   @Override
   public String getDisplayString() {
      StringBuffer var1 = new StringBuffer("[A] ");
      var1.append(this.getXmlAttribute().getName());
      var1.append(" = \"");
      var1.append(this.getXmlAttribute().getNodeValue());
      var1.append("\"");
      return var1.toString();
   }

   public Attr getXmlAttribute() {
      return (Attr)this.getUserObject();
   }
}
