package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node;

import java.awt.Dimension;
import javax.swing.JTextField;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.ui.graph2d.node.AbstractIconLabelNodeUi2D;

public class NameEditor extends EntityNodeNameEditor {
   private final AbstractIconLabelNodeUi2D nodeUi;

   public NameEditor(AbstractIconLabelNodeUi2D var1, IntegrationEntity var2) {
      super(var1.getNodeObject(), var2);
      this.nodeUi = var1;
      this.setOffset(var1.getContentLabelOffset());
   }

   @Override
   protected void prepareField(JTextField var1) {
      super.prepareField(var1);
      Dimension var2 = this.nodeUi.getContentLabel().getSize();
      Dimension var3 = var1.getPreferredSize();
      var3.width = Math.max(var2.width, 75);
      var3.height = Math.max(var2.height, var3.height);
      var1.setSize(var3);
   }
}
