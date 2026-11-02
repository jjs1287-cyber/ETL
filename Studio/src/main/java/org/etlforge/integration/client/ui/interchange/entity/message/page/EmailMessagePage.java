package org.etlforge.integration.client.ui.interchange.entity.message.page;

import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPageModel;
import org.etlforge.integration.data.entity.EmailMessage;

public final class EmailMessagePage extends IntegrationEntityPage {
   public EmailMessagePage(EmailMessage var1) {
      super(new IntegrationEntityPageModel(var1));
      this.setHelpId("integration.emailmessage.main");
   }

   protected MessagePanel createDataPanel() {
      MessagePanel var1 = new MessagePanel(this.getWindow(), this.getObject());
      this.getPageModel().bindProperties(var1.getBoundPropertyControls());
      return var1;
   }

   public EmailMessage getObject() {
      return (EmailMessage)super.getObject();
   }
}
