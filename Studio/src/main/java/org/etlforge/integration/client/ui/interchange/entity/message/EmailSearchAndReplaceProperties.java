package org.etlforge.integration.client.ui.interchange.entity.message;

import org.etlforge.integration.client.entity.search.ReplaceableKeyValueBooleanProperty;
import org.etlforge.integration.client.entity.search.ReplaceableKeyValueStringProperty;
import org.etlforge.integration.client.entity.search.SearchAndReplaceMetaData;
import org.etlforge.integration.client.entity.search.SearchableProperty;
import org.etlforge.integration.client.entity.search.TargetFunctions;

final class EmailSearchAndReplaceProperties extends SearchAndReplaceMetaData {
   public EmailSearchAndReplaceProperties() {
      super(
         stringProperty("Mail server", "smtp_servers"),
         stringProperty("From", "sent_from"),
         stringProperty("To", "send_to"),
         stringProperty("Subject", "subject"),
         stringProperty("Text", "message_text"),
         stringProperty("CC", "cc"),
         stringProperty("BCC", "bcc"),
         stringProperty("Reply-To", "reply_to"),
         stringProperty("Account", "account"),
         booleanProperty("Use SSL/TLS", "use_ssl")
      );
   }

   private static SearchableProperty<String> stringProperty(String var0, String var1) {
      return new ReplaceableKeyValueStringProperty(var0, var1);
   }

   private static SearchableProperty<Boolean> booleanProperty(String var0, String var1) {
      return new ReplaceableKeyValueBooleanProperty(var0, var1, false, TargetFunctions.IDENTITY);
   }
}
