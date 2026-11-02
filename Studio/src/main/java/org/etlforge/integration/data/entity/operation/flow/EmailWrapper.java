package org.etlforge.integration.data.entity.operation.flow;

import org.etlforge.integration.data.entity.EmailMessage;

public final class EmailWrapper extends OperationActivityWrapper {
   EmailWrapper(EmailMessage var1) {
      super(var1);
   }

   public EmailMessage getActivity() {
      return (EmailMessage)super.getActivity();
   }

   @Override
   public void dispose() {
   }
}
