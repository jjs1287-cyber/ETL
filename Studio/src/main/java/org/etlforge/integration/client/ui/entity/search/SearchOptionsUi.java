package org.etlforge.integration.client.ui.entity.search;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.widget.button.ButtonUtils;

final class SearchOptionsUi implements UiProvider {
   private final AdvancedEntitySearchModel model;
   private final JCheckBox caseSensitive;
   private final JCheckBox regex;
   private final JCheckBox emptyStringMatchesAll;
   private final SearchFieldCombineModeUi fieldCombineModeUi;

   public SearchOptionsUi(AdvancedEntitySearchModel var1) {
      this.model = var1;
      this.caseSensitive = new JCheckBox("Case sensitive");
      this.regex = new JCheckBox("Regular expressions");
      this.emptyStringMatchesAll = new JCheckBox("Empty string matches all");
      this.fieldCombineModeUi = new SearchFieldCombineModeUi();
      SearchOptionsUi.Listener var2 = new SearchOptionsUi.Listener();
      ButtonUtils.addActionListener(var2, this.caseSensitive, this.regex, this.emptyStringMatchesAll);
      this.fieldCombineModeUi.addActionListener(var2);
   }

   public SearchOptions getOptions() {
      return new SearchOptions(
         this.caseSensitive.isSelected(), this.regex.isSelected(), this.fieldCombineModeUi.getMode(), this.emptyStringMatchesAll.isSelected()
      );
   }

   public void addSearchAction(ActionListener var1) {
      this.caseSensitive.addActionListener(var1);
      this.regex.addActionListener(var1);
      this.emptyStringMatchesAll.addActionListener(var1);
      this.fieldCombineModeUi.addActionListener(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      TwoColumnPanel var2 = new TwoColumnPanel();
      var2.withLeftInsets(0, 0, 10, 10);
      var2.withRightInsets(0, 0, 10, 0);
      var2.addRow(this.caseSensitive, this.regex);
      var2.addRow(this.emptyStringMatchesAll);
      var2.addRow(this.fieldCombineModeUi);
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.caseSensitive.requestFocusInWindow();
   }

   private class Listener implements ActionListener {
      private Listener() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         SearchOptions var2 = SearchOptionsUi.this.getOptions();
         SearchOptionsUi.this.model.setOptions(var2);
      }
   }
}
