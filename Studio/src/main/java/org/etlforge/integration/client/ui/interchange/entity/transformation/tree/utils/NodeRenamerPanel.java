package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils;

import org.apache.commons.lang.StringUtils;
import org.etlforge.application.ui.widget.InputFieldFactory;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.layout.OneColumnPanel;

class NodeRenamerPanel extends InputPanel {
   private final TextInputField2 field;

   public NodeRenamerPanel(TreeComponent var1, Node var2) {
      this.field = createTextField(getOriginalName(var1, var2));
      this.addInputField(this.field, this.field, this.field);
      this.layoutMyself();
      this.setSendUndoEvents(true);
      this.setSendInputChangeEvents(true);
   }

   public String getNewName() {
      return this.field.getValue().trim();
   }

   public void requestFocus() {
      this.field.requestFocus();
   }

   private void layoutMyself() {
      OneColumnPanel var1 = new OneColumnPanel();
      var1.withInsets(0, 0, 3, 0);
      var1.addAll(this.field.getLabel(), this.field.getInputComponent());
      this.wrap(var1);
   }

   private static TextInputField2 createTextField(String var0) {
      String var1 = "Enter the new name:";
      TextInputField2 var2 = InputFieldFactory.getNonEmptyTextInputField(var1, var1, 30, false);
      var2.setSelectAllWhenFocused(true);
      var2.setValue(var0);
      return var2;
   }

   private static String getOriginalName(TreeComponent var0, Node var1) {
      String var2 = NodeNameUtils.getUserDefinedName(var0, var1);
      if (var2 != null) {
         return var2;
      }

      String var3 = var1.getDisplay();
      CROM var4 = var1.getCROM();
      String var5 = var4.getNodeTypeMarker();
      return StringUtils.substringAfter(var3, var5).trim();
   }
}
