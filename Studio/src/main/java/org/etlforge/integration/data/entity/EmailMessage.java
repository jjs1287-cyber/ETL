package org.etlforge.integration.data.entity;

import com.google.common.annotations.VisibleForTesting;
import org.etlforge.integration.data.entity.id.EmailMessageId;
import org.etlforge.integration.data.entity.operation.flow.OperationFlowActivity;

public final class EmailMessage extends Message implements OperationFlowActivity {
   @VisibleForTesting
   public EmailMessage() {
      this("New Email Message");
   }

   public EmailMessage(String var1) {
      this(null, var1);
   }

   public EmailMessage(EmailMessageId var1, String var2) {
      super(EntityType.EmailMessage, var1, var2);
   }

   public EmailMessageId getID() {
      return (EmailMessageId)super.getID();
   }

   @Override
   protected String getNameKey() {
      return "message_name";
   }

   @Override
   protected String getIdKey() {
      return "message_id";
   }
}
