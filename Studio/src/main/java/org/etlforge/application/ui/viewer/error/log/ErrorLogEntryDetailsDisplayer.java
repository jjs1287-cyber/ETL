package org.etlforge.application.ui.viewer.error.log;

import com.google.common.base.Supplier;
import java.text.DateFormat;
import java.util.Date;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.etlforge.application.errorlog.ErrorLogEntry;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.viewer.error.StackTraceDisplayer;
import org.etlforge.ui.Size;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.util.CopyToClipboardButton;
import org.etlforge.ui.widget.text.KongaTextArea;
import org.etlforge.ui.widget.text.KongaTextField;

public final class ErrorLogEntryDetailsDisplayer implements UiProvider {
   private final ErrorLogEntry entry;
   private final KongaTextArea messageField;
   private final JTextArea detailsArea;
   private final JTextField timestampField;
   private final JTextField sourceField;
   private final JTextField levelField;
   private final StackTraceDisplayer stackTraceArea;
   private final UiProvider copyToClipboardButton;

   public ErrorLogEntryDetailsDisplayer(ErrorLogEntry var1) {
      this.entry = var1;
      this.messageField = KongaTextArea.viewer(var1.message(), Size.rows(3), Size.columns(60));
      this.detailsArea = this.createDetailsArea(var1);
      this.timestampField = this.createTimestampField(var1);
      this.sourceField = this.createSourceField(var1);
      this.levelField = this.createLevelField(var1);
      this.stackTraceArea = this.createStackTraceArea(var1);
      this.copyToClipboardButton = this.createCopyToClipboardButton();
   }

   @Override
   public void decorate(UiCanvas var1) {
      new ErrorLogEntryDetailsDisplayer.Layout().decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.messageField.requestFocusInWindow();
   }

   private JTextField createTimestampField(ErrorLogEntry var1) {
      Date var2 = new Date(var1.timestamp());
      String var3 = DateFormat.getDateTimeInstance().format(var2);
      return this.createValueField(var3);
   }

   private JTextField createSourceField(ErrorLogEntry var1) {
      String var2 = this.getSourceString(var1.source());
      return this.createValueField(var2);
   }

   private String getSourceString(Object var1) {
      if (var1 == null) {
         return "[Unknown]";
      } else {
         return var1 instanceof Class ? ((Class)var1).getName() : var1.toString();
      }
   }

   private JTextField createLevelField(ErrorLogEntry var1) {
      String var2 = var1.level().name();
      return this.createValueField(var2);
   }

   private JTextArea createDetailsArea(ErrorLogEntry var1) {
      KongaTextArea var2 = KongaTextArea.viewer(var1.details().trim(), Size.rows(8), Size.columns(40));
      TextStyles.DefaultMonoSpaced.makeOver(var2);
      return var2;
   }

   private JTextField createValueField(String var1) {
      KongaTextField var2 = new KongaTextField(var1);
      var2.setEditable(false);
      var2.setSelectAllWhenFocused(true);
      return var2;
   }

   private StackTraceDisplayer createStackTraceArea(ErrorLogEntry var1) {
      Throwable var2 = var1.cause();
      if (var2 == null) {
         return null;
      }

      StackTraceDisplayer var3 = new StackTraceDisplayer();
      var3.displayStackTrace(var2);
      var3.setSimpleLayout(true);
      return var3;
   }

   private UiProvider createCopyToClipboardButton() {
      return new CopyToClipboardButton(new Supplier<String>() {
         public String get() {
            return ErrorLogEntryDetailsDisplayer.this.entry.getReport();
         }
      });
   }

   private class Layout {
      private final TwoColumnPanel grid = this.emptyGrid();
      private double remainingWeightY = 1.0;

      public Layout() {
         this.layoutMessage();
         this.layoutField("Time:", ErrorLogEntryDetailsDisplayer.this.timestampField);
         this.layoutField("Reported from:", ErrorLogEntryDetailsDisplayer.this.sourceField);
         this.layoutField("Level:", ErrorLogEntryDetailsDisplayer.this.levelField);
         this.layoutDetails();
         this.layoutStackTrace();
         this.layoutCopyButton();
      }

      public void decorate(UiCanvas var1) {
         this.grid.decorate(var1);
      }

      private TwoColumnPanel emptyGrid() {
         TwoColumnPanel var1 = new TwoColumnPanel();
         var1.withLeftInsets(0, 0, 10, 5);
         var1.withRightInsets(0, 0, 10, 0);
         var1.withRightWeights(1.0, 0.0);
         var1.withRightFill(GridBagLayoutPanel.Fill.HORIZONTAL);
         return var1;
      }

      private void layoutMessage() {
         this.grid.withAnchor(GridBagLayoutPanel.Anchor.NORTHWEST);
         this.grid.withRightFill(GridBagLayoutPanel.Fill.BOTH);
         this.grid.withRightWeights(1.0, this.reserveWeightY(0.1));
         this.grid.addRow(new JLabel("Message:"), new JScrollPane(ErrorLogEntryDetailsDisplayer.this.messageField));
      }

      private double reserveWeightY(double var1) {
         var1 = Math.min(var1, this.remainingWeightY);
         this.remainingWeightY -= var1;
         return var1;
      }

      private void layoutField(String var1, JComponent var2) {
         this.grid.withAnchor(GridBagLayoutPanel.Anchor.WEST);
         this.grid.withRightFill(GridBagLayoutPanel.Fill.HORIZONTAL);
         this.grid.withRightWeights(1.0, 0.0);
         this.grid.addRow(new JLabel(var1), var2);
      }

      private void layoutDetails() {
         this.grid.withAnchor(GridBagLayoutPanel.Anchor.NORTHWEST);
         double var1 = ErrorLogEntryDetailsDisplayer.this.stackTraceArea == null ? this.remainingWeightY : this.reserveWeightY(0.25);
         this.grid.withRightWeights(1.0, var1);
         this.grid.withRightFill(GridBagLayoutPanel.Fill.BOTH);
         this.grid.addRow(new JLabel("Details:"), new JScrollPane(ErrorLogEntryDetailsDisplayer.this.detailsArea));
      }

      private void layoutStackTrace() {
         if (ErrorLogEntryDetailsDisplayer.this.stackTraceArea != null) {
            this.grid.withAnchor(GridBagLayoutPanel.Anchor.NORTHWEST);
            this.grid.withRightWeights(1.0, this.remainingWeightY);
            this.grid.withRightFill(GridBagLayoutPanel.Fill.BOTH);
            this.grid.addRow(new JLabel("Stacktrace:"), BorderLayoutBuilder.fillWith(ErrorLogEntryDetailsDisplayer.this.stackTraceArea));
         }
      }

      private void layoutCopyButton() {
         this.grid.withRightFill(GridBagLayoutPanel.Fill.NONE);
         this.grid.withRightWeights(0.0, 0.0);
         this.grid.addRow(null, ErrorLogEntryDetailsDisplayer.this.copyToClipboardButton);
      }
   }
}
