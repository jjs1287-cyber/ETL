package org.etlforge.integration.activity.ui.component.filter.displayer;

import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import org.etlforge.integration.activity.client.ActivityEntry;
import org.etlforge.integration.activity.ui.component.filter.ActivityEntryFilterUi;
import org.etlforge.integration.activity.ui.component.filter.FilterListener;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.widget.WidgetUtils;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.overlay.OverlayContainer;
import org.etlforge.ui.widget.panel.InvisiblePanel;

public final class FilterUiDisplayerLauncher<T extends ActivityEntry> {
   private final ActivityEntryFilterUi<T> filter;
   private final FilterListener filterListener;
   private final String title;
   private JButton launchButton;

   public FilterUiDisplayerLauncher(ActivityEntryFilterUi<T> var1, FilterListener var2, String var3) {
      this.filter = var1;
      this.filterListener = var2;
      this.title = var3;
   }

   public UiProvider getUi() {
      final JComponent var1 = this.createUi();
      return new UiProvider() {
         @Override
         public void decorate(UiCanvas var1x) {
            var1x.display(var1);
         }

         @Override
         public void requestFocus() {
            FilterUiDisplayerLauncher.this.launchButton.requestFocusInWindow();
         }
      };
   }

   private JComponent createUi() {
      this.launchButton = this.createLaunchButton();
      return InvisiblePanel.of(this.launchButton);
   }

   private JButton createLaunchButton() {
      KongaButton var1 = new KongaButton(PackageResources.Launcher.LABEL, PackageResources.Launcher.ICON);
      var1.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            FilterUiDisplayerLauncher.this.showDisplayer();
         }
      });
      return var1;
   }

   private void showDisplayer() {
      FilterUiDisplayer var1 = new FilterUiDisplayer<>(this.filter, this.filterListener, this.title);
      OverlayContainer var2 = WidgetUtils.getOverlayContainerAncestor(this.launchButton);
      if (var2 != null) {
         var1.showInOverlay(var2, this.launchButton);
      } else {
         this.openDialog(var1);
      }
   }

   private void openDialog(FilterUiDisplayer<T> var1) {
      Window var2 = SwingUtilities.getWindowAncestor(this.launchButton);
      Point var3 = this.getDialogLocation();
      var1.showInDialog(var2, var3);
   }

   private Point getDialogLocation() {
      Point var1 = this.launchButton.getLocationOnScreen();
      return new Point(var1.x + 5, var1.y + 5);
   }
}
