package org.etlforge.integration.client.ui.interchange.entity.webservice.edit.wsdl;

import java.util.List;
import org.etlforge.application.ui.Alert;
import org.etlforge.integration.client.ui.entity.IntegrationEntityList;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;

class SuccessMessage {
   private final List<WebServiceCall> wsCalls;

   public SuccessMessage(List<WebServiceCall> var1) {
      this.wsCalls = var1;
   }

   public void show() {
      Alert.message(null, this.createLayout(), Strings.get("EditWsdl.Success.Title"), Alert.Type.INFORMATION);
   }

   private UiProvider createLayout() {
      BorderLayoutBuilder var1 = new BorderLayoutBuilder(0, 5);
      var1.northToSouth(Strings.get("EditWsdl.Success.Caption"), this.createList(), Strings.get("EditWsdl.Success.Transformations"));
      return var1;
   }

   private UiProvider createList() {
      return new IntegrationEntityList(this.wsCalls);
   }
}
