package org.etlforge.ui.widget.overlay;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashSet;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UIManager2;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.event.ActionListeners;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.util.ButtonOrder;
import org.etlforge.ui.util.OkAndCancelShortCutsInstaller;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.panel.KongaPanel;
import org.etlforge.ui.widget.panel.KongaPanelAlphaAnimator;

public class OverlayDecorator {
   private final JComponent content;
   private String title;
   private JLabel titleLabel;
   private TextStyle titleStyle;
   private final OverlayDecorator.Background background;
   private KongaPanelAlphaAnimator fader;
   private boolean fadeIn;
   private boolean fadeOut;
   private final BorderLayoutBuilder layout;
   private boolean layedOut;
   private OverlayContainer container;
   private Action okAction;
   private Action cancelAction;
   private boolean useOkAndCancel;
   private boolean closeOnly;
   private boolean okAsDefault;
   private final LinkedHashSet<Action> actions;
   private OverlayDecorator.ButtonPlacement buttonPlacement;
   private Icon titleIcon;
   private JComponent focusReceiverWhenClosed;
   private final ActionListeners okListeners;

   public OverlayDecorator(UiProvider var1, String var2) {
      this(wrap(var1), var2);
   }

   public OverlayDecorator(JComponent var1, String var2) {
      this.content = var1;
      this.title = var2;
      this.background = new OverlayDecorator.Background();
      this.layout = BorderLayoutBuilder.from(this.background);
      this.layout.container().setFocusable(true);
      this.buttonPlacement = OverlayDecorator.ButtonPlacement.DIALOG;
      this.okAction = new OverlayDecorator.OkAction();
      this.cancelAction = new OverlayDecorator.CancelAction();
      this.actions = Sets.newLinkedHashSet();
      this.okListeners = new ActionListeners();
   }

   public final void show(OverlayContainer var1, OverlayPositioner var2) {
      if (this.container != null) {
         this.container.removeOverlay(this.get());
      }

      this.container = var1;
      var1.addOverlay(this.get(), var2);
      if (this.fadeIn) {
         this.fadeIn();
      }

      this.okAction.setEnabled(true);
      this.cancelAction.setEnabled(true);
      if (this.useOkAndCancel || this.closeOnly) {
         this.installOkAndCancelShortCuts(this.okAction, this.cancelAction);
         UiUtils.makeDefaultButton(this.get(), this.okAsDefault ? this.okAction : this.cancelAction);
      } else if (this.okAsDefault) {
         UiUtils.makeDefaultButton(this.get(), this.okAction);
      }

      if (!this.fadeIn) {
         this.onDisplayed();
      }
   }

   protected void onDisplayed() {
   }

   public final JComponent get() {
      if (!this.layedOut) {
         this.layoutOverlay();
         this.layedOut = true;
      }

      return this.layout.container();
   }

   public final void setOkAction(Action var1) {
      this.okAction = var1;
   }

   public final void setCancelAction(Action var1) {
      this.cancelAction = var1;
   }

   public final void setFocusCycleRoot(boolean var1) {
      this.layout.container().setFocusCycleRoot(true);
   }

   public final void setBlocking(boolean var1) {
      this.layout.container().putClientProperty("konga.overlay.blocking", var1);
      if (var1) {
         this.setFocusCycleRoot(var1);
      }
   }

   public final void setFillHorizontally(boolean var1) {
      this.layout.container().putClientProperty("konga.overlay.fillHorizontally", var1);
   }

   public final void setResizable(boolean var1) {
      this.layout.container().putClientProperty("konga.overlay.resizable", var1);
   }

   public final void setFillVertically(boolean var1) {
      this.layout.container().putClientProperty("konga.overlay.fillVertically", var1);
   }

   public final void setFillOut() {
      this.setFillHorizontally(true);
      this.setFillVertically(true);
   }

   public final void setAlpha(float var1) {
      this.background.finalAlpha = var1;
   }

   public final void setFadeIn(boolean var1) {
      this.fadeIn = var1;
   }

   public final void setFadeOut(boolean var1) {
      this.fadeOut = var1;
   }

   public final void setFocusReceiverWhenClosed(JComponent var1) {
      this.focusReceiverWhenClosed = var1;
   }

   public void installOkAndCancelShortCuts() {
      this.installOkAndCancelShortCuts(this.okAction, this.cancelAction);
   }

   public final void installOkAndCancelShortCuts(Action var1, Action var2) {
      new OkAndCancelShortCutsInstaller(this.layout.container(), 1).install(var1, var2);
   }

   public final void setActions(Action... var1) {
      this.actions.clear();

      for (Action var5 : var1) {
         this.actions.add(var5);
      }
   }

   public final void setTitle(String var1) {
      this.title = var1;
      if (this.titleLabel != null) {
         this.titleLabel.setText(var1);
      }
   }

   public final void setTitleIcon(Icon var1) {
      this.titleIcon = var1;
   }

   public final void setTitleStyle(TextStyle var1) {
      this.titleStyle = var1;
   }

   public final void setButtonPlacement(OverlayDecorator.ButtonPlacement var1) {
      this.buttonPlacement = var1;
   }

   public final void installKeyBinding(KeyStroke var1, Object var2, Action var3) {
      InputMap var4 = this.layout.container().getInputMap(1);
      var4.put(var1, var2);
      ActionMap var5 = this.layout.container().getActionMap();
      var5.put(var2, var3);
   }

   public final Action getOkAction() {
      return this.okAction;
   }

   public final Action getCancelAction() {
      return this.cancelAction;
   }

   public final void useOkAndCancel() {
      this.useOkAndCancel = true;
      this.okAsDefault = true;
      ImmutableList var1 = ButtonOrder.okAndCancelOrder(this.okAction, this.cancelAction);
      this.actions.addAll(var1);
   }

   public final void setOkAsDefault(boolean var1) {
      this.okAsDefault = var1;
   }

   public final void addOkListener(ActionListener var1) {
      this.okListeners.add(var1);
   }

   public final void removeOkListener(ActionListener var1) {
      this.okListeners.remove(var1);
   }

   public final void useCloseOnly() {
      this.closeOnly = true;
      this.useOkAndCancel = false;
      this.okAction.putValue("Name", "Close");
      this.okAsDefault = true;
      this.actions.add(this.okAction);
   }

   public final void hide() {
      if (this.fadeOut) {
         this.fadeOut();
      } else {
         this.hideImpl();
      }
   }

   private void hideImpl() {
      if (this.container != null) {
         this.container.removeOverlay(this.get());
         this.container = null;
         this.onHidden();
         this.restoreFocus();
      }
   }

   protected void onOk() {
   }

   protected void onCancel() {
   }

   protected void onHidden() {
   }

   private void fadeIn() {
      if (this.fader != null) {
         this.fader.stop();
      }

      this.fader = new KongaPanelAlphaAnimator(this.background, new TimingTargetAdapter() {
         @Override
         public void end() {
            if (OverlayDecorator.this.background.finalAlpha < 1.0F) {
               OverlayDecorator.this.background.getParent().repaint();
            }

            OverlayDecorator.this.onDisplayed();
         }
      });
      this.fader.setAcceleration(0.3F);
      this.fader.setDeceleration(0.2F);
      this.fader.show();
   }

   private void fadeOut() {
      if (this.fader != null) {
         this.fader.stop();
      }

      this.fader = new KongaPanelAlphaAnimator(this.background, new TimingTargetAdapter() {
         @Override
         public void end() {
            OverlayDecorator.this.hideImpl();
         }
      });
      this.fader.setAcceleration(0.2F);
      this.fader.setDeceleration(0.3F);
      this.fader.hide();
   }

   private void layoutOverlay() {
      this.layoutCaption();
      BorderLayoutBuilder var1 = new BorderLayoutBuilder();
      var1.center(this.createCenter(this.content));
      this.layoutSouthPart(var1);
      var1.nonOpaque().withBorder(new LineBorder(getInnerBorderColor()));
      this.layout.center(var1);
      this.layout.withBackground(getBackgroundColor()).withBorder(new BorderBuilder().rounded(24, 24, 8, getOuterBorderColor()).empty(10));
   }

   private void layoutCaption() {
      if (this.title != null || this.needsToolbar()) {
         this.titleLabel = this.createCaption(this.title);
         JComponent var1;
         if (this.needsToolbar()) {
            BoxBuilder var2 = BoxBuilder.horizontal();
            var2.add(this.titleLabel).glue().add(this.createToolbar());
            var1 = var2.deeplyNonOpaque().container();
         } else {
            var1 = this.titleLabel;
         }

         var1.setBorder(Empty.border(0, 0, 10, 0));
         this.layout.north(var1);
      }
   }

   private boolean needsToolbar() {
      return this.actions != null && this.buttonPlacement == OverlayDecorator.ButtonPlacement.TOOLBAR;
   }

   private JComponent createToolbar() {
      Grid var1 = Grid.oneRow(10).of(this.actions);
      return new BorderLayoutBuilder().east(var1).nonOpaque().container();
   }

   private JComponent createCenter(JComponent var1) {
      JPanel var2 = BorderLayoutBuilder.fillWith(var1);
      UiUtils.setOpaqueRecursivelySkipDefaults(var2, false);
      var2.setOpaque(true);
      var2.setBackground(getContentBackgroundColor());
      return var2;
   }

   private void layoutSouthPart(BorderLayoutBuilder var1) {
      if (this.actions != null && this.buttonPlacement == OverlayDecorator.ButtonPlacement.DIALOG) {
         Grid var2 = Grid.oneRow(10).of(this.actions).nonOpaque();
         BorderLayoutBuilder var3 = new BorderLayoutBuilder().east(var2).withBorder(Empty.border(10, 0, 10, 10)).withBackground(getContentBackgroundColor());
         var1.south(var3);
      }
   }

   private JLabel createCaption(String var1) {
      if (var1 == null) {
         return null;
      }

      TextStyle var2 = this.getTitleStyle();
      KongaLabel var3 = var2.makeLabel(var1).makeBold();
      var3.setIcon(this.titleIcon);
      var3.setForeground(getForegroundColor());
      return var3;
   }

   private TextStyle getTitleStyle() {
      if (this.titleStyle != null) {
         return this.titleStyle;
      }

      TextStyle var1 = (TextStyle)UIManager.get("konga.overlaydecorator.caption-style");
      if (var1 == null) {
         var1 = new TextStyle(new JLabel());
      }

      return var1;
   }

   private void restoreFocus() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            if (OverlayDecorator.this.focusReceiverWhenClosed != null) {
               OverlayDecorator.this.focusReceiverWhenClosed.requestFocusInWindow();
            }
         }
      });
   }

   private static Color getForegroundColor() {
      return UIManager2.getColor("konga.overlaydecorator.foreground", Color.BLACK);
   }

   private static Color getBackgroundColor() {
      return UIManager2.getColor("konga.overlaydecorator.background", UIManager.getColor("Panel.background"));
   }

   private static Color getContentBackgroundColor() {
      return UIManager2.getColor("konga.overlaydecorator.content.background", Color.WHITE);
   }

   private static Color getOuterBorderColor() {
      return UIManager2.getColor("konga.overlaydecorator.outerborder", Color.BLACK);
   }

   private static Color getInnerBorderColor() {
      return UIManager2.getColor("konga.overlaydecorator.innerborder", Color.GRAY);
   }

   private static JComponent wrap(UiProvider var0) {
      return new BorderLayoutBuilder(var0).container();
   }

   private static class Background extends KongaPanel {
      public float finalAlpha = 1.0F;

      private Background() {
      }

      @Override
      protected void paintComponent(Graphics var1) {
         if (this.finalAlpha < 1.0F) {
            Graphics2D var2 = (Graphics2D)var1;
            Composite var3 = var2.getComposite();
            var2.setComposite(AlphaComposite.getInstance(3, this.finalAlpha));
            super.paintComponent(var1);
            var2.setComposite(var3);
         } else {
            super.paintComponent(var1);
         }
      }
   }

   public enum ButtonPlacement {
      TOOLBAR,
      DIALOG;
   }

   private class CancelAction extends AbstractAction {
      public CancelAction() {
         super("Cancel");
         this.setEnabled(false);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         OverlayDecorator.this.hide();
         OverlayDecorator.this.onCancel();
      }
   }

   private class OkAction extends AbstractAction {
      public OkAction() {
         super("OK");
         this.setEnabled(false);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         OverlayDecorator.this.hide();
         OverlayDecorator.this.onOk();
         OverlayDecorator.this.okListeners.call().actionPerformed(var1);
      }
   }
}
