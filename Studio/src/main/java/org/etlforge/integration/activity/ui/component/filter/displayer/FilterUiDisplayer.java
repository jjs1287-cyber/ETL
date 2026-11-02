package org.etlforge.integration.activity.ui.component.filter.displayer;

import java.awt.Point;
import java.awt.Window;
import javax.swing.JComponent;
import org.etlforge.application.ui.widget.AnimationPreference;
import org.etlforge.integration.activity.client.ActivityEntry;
import org.etlforge.integration.activity.ui.component.filter.ActivityEntryFilterUi;
import org.etlforge.integration.activity.ui.component.filter.FilterListener;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.overlay.OverlayCenterPositioner;
import org.etlforge.ui.widget.overlay.OverlayContainer;
import org.etlforge.ui.widget.overlay.OverlayDecorator;
import org.etlforge.ui.widget.overlay.OverlayPositioner;
import org.etlforge.ui.widget.overlay.OverlayRelativePositioner;
import org.etlforge.ui.widget.window.WindowUtils;

public final class FilterUiDisplayer<T extends ActivityEntry> {
   private final ActivityEntryFilterUi<T> filter;
   private final FilterListener listener;
   private final String title;

   public FilterUiDisplayer(ActivityEntryFilterUi<T> var1, FilterListener var2, String var3) {
      this.filter = var1;
      this.listener = var2;
      this.title = var3;
   }

   public void showInDialog(Window var1, Point var2) {
      FilterUiDisplayer.DialogDisplayer var3 = new FilterUiDisplayer.DialogDisplayer(var1, var2);
      var3.display(this.title, this.filter, this.listener);
   }

   public void showInOverlay(OverlayContainer var1, JComponent var2) {
      FilterUiDisplayer.OverlayDisplayer var3 = new FilterUiDisplayer.OverlayDisplayer(var1, var2);
      var3.display(this.title, this.filter, this.listener);
   }

   private static final class DialogDisplayer<E extends ActivityEntry> implements FilterUiDisplayer.Displayer<E> {
      private final Window owner;
      private final Point locationOnScreen;

      public DialogDisplayer(Window var1, Point var2) {
         this.owner = var1 == null ? UiUtils.getActiveFrame() : var1;
         this.locationOnScreen = var2;
      }

      @Override
      public void display(String var1, ActivityEntryFilterUi<E> var2, FilterListener var3) {
         String var4 = var2.getFilterString();
         KongaDialog var5 = this.createDialog(var2, var1);
         var5.setVisible(true);
         if (var5.wasOkPressed()) {
            var2.apply();
            this.notifyFilterListeners(var2, var3, var4);
         }
      }

      private void notifyFilterListeners(ActivityEntryFilterUi<E> var1, FilterListener var2, String var3) {
         String var4 = var1.getFilterString();
         if (var2 != null && !var4.equals(var3)) {
            var2.filterChanged(var1);
         }
      }

      private KongaDialog createDialog(final ActivityEntryFilterUi<E> var1, String var2) {
         KongaDialog var3 = new KongaDialog(this.owner, var2) {
            @Override
            protected void onDialogOpened() {
               var1.requestFocus();
            }
         };
         this.layoutDialog(var3, var1);
         if (this.locationOnScreen != null) {
            var3.setLocation(this.locationOnScreen);
            WindowUtils.ensureWindowCompletelyVisibleOnScreen(var3);
         }

         return var3;
      }

      private void layoutDialog(KongaDialog var1, ActivityEntryFilterUi<E> var2) {
         var1.standardLayout(var2, KongaDialog.OK_CANCEL);
         var1.setOkButtonAsDefault();
         var1.setResizable(false);
      }
   }

   private interface Displayer<E extends ActivityEntry> {
      void display(String var1, ActivityEntryFilterUi<E> var2, FilterListener var3);
   }

   private final class OverlayDisplayer<E extends ActivityEntry> implements FilterUiDisplayer.Displayer<E> {
      private final OverlayContainer container;
      private final JComponent trigger;

      public OverlayDisplayer(OverlayContainer var2, JComponent var3) {
         this.container = var2;
         this.trigger = var3;
      }

      @Override
      public void display(String var1, final ActivityEntryFilterUi<E> var2, final FilterListener var3) {
         final String var4 = var2.getFilterString();
         BorderLayoutBuilder var5 = new BorderLayoutBuilder(var2).withEmptyBorder(10);
         OverlayDecorator var6 = new OverlayDecorator(var5, var1) {
            @Override
            protected void onOk() {
               var2.apply();
               OverlayDisplayer.this.notifyListeners(var2, var3, var4);
            }
         };
         var6.setFadeIn(AnimationPreference.get());
         var6.setBlocking(true);
         var6.useOkAndCancel();
         var6.setAlpha(0.8F);
         var6.show(this.container, this.getPositioner());
         var2.requestFocus();
      }

      private void notifyListeners(ActivityEntryFilterUi<E> var1, FilterListener var2, String var3) {
         String var4 = var1.getFilterString();
         if (var2 != null && !var4.equals(var3)) {
            var2.filterChanged(var1);
         }
      }

      private OverlayPositioner getPositioner() {
         return this.trigger != null ? new OverlayRelativePositioner(this.trigger, new Point(8, 8)) : new OverlayCenterPositioner();
      }
   }
}
