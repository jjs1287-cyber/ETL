package org.etlforge.integration.client.ui.structure.editor.db.rels;

import javax.swing.JComponent;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.application.ui.wizard.NumberedSectionsLayout;
import org.etlforge.integration.data.structure.database.TableRelationship;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;

final class JoinPropertiesUi implements UiProvider {
   private final TableRelationship relation;
   private final LinkKeysDefiner linkKeysDefiner;
   private final JoinTypeSelector joinTypeSelector;

   public JoinPropertiesUi(TableRelationship var1) {
      this.relation = var1;
      this.linkKeysDefiner = new ListLinkKeysDefiner(var1);
      this.joinTypeSelector = new JoinTypeSelector(var1);
   }

   public boolean checkInput() {
      return this.linkKeysDefiner.checkInput();
   }

   public void apply() {
      this.linkKeysDefiner.apply();
      this.relation.setRelationshipNature(this.joinTypeSelector.getSelection());
   }

   @Override
   public void decorate(UiCanvas var1) {
      NumberedSectionsLayout var2 = new NumberedSectionsLayout();
      var2.setVerticalGap(16);
      var2.setWeights(new double[]{1.0, 1.0}, new double[]{1.0, 0.0});
      var2.addSection("Link Keys", this.wrapComponent(this.linkKeysDefiner));
      var2.addSection("Join Type", this.wrapComponent(this.joinTypeSelector));
      var2.decorate(var1);
   }

   private JComponent wrapComponent(UiProvider var1) {
      JPanel var2 = BorderLayoutBuilder.fillWith(var1);
      var2.setBorder(StyleSheet.getDefaultTitleBorder(null));
      return var2;
   }

   @Override
   public void requestFocus() {
      this.linkKeysDefiner.requestFocus();
   }
}
