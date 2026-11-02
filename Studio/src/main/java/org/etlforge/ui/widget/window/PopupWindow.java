package org.etlforge.ui.widget.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import org.etlforge.system.Disposable;
import org.etlforge.ui.Empty;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiProviderAdapter;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.resources.UiResourceBundle;
import org.etlforge.ui.util.focus.FocusUtils;
import org.etlforge.ui.widget.Label;
import org.etlforge.ui.widget.LabelDecorator;
import org.etlforge.ui.widget.button.ButtonUtils;

public final class PopupWindow implements Disposable {
   private final Component owner;
   private final Component content;
   private Point locationOnScreen;
   private JWindow window;
   private final WindowSizeHandler sizeHandler;
   private boolean resizable;
   private WindowResizer resizer;
   private boolean focusable;
   private boolean giveFocusToOwnerWhenClosed;
   private WindowEscapeCloser escapeCloser;
   private boolean consumeEscapeWhileVisible;
   private PopupWindow.TitlePanel titlePanel;

   private PopupWindow(Component var1, Component var2, Point var3) {
      this.owner = var1;
      this.content = var2;
      this.locationOnScreen = var3;
      this.sizeHandler = new WindowSizeHandler();
      this.focusable = true;
      this.giveFocusToOwnerWhenClosed = true;
      this.consumeEscapeWhileVisible = true;
   }

   public static PopupWindow newWindow(Component var0, Component var1, Point var2) {
      return new PopupWindow(var0, var1, var2);
   }

   public void show() {
      if (this.window == null) {
         this.window = this.createWindow();
         this.escapeCloser = WindowEscapeCloser.bind(this.window);
         this.escapeCloser.setConsumeEscapeWhileVisible(this.consumeEscapeWhileVisible);
         new WindowMouseCloser(this.window, 10, 10);
         this.showImpl(this.window);
      }
   }

   public void show(Point var1) {
      this.locationOnScreen = var1 != null ? var1 : new Point();
      this.show();
   }

   private void showImpl(JWindow var1) {
      var1.setLocation(this.locationOnScreen);
      WindowUtils.ensureWindowCompletelyVisibleOnScreen(var1);
      var1.setVisible(true);
   }

   @Override
   public void dispose() {
      if (this.window != null) {
         this.window.dispose();
         if (this.resizer != null) {
            this.resizer.dispose();
         }

         this.window = null;
      }
   }

   public boolean isVisible() {
      return this.window != null && this.window.isShowing();
   }

   public void setPreferredSize(Dimension var1) {
      this.sizeHandler.preferredSize = var1;
   }

   public void setMaximumSize(Dimension var1) {
      this.sizeHandler.maximumSize = var1;
   }

   public void setMinimumSize(Dimension var1) {
      this.sizeHandler.minimumSize = var1;
   }

   public void setResizable(boolean var1) {
      this.resizable = var1;
   }

   public void setFocusable(boolean var1) {
      this.focusable = var1;
   }

   public void setGiveFocusToOwnerWhenClosed(boolean var1) {
      this.giveFocusToOwnerWhenClosed = var1;
      if (this.escapeCloser != null) {
         this.escapeCloser.setConsumeEscapeWhileVisible(var1);
      }
   }

   public void setConsumeEscapeWhileVisible(boolean var1) {
      this.consumeEscapeWhileVisible = var1;
   }

   public void setTitle(LabelDecorator var1, boolean var2) {
      this.titlePanel = new PopupWindow.TitlePanel(var2);
      var1.decorate(this.titlePanel);
   }

   public boolean contains(Point var1) {
      return this.contains(var1, 0, 0);
   }

   public boolean contains(Point var1, int var2, int var3) {
      return this.window != null && WindowUtils.contains(this.window, var1, var2, var3);
   }

   private JWindow createWindow() {
      JWindow var1 = new JWindow(SwingUtilities.getWindowAncestor(this.owner));
      var1.setLocationByPlatform(this.locationOnScreen == null);
      this.createContentPane(var1);
      this.sizeHandler.setPreferredSize(var1);
      this.installWindowListener(var1);
      this.installResizer(var1);
      var1.setFocusableWindowState(this.focusable);
      var1.pack();
      this.sizeHandler.enforceLimits(var1);
      return var1;
   }

   private void installWindowListener(JWindow var1) {
      var1.addWindowListener(new PopupWindow.WindowListenerImpl());
   }

   private void installResizer(JWindow var1) {
      if (this.resizable) {
         this.resizer = new WindowResizer(var1);
         this.resizer.install();
      }
   }

   private void createContentPane(JWindow var1) {
      JPanel var2 = new JPanel(new BorderLayout(5, 0));
      var2.setBorder(BorderFactory.createBevelBorder(0));
      if (this.titlePanel != null) {
         this.titlePanel.addToParent(var2, "North");
      }

      var2.add(this.content, "Center");
      var1.setContentPane(var2);
   }

   private final class TitlePanel implements Label {
      private final JLabel label = this.createLabel();
      private final JComponent background = this.createBackground(this.label);

      public TitlePanel(boolean var2) {
         if (var2) {
            this.addCloseButton();
         }
      }

      private JLabel createLabel() {
         JLabel var1 = new JLabel();
         var1.setBorder(Empty.border(2, 5, 2, 0));
         return var1;
      }

      private JComponent createBackground(JLabel var1) {
         return new BorderLayoutBuilder(var1).withBorder(BorderFactory.createLineBorder(Color.DARK_GRAY)).container();
      }

      private void addCloseButton() {
         JButton var1 = new JButton(UiResourceBundle.getCloseIcon16());
         var1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent var1) {
               PopupWindow.this.dispose();
            }
         });
         ButtonUtils.configureButtonForToolBar(var1);
         this.background.add(var1, "East");
      }

      public void addToParent(Container var1, Object var2) {
         var1.add(this.background, var2);
      }

      @Override
      public void setBackgroundColor(Color var1) {
         this.background.setBackground(var1);
      }

      @Override
      public void setFont(Font var1) {
         this.label.setFont(var1);
      }

      @Override
      public void setIcon(Icon var1) {
         this.label.setIcon(var1);
      }

      @Override
      public void setText(String var1) {
         this.label.setText(var1);
      }

      @Override
      public void setText(MnemonicString var1) {
         this.setText(var1.getText());
      }

      @Override
      public void setTextColor(Color var1) {
         this.label.setForeground(var1);
      }

      @Override
      public void setToolTip(String var1) {
         this.label.setToolTipText(var1);
      }

      @Override
      public void setBorder(Border var1) {
         this.label.setBorder(var1);
      }

      @Override
      public UiProvider asUiProvider() {
         return new UiProviderAdapter(this.background);
      }
   }

   private final class WindowListenerImpl extends WindowAdapter {
      private WindowListenerImpl() {
      }

      @Override
      public void windowClosed(WindowEvent var1) {
         PopupWindow.this.dispose();
         if (this.shouldFocusBeTransfered()) {
            FocusUtils.changeFocusAcrossWindows(PopupWindow.this.owner, 100);
         }
      }

      private boolean shouldFocusBeTransfered() {
         return PopupWindow.this.giveFocusToOwnerWhenClosed
            && PopupWindow.this.owner != null
            && !PopupWindow.this.owner.isFocusOwner()
            && PopupWindow.this.owner.isFocusable();
      }
   }
}
