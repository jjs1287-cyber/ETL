package org.etlforge.integration.client.ui.structure.editor.db.rels;

import javax.swing.JLabel;
import org.etlforge.integration.data.structure.database.RelationshipCardinality;
import org.etlforge.integration.data.structure.database.TableRelationship;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.RadioButtonGroupPanel;

final class JoinTypeSelector implements UiProvider {
   private final RadioButtonGroupPanel<RelationshipCardinality> panel = new RadioButtonGroupPanel<>();
   private final UiProvider ui;

   public JoinTypeSelector(TableRelationship var1) {
      for (RelationshipCardinality var5 : RelationshipCardinality.values()) {
         this.panel.addOption(var5, getLabel(var5), var5 == var1.getRelationshipNature());
      }

      JLabel var6 = new JLabel(
         "<html>Identify whether the child table's data must be present (\"1 to N\" or \"1\")<br>or may be present (\"0 to N\" or \"0 or 1\")"
      );
      this.ui = this.panel.getUi(var6, RadioButtonGroupPanel.Layout.TWO_ROWS);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.ui.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.ui.requestFocus();
   }

   public RelationshipCardinality getSelection() {
      return this.panel.getSelection();
   }

   private static String getLabel(RelationshipCardinality var0) {
      switch (var0) {
         case ONE_TO_N:
            return "1 to N";
         case ONE_TO_ONE:
            return "1";
         case ZERO_TO_N:
            return "0 to N";
         case ZERO_OR_ONE:
            return "0 or 1";
         default:
            throw new RuntimeException("Unexpected join type: " + var0);
      }
   }
}
