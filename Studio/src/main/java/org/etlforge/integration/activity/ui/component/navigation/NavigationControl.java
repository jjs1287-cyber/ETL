package org.etlforge.integration.activity.ui.component.navigation;

import com.google.common.collect.Maps;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.TreeMap;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.etlforge.application.ui.action.ActionDecorators;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.activity.ActivitySubject;
import org.etlforge.ui.Empty;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.label.KongaLabel;

public abstract class NavigationControl {
   public static final int DEFAULT_CONTROL_GAP = 10;
   private final TreeMap<Integer, JComponent> controls = Maps.newTreeMap();
   private boolean isEnabled = true;
   private int controlGap = 10;
   private final BoxBuilder displayer;
   private final NavigationControl.RefreshAction refreshAction = new NavigationControl.RefreshAction();
   private final JLabel subTitleLabel;
   private Icon subjectIcon;

   public NavigationControl() {
      this.addSpace(25, Integer.MIN_VALUE);
      this.addControl(new KongaButton(this.refreshAction), 0);
      this.displayer = this.createDisplayerComponent();
      this.subTitleLabel = new JLabel(" ");
      this.layoutTitle(this.displayer);
   }

   public final void setSubjectIcon(Icon var1) {
      this.subjectIcon = var1;
      this.subTitleLabel.setIcon(var1);
   }

   public final void addSpace(int var1, int var2) {
      if (var1 > 0) {
         this.controls.put(var2, (JComponent)Box.createHorizontalStrut(var1));
      }
   }

   public final Action getRefreshAction() {
      return this.refreshAction;
   }

   private BoxBuilder createDisplayerComponent() {
      return Horizontal.box().withBorder(Empty.border(0, 5, 0, 2));
   }

   private void layoutTitle(BoxBuilder var1) {
      KongaLabel var2 = TextStyles.GenericEditorTitleMain.makeLabel(this.getTitle());
      var1.addAllWithSpace(5, var2, this.subTitleLabel);
   }

   public final void setCloseAction(Action var1) {
      JButton var2 = createToolBarButton(var1);
      this.addControl(var2, Integer.MAX_VALUE);
   }

   public abstract void onRecordsRequested();

   public abstract String getTitle();

   public final void setDisplayedSubject(ActivitySubject var1) {
      if (var1 == null) {
         this.subTitleLabel.setIcon(null);
         this.subTitleLabel.setText(" ");
      } else {
         this.subTitleLabel.setIcon(this.subjectIcon);
         this.subTitleLabel.setText(var1.getName());
      }
   }

   public void setEnabled(boolean var1) {
      if (var1 != this.isEnabled) {
         this.refreshAction.setEnabled(var1);
         this.isEnabled = var1;
      }
   }

   public final boolean isEnabled() {
      return this.isEnabled;
   }

   public final void setControlGap(int var1) {
      this.controlGap = Math.max(var1, 0);
   }

   public final void addControl(JComponent var1, int var2) {
      if (var1 != null) {
         this.controls.put(var2, var1);
      }
   }

   public final void addControl(Action var1, int var2) {
      this.addControl(new KongaButton(var1), var2);
   }

   public final JComponent getDisplayer() {
      this.layoutControls(this.displayer);
      UiUtils.setPanelBackgrounds(this.displayer.container(), Colors.LIGHT_BLUE_BACKGROUND);
      return this.displayer.container();
   }

   private void layoutControls(BoxBuilder var1) {
      var1.glue();
      var1.addAllWithSpace(this.controlGap, this.controls.values());
   }

   protected static final JButton createToolBarButton(Action var0) {
      return KongaButton.forToolbar(var0);
   }

   private class RefreshAction extends AbstractAction {
      public RefreshAction() {
         ActionDecorators.REFRESH.decorate(this);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               NavigationControl.this.onRecordsRequested();
            }
         });
      }
   }
}
