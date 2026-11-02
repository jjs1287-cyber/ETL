package org.etlforge.integration.client.ui.interchange.entity.message;

import java.util.Arrays;
import java.util.List;
import org.etlforge.integration.client.ui.entity.search.SearchValuesCollector;
import org.etlforge.integration.data.entity.EmailMessage;
import org.etlforge.integration.data.entity.IntegrationEntity;

final class EmailSearchValuesCollector implements SearchValuesCollector {
   @Override
   public List<String> getValues(IntegrationEntity var1) {
      EmailMessage var2 = (EmailMessage)var1;
      return Arrays.asList(
         var2.getSmtpServers(),
         var2.getSendTo(),
         var2.getSentFrom(),
         var2.getAccountName(),
         var2.getCC(),
         var2.getBCC(),
         var2.getReplyTo(),
         var2.getSubject(),
         var2.getText()
      );
   }
}
