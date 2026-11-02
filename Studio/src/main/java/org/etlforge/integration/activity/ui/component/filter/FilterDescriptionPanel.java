package org.etlforge.integration.activity.ui.component.filter;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.widget.label.KongaLabel;

public final class FilterDescriptionPanel implements FilterListener, UiProvider {
   private final JComponent row;
   private final JLabel filterString;

   public FilterDescriptionPanel(ActivityEntryFilterUi<?> var1) {
      var1.addFilterListener(this);
      this.row = Box.createHorizontalBox();
      KongaLabel var2 = TextStyles.AdminHint.makeLabel(PackageResources.FilterDescriptionPanel.LABEL);
      this.row.add(var2);
      this.filterString = new JLabel(var1.getFilterString());
      this.row.add(this.filterString);
      this.row.add(Box.createHorizontalGlue());
      this.row.setBorder(BorderFactory.createEtchedBorder());
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.row);
   }

   @Override
   public void requestFocus() {
   }

   @Override
   public void filterChanged(ActivityEntryFilterUi<?> var1) {
      this.filterString.setText(var1.getFilterString());
   }
}
