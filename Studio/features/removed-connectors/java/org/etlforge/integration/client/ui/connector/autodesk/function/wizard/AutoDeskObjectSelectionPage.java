package org.etlforge.integration.client.ui.connector.autodesk.function.wizard;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import java.util.LinkedHashMap;
import javax.swing.Action;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.integration.client.ui.connector.wizard.ObjectSelectionController;
import org.etlforge.integration.client.ui.connector.wizard.ObjectSelectionModel;
import org.etlforge.integration.client.ui.connector.wizard.ObjectSelectionPage;
import org.etlforge.integration.etlforgeconnector.ConnectorObject;
import org.etlforge.ui.util.KeyValueToolTip;
import org.etlforge.util.string.StringLimiter;

public final class AutoDeskObjectSelectionPage extends ObjectSelectionPage {
   private volatile ConnectorObject selectedObject;

   public AutoDeskObjectSelectionPage(Wizard var1, ObjectSelectionModel var2, ObjectSelectionController var3, Action var4) {
      super(var1, "Select a Workspace", var2, var3, var4);
      this.setToolTipProducer(new AutoDeskObjectSelectionPage.ToolTipProducer());
   }

   public ConnectorObject getSelectedObject() {
      return this.selectedObject;
   }

   @Override
   protected String getForwardNavigationMessage() {
      return "Retrieving object metadata";
   }

   private static class ToolTipProducer implements Function<ConnectorObject, String> {
      private ToolTipProducer() {
      }

      public String apply(ConnectorObject var1) {
         LinkedHashMap var2 = Maps.newLinkedHashMap();
         var2.put("Name:", StringLimiter.limit(var1.getName(), 150));
         var2.put("Description:", StringLimiter.limit(var1.getDescription(), 150));
         return KeyValueToolTip.createTable(var2);
      }
   }
}
