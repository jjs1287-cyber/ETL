package org.etlforge.integration.client.ui.interchange.location.http;

import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.entity.wizard.TreeWidget;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.functors.FileBasedLocationFilter;
import org.etlforge.integration.data.location.HttpLocation;
import org.etlforge.ui.input.InputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.TwoColumnLayout;

public final class ResponseTargetComboBoxSelector implements ResponseTargetSelector {
   private final TreeWidget<Target> selector;

   public ResponseTargetComboBoxSelector(EntityExplorerSupport var1) {
      this.selector = new TreeWidget<>(var1, EntityType.Target, Target.class, new FileBasedLocationFilter());
      this.selector.setIncludeNoneItem(true);
   }

   @Override
   public Target getSelectedTarget() {
      return this.selector.getSelectedEntity();
   }

   @Override
   public void layout(String var1, TwoColumnLayout var2) {
      this.selector.setSelectorLabel(var1);
      this.selector.layoutWithLabelInTwoColumns(var2);
   }

   @Override
   public void reset(HttpLocation var1) {
      this.selector.setFilter(new ResponseTargetFilter(var1));
      this.selector.setSelectedEntity(var1.getTargetIdForResponse());
   }

   @Override
   public void register(InputPanel var1) {
      InputField var2 = this.selector.asInputField("Response Target");
      var1.addInputField(var2);
   }

   @Override
   public void dispose() {
      this.selector.dispose();
   }
}
