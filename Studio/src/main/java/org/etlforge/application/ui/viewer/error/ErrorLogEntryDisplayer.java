package org.etlforge.application.ui.viewer.error;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Window;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import org.etlforge.application.errorlog.ErrorLogEntry;
import org.etlforge.application.ui.window.action.SupportActionFactory;
import org.etlforge.io.KongaIoUtils;
import org.etlforge.lang.exception.ExceptionUtils;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.action.ShowHideAction;
import org.etlforge.ui.event.ApplicationMessageEvent;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.util.clipboard.ClipboardUtils;
import org.etlforge.util.preference.KongaPreferences;

public final class ErrorLogEntryDisplayer implements UiProvider {
   private static final Border TOP_PANEL_BORDER = Empty.border(5);
   private static final Border SUB_PANEL_BORDER = Empty.border(15, 5, 5, 5);
   private static final KongaPreferences PREFERENCES = KongaPreferences.getPreferences(ErrorLogEntryDisplayer.class);
   private static final String DETAILS_SHOWING_KEY = "details_showing";
   private ErrorLogEntry error;
   private final MessageDisplayer messageDisplayer;
   private final DetailsDisplayer detailsDisplayer;
   private final StackTraceDisplayer stackTraceDisplayer;
   private final JComponent contentPane = Box.createVerticalBox();
   private JComponent topPanel;
   private JComponent detailsPanel;
   private JComponent stackTracePanel;
   private ShowHideAction detailsAction;
   private ShowHideAction stackTraceAction;
   private final Action copyToClipboardAction;
   private Action disposeHandler;
   private JButton disposeButton;

   public ErrorLogEntryDisplayer() {
      this.messageDisplayer = new MessageDisplayer();
      this.detailsDisplayer = new DetailsDisplayer();
      this.stackTraceDisplayer = new StackTraceDisplayer();
      this.copyToClipboardAction = new ErrorLogEntryDisplayer.CopyToClipBoardAction();
   }

   public void display(ErrorLogEntry var1) {
      this.error = var1;
      this.messageDisplayer.displayMessage(var1.message());
      this.detailsDisplayer.displayDetails(var1.details());
      this.stackTraceDisplayer.displayStackTrace(var1.cause());
   }

   public void setDetailsShowing(boolean var1) {
      this.detailsAction.setShowing(var1);
   }

   public void setStackTraceShowing(boolean var1) {
      this.stackTraceAction.setShowing(var1);
   }

   public void setDisposeHandler(Action var1) {
      this.disposeHandler = var1;
      if (this.disposeButton != null) {
         if (var1 != null) {
            this.disposeButton.setAction(var1);
            this.disposeButton.setEnabled(true);
         } else {
            this.disposeButton.setEnabled(false);
         }
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.createComponents();
      this.contentPane.add(this.topPanel);
      var1.display(this.contentPane);
      this.setDetailsShowing(PREFERENCES.getBoolean("details_showing", true));
   }

   @Override
   public void requestFocus() {
      if (this.disposeButton != null) {
         this.disposeButton.requestFocusInWindow();
      }
   }

   private void onLayoutChanged() {
      Window var1 = SwingUtilities.getWindowAncestor(this.contentPane);
      if (var1 instanceof JDialog) {
         var1.pack();
      }
   }

   private void createComponents() {
      this.createTopPanel();
      this.createDetailsPanel();
      this.createStackTracePanel();
   }

   private void createTopPanel() {
      this.topPanel = new JPanel() {
         @Override
         public Dimension getMaximumSize() {
            return new Dimension(super.getMaximumSize().width, super.getPreferredSize().height);
         }
      };
      this.topPanel.setOpaque(false);
      this.topPanel.setBorder(TOP_PANEL_BORDER);
      BorderLayoutBuilder.from(this.topPanel, 0, 10).center(this.messageDisplayer).south(this.createUpperButtonPanel());
   }

   private JComponent createUpperButtonPanel() {
      BoxBuilder var1 = BoxBuilder.horizontal();
      var1.glue();
      if (this.disposeHandler != null) {
         this.disposeButton = new KongaButton(this.disposeHandler);
         var1.add(this.disposeButton).strut(10);
      }

      this.detailsAction = new ErrorLogEntryDisplayer.DetailsAction();
      var1.add(new KongaButton(this.detailsAction));
      return var1.container();
   }

   private void createDetailsPanel() {
      BorderLayoutBuilder var1 = new BorderLayoutBuilder(0, 10);
      var1.center(this.detailsDisplayer).south(this.createLowerButtonPanel());
      var1.nonOpaque().withBorder(SUB_PANEL_BORDER);
      this.detailsPanel = var1.container();
   }

   private JComponent createLowerButtonPanel() {
      this.stackTraceAction = new ErrorLogEntryDisplayer.StackTraceAction();
      Box var1 = Box.createHorizontalBox();
      var1.add(Box.createHorizontalStrut(Icons.EMPTY_ICON.getIconWidth() + 5));
      var1.add(new KongaButton(this.copyToClipboardAction));
      if (SupportActionFactory.isEnabled()) {
         var1.add(Box.createHorizontalStrut(10));
         var1.add(new KongaButton(SupportActionFactory.newAction()));
      }

      var1.add(Box.createHorizontalGlue());
      var1.add(new KongaButton(this.stackTraceAction));
      return var1;
   }

   private void createStackTracePanel() {
      BorderLayoutBuilder var1 = new BorderLayoutBuilder(0, 10);
      var1.south(this.stackTraceDisplayer);
      var1.nonOpaque().withBorder(SUB_PANEL_BORDER);
      this.stackTracePanel = var1.container();
   }

   private class CopyToClipBoardAction extends AbstractAction {
      public CopyToClipBoardAction() {
         super(Strings.get("CopyToClipboard.Name"));
         this.putValue("ShortDescription", Strings.get("ExceptionDisplayer.Copy.ToolTip"));
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               CopyToClipBoardAction.this.copy();
            }
         });
      }

      private void copy() {
         String var1 = this.createString();
         this.toClipboard(var1);
         this.displayMessage();
      }

      private String createString() {
         StringBuilder var1 = new StringBuilder(ErrorLogEntryDisplayer.this.error.message());
         var1.append(KongaIoUtils.LINE_BREAK);
         var1.append(KongaIoUtils.LINE_BREAK);
         var1.append(ErrorLogEntryDisplayer.this.error.details());
         if (ErrorLogEntryDisplayer.this.error.cause() != null) {
            var1.append(KongaIoUtils.LINE_BREAK);
            var1.append(KongaIoUtils.LINE_BREAK);
            var1.append(ExceptionUtils.getStackTrace(ErrorLogEntryDisplayer.this.error.cause()));
         }

         return var1.toString();
      }

      private void toClipboard(String var1) {
         ClipboardUtils.copyToSystemClipboard(var1);
      }

      private void displayMessage() {
         ApplicationMessageEvent.postInformationMessage(ErrorLogEntryDisplayer.class, Strings.get("ExceptionDisplayer.Copy.Message"));
      }
   }

   private class DetailsAction extends ShowHideAction {
      public DetailsAction() {
         super(Strings.get("ExceptionDisplayer.Details.Name"), false);
      }

      @Override
      protected void show() {
         ErrorLogEntryDisplayer.PREFERENCES.putBoolean("details_showing", true);
         ErrorLogEntryDisplayer.this.contentPane.add(ErrorLogEntryDisplayer.this.detailsPanel);
         ErrorLogEntryDisplayer.this.onLayoutChanged();
      }

      @Override
      protected void hide() {
         ErrorLogEntryDisplayer.PREFERENCES.putBoolean("details_showing", false);
         ErrorLogEntryDisplayer.this.contentPane.remove(ErrorLogEntryDisplayer.this.detailsPanel);
         ErrorLogEntryDisplayer.this.stackTraceAction.setShowing(false);
         ErrorLogEntryDisplayer.this.onLayoutChanged();
      }
   }

   private class StackTraceAction extends ShowHideAction {
      public StackTraceAction() {
         super(Strings.get("ExceptionDisplayer.StackTrace.Name"), false);
      }

      @Override
      protected void show() {
         ErrorLogEntryDisplayer.this.contentPane.add(ErrorLogEntryDisplayer.this.stackTracePanel);
         ErrorLogEntryDisplayer.this.onLayoutChanged();
      }

      @Override
      protected void hide() {
         ErrorLogEntryDisplayer.this.contentPane.remove(ErrorLogEntryDisplayer.this.stackTracePanel);
         ErrorLogEntryDisplayer.this.onLayoutChanged();
      }
   }
}
