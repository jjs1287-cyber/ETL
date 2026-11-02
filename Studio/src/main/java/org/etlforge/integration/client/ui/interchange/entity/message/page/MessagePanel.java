package org.etlforge.integration.client.ui.interchange.entity.message.page;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPanel;
import org.etlforge.integration.data.binding.BoundProperty;
import org.etlforge.integration.data.entity.Message;
import org.etlforge.ui.util.UiUtils;

public class MessagePanel extends IntegrationEntityPanel {
   private final MessageDetailsPanel detailsPanel;

   public MessagePanel(ApplicationWindow var1, Message var2) {
      super(var1, var2);
      this.detailsPanel = new MessageDetailsPanel(var2);
      this.layoutComponents();
      this.addInputPanel(this.detailsPanel);
      UiUtils.setOpaqueRecursivelySkipDefaults(this.getUi(), false);
   }

   BoundProperty[] getBoundPropertyControls() {
      ArrayList var1 = Lists.newArrayList();

      for (BoundProperty var5 : this.detailsPanel.getBoundPropertyControls()) {
         var1.add(var5);
      }

      return (BoundProperty[]) var1.toArray(new BoundProperty[var1.size()]);
   }

   private void layoutComponents() {
      this.getDefaultLayout().add(this.detailsPanel, true);
   }

   public Message getDisplayedObject() {
      return (Message)super.getDisplayedObject();
   }

   @Override
   protected void applyOtherPropertiesThanTheName() {
      this.detailsPanel.applyTo(this.getDisplayedObject());
   }
}
