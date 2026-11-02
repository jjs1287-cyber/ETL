package org.etlforge.application.ui.viewer.error.log;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.util.LinkedList;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.errorlog.ErrorLogEntry;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.application.ui.viewer.error.dialog.ErrorDialog;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.button.ButtonUtils;

public class BasicErrorLogDisplayer extends AbstractErrorLogDisplayer implements ErrorLogDisplayer {
   private static final DateFormat TIMESTAMP_FORMAT = DateFormat.getDateTimeInstance(2, 2);
   private final LinkedList<ErrorLogEntry> loggedEntries = new LinkedList<>(ErrorLog.getLoggedErrors());
   private final JComponent ui = this.createUi();
   private JComponent defaultFocusOwner;

   @Override
   protected void handleNewError(final ErrorLogEntry var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            BasicErrorLogDisplayer.this.addEntryToUi(var1);
         }
      });
   }

   private void addEntryToUi(ErrorLogEntry var1) {
      this.loggedEntries.addFirst(var1);
      this.layoutComponents(this.ui);
      this.ui.validate();
      this.ui.repaint();
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.ui);
   }

   @Override
   public void requestFocus() {
      if (this.defaultFocusOwner != null) {
         this.defaultFocusOwner.requestFocus();
      }
   }

   private JComponent createUi() {
      Box var1 = Box.createVerticalBox();
      this.layoutComponents(var1);
      var1.setBorder(Empty.border(5));
      return var1;
   }

   private void layoutComponents(JComponent var1) {
      var1.removeAll();
      this.defaultFocusOwner = null;

      for (ErrorLogEntry var3 : this.loggedEntries) {
         BasicErrorLogDisplayer.EntryButton var4 = new BasicErrorLogDisplayer.EntryButton(var3);
         if (this.defaultFocusOwner == null) {
            this.defaultFocusOwner = var4;
         }

         var1.add(var4);
         var1.add(Box.createVerticalStrut(5));
      }

      UiUtils.setOpaqueRecursivelySkipDefaults(var1, false);
   }

   private static class EntryButton extends JButton implements ActionListener {
      private static final Color TEXT_COLOR = Color.BLACK;
      private static final Color TEXT_COLOR_HOVER = Color.RED;
      private final ErrorLogEntry entry;

      public EntryButton(ErrorLogEntry var1) {
         super(BasicErrorLogDisplayer.TIMESTAMP_FORMAT.format(var1.timestamp()) + ": " + var1.message(), CommonIcons.ERROR_16);
         this.entry = var1;
         this.setContentAreaFilled(false);
         ButtonUtils.enableRolloverForButton(this);
         this.setFont(TextStyles.DefaultText.getFont());
         this.setForeground(TEXT_COLOR);
         this.addActionListener(this);
         this.addMouseListener(new BasicErrorLogDisplayer.EntryButton.TextPainter());
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         JFrame var2 = ApplicationUi.getAppWinFrame();
         ErrorDialog var3 = new ErrorDialog(var2, this.entry);
         var3.setDetailsShowing(true);
         var3.setVisible(true);
      }

      private class TextPainter extends MouseAdapter {
         private TextPainter() {
         }

         @Override
         public void mouseEntered(MouseEvent var1) {
            EntryButton.this.setForeground(BasicErrorLogDisplayer.EntryButton.TEXT_COLOR_HOVER);
         }

         @Override
         public void mouseExited(MouseEvent var1) {
            EntryButton.this.setForeground(BasicErrorLogDisplayer.EntryButton.TEXT_COLOR);
         }
      }
   }
}
