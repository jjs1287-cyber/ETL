package org.etlforge.integration.client.ui.connector.mscrm.query.builder;

import javax.swing.JLabel;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiProviderAdapter;
import org.etlforge.ui.input.RadioButtonGroupPanel;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.property.Bindings;
import org.etlforge.ui.property.BooleanBinding;
import org.etlforge.ui.property.TextBinding;
import org.etlforge.ui.undo.UndoRedoControllerBase;
import org.etlforge.ui.widget.button.KongaCheckBox;
import org.etlforge.ui.widget.text.KongaTextField;
import org.etlforge.ui.widget.text.undo.UndoableTextEditSender;

public final class LinkEntityBuilderUi implements UiProvider {
   private final LinkEntityModel model;
   private final QueryBuilderUi basicUi;
   private final RadioButtonGroupPanel<LinkType> linkTypeSelector = RadioButtonGroupPanel.forEnum(LinkType.class);
   private final KongaTextField aliasField = new KongaTextField();
   private final KongaCheckBox visibleCheckBox = new KongaCheckBox();

   public LinkEntityBuilderUi(LinkEntityModel var1, QueryBuilderController var2) {
      this.model = var1;
      this.aliasField.setLabel("Alias:");
      this.basicUi = new QueryBuilderUi(var1, var2);
      QueryBuilderUi.Box var3 = this.createControlBox();
      this.basicUi.addBox(var3);
      this.bindWithModel(var1);
   }

   private QueryBuilderUi.Box createControlBox() {
      TwoColumnPanel var1 = TwoColumnPanel.forFormEntry();
      var1.addRow(UiProviderAdapter.label("Link Type:"), this.linkTypeSelector.oneRowLayout());
      this.aliasField.layoutWithLabel(var1);
      var1.addRow(new JLabel("Visible:"), this.visibleCheckBox);
      return new QueryBuilderUi.Box(Vertical.Anchor.north(var1), "Link Properties");
   }

   private void bindWithModel(LinkEntityModel var1) {
      Bindings var2 = Bindings.of(
         this.linkTypeSelector.bindWith(var1.getProperty("link_type")),
         TextBinding.bind(var1, "alias", this.aliasField),
         BooleanBinding.bind(var1, "visible", this.visibleCheckBox)
      );
      var2.syncUi();
   }

   public LinkEntityModel getModel() {
      return this.model;
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.basicUi.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.basicUi.requestFocus();
   }

   public void contributeToUndoRedo(UndoRedoControllerBase var1) {
      var1.addEditSender(new UndoableTextEditSender(this.aliasField));
   }
}
