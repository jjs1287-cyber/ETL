package org.etlforge.ui.xml;

import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;

abstract class XmlTreeNode extends DefaultKongaTreeNode {
   public XmlTreeNode(Object var1, boolean var2) {
      super(var1, var2);
   }

   public abstract String getDisplayString();

   public XmlTreeNode getChildAt(int var1) {
      return (XmlTreeNode)super.getChildAt(var1);
   }

   public XmlTreeNode getFirstChild() {
      return (XmlTreeNode)super.getFirstChild();
   }

   public XmlTreeNode getLastChild() {
      return (XmlTreeNode)super.getLastChild();
   }

   public XmlTreeNode getNextSibling() {
      return (XmlTreeNode)super.getNextSibling();
   }

   public XmlTreeNode getParent() {
      return (XmlTreeNode)super.getParent();
   }

   public XmlTreeNode getPreviousSibling() {
      return (XmlTreeNode)super.getPreviousSibling();
   }
}
