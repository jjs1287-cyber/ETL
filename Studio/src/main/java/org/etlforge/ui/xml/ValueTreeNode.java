package org.etlforge.ui.xml;

public final class ValueTreeNode extends XmlTreeNode {
   public ValueTreeNode(String var1) {
      super(var1, false);
   }

   @Override
   public String getDisplayString() {
      return "[V] " + this.getValue();
   }

   public String getValue() {
      return (String)this.getUserObject();
   }
}
