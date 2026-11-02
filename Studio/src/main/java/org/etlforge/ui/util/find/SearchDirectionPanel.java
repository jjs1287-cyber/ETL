package org.etlforge.ui.util.find;

import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.RadioButtonGroupPanel;
import org.etlforge.ui.widget.panel.InvisiblePanel;

public final class SearchDirectionPanel implements UiProvider {
   private final RadioButtonGroupPanel<SearchDirection> panel = new RadioButtonGroupPanel<>();
   private final UiProvider ui;
   private boolean useTitle;

   private SearchDirectionPanel(RadioButtonGroupPanel.Layout var1) {
      this.panel.addOption(SearchDirection.DOWN, SearchDirection.DOWN.toString(), true);
      this.panel.addOption(SearchDirection.UP, SearchDirection.UP.toString(), false);
      this.ui = this.panel.getUi(null, var1);
      this.useTitle = true;
   }

   public static SearchDirectionPanel newHorizontalPanel() {
      return new SearchDirectionPanel(RadioButtonGroupPanel.Layout.ONE_ROW);
   }

   public static SearchDirectionPanel newVerticalPanel() {
      return new SearchDirectionPanel(RadioButtonGroupPanel.Layout.ONE_COLUMN);
   }

   public void setUseTitle(boolean var1) {
      this.useTitle = var1;
   }

   public void shareModel(SearchDirectionPanel var1) {
      this.panel.shareModel(var1.panel);
   }

   @Override
   public void decorate(UiCanvas var1) {
      JPanel var2 = InvisiblePanel.of(this.ui);
      if (this.useTitle) {
         var2.setBorder(new TitledBorder(PackageResources.TextSearch.DIRECTION));
      } else {
         var2.setBorder(Empty.border(5));
      }

      var1.display(var2);
   }

   @Override
   public void requestFocus() {
      this.ui.requestFocus();
   }

   public SearchDirection getSearchDirection() {
      return this.panel.getSelection();
   }

   public void setSearchDirection(SearchDirection var1) {
      this.panel.setSelectedValue(var1);
   }

   public void addActionListener(ActionListener var1) {
      this.panel.addActionListener(var1);
   }

   public void removeActionListener(ActionListener var1) {
      this.panel.removeActionListener(var1);
   }
}
