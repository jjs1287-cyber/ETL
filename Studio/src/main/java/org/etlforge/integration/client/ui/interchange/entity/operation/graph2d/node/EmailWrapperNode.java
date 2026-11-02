package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node;

import org.etlforge.integration.data.entity.id.EmailMessageId;
import org.etlforge.integration.data.entity.operation.flow.EmailWrapper;
import org.etlforge.ui.graph.node.AbstractGraphNode;

public final class EmailWrapperNode extends AbstractGraphNode {
   public EmailWrapperNode(EmailWrapper var1) {
      super(var1, null);
   }

   public EmailMessageId getEmailId() {
      return this.getDataObject().getActivity().getID();
   }

   public EmailWrapper getDataObject() {
      return (EmailWrapper)super.getDataObject();
   }
}
