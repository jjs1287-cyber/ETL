package org.etlforge.cli.ui;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import javax.swing.AbstractAction;
import javax.swing.text.BadLocationException;
import javax.swing.text.NavigationFilter;
import javax.swing.text.NavigationFilter.FilterBypass;
import javax.swing.text.Position.Bias;
import org.apache.commons.lang.StringUtils;
import org.etlforge.functors.function.NullFunction;
import org.etlforge.system.callback.NoReceiver;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.text.DocumentSectionGuard;
import org.etlforge.ui.util.KeyBindingInstaller;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.widget.text.FileDropStrategy;
import org.etlforge.ui.widget.text.FileDropSupport;
import org.etlforge.ui.widget.text.KongaTextField;

public final class CommandLineField implements CommandProducer {
   private static final String MARKER = "$ ";
   private static final int MARKER_LENGTH = "$ ".length();
   private final KongaTextField field;
   private Receiver<String> consumer;
   private Function<String, String> completer;
   private final DocumentSectionGuard guard;
   private final LinkedList<String> history;
   private int historyPoint;
   private int historySize = 100;

   public CommandLineField() {
      this.field = new KongaTextField("$ ");
      this.consumer = NoReceiver.create();
      this.completer = NullFunction.create();
      this.history = Lists.newLinkedList();
      this.guard = this.installGuard();
      this.configureAppearance();
      this.installKeyBindings();
      this.field.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            CommandLineField.this.processCommand();
         }
      });
      this.field.setCaretPosition(MARKER_LENGTH);
   }

   private void configureAppearance() {
      this.field.setBackground(new Color(0, 20, 88));
      new TextStyle("Monospaced, Courier New, Courier", 16, 1, Color.WHITE).makeOver(this.field);
      this.field.setMargin(new Insets(3, 3, 3, 3));
      this.field.setSelectionColor(Color.WHITE);
      this.field.setSelectedTextColor(Color.BLACK);
      FileDropSupport.install(this.field, FileDropStrategy.INSERT_FULL_PATH);
      this.field.setFocusTraversalKeysEnabled(false);
   }

   private DocumentSectionGuard installGuard() {
      DocumentSectionGuard var1 = new DocumentSectionGuard(this.field.getDocument());

      try {
         var1.guard(0, MARKER_LENGTH);
         this.field.setNavigationFilter(new CommandLineField.NavigationFilterImpl());
      } catch (BadLocationException var3) {
         var3.printStackTrace();
      }

      return var1;
   }

   private void installKeyBindings() {
      KeyBindingInstaller var1 = new KeyBindingInstaller(this.field);
      var1.addBinding(KeyUtils.UP, "up", new CommandLineField.HistoryAction(-1));
      var1.addBinding(KeyUtils.DOWN, "down", new CommandLineField.HistoryAction(1));
      var1.addBinding(KeyUtils.ESCAPE, "clear", new CommandLineField.ClearTextAction());
      var1.addBinding(KeyUtils.TAB, "complete", new CommandLineField.CompletionAction());
      var1.install();
   }

   public void setWidth(int var1) {
      this.field.setColumns(var1);
   }

   @Override
   public void setCommandConsumer(Receiver<String> var1) {
      this.consumer = var1;
   }

   @Override
   public void setCompleter(Function<String, String> var1) {
      this.completer = var1;
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.field.asUiProvider().decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.field.requestFocusInWindow();
   }

   private void processCommand() {
      String var1 = this.getCommand();
      if (!var1.isEmpty()) {
         this.clearCurrentCommand();
         this.addToHistory(var1);
         this.notifyConsumer(var1);
      }
   }

   private String getCommand() {
      return StringUtils.removeStart(this.field.getText(), "$ ");
   }

   private void clearCurrentCommand() {
      this.setDisplayedCommand("");
   }

   private void setDisplayedCommand(String var1) {
      this.guard.setActive(false);
      this.field.setText("$ " + var1);
      this.guard.setActive(true);
   }

   private void addToHistory(String var1) {
      if (!this.isSameAsLastCommand(var1)) {
         this.history.addLast(var1);
         this.trimHistorySize();
      }

      this.historyPoint = this.history.size();
   }

   private boolean isSameAsLastCommand(String var1) {
      return !this.history.isEmpty() && this.history.getLast().equals(var1);
   }

   private void trimHistorySize() {
      if (this.history.size() > this.historySize) {
         this.history.removeFirst();
      }
   }

   private void notifyConsumer(final String var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            CommandLineField.this.consumer.handle(var1);
         }
      });
   }

   private final class ClearTextAction extends AbstractAction {
      private ClearTextAction() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         CommandLineField.this.clearCurrentCommand();
      }
   }

   private final class CompletionAction extends AbstractAction {
      private CompletionAction() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         String var2 = CommandLineField.this.getCommand();
         String var3 = CommandLineField.this.completer.apply(var2);
         if (var3 != null) {
            try {
               CommandLineField.this.field.getDocument().insertString(CommandLineField.this.field.getCaretPosition(), var3, null);
            } catch (BadLocationException var5) {
               var5.printStackTrace();
            }
         }
      }
   }

   private final class HistoryAction extends AbstractAction {
      private final int step;

      public HistoryAction(int var2) {
         this.step = var2;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (!CommandLineField.this.history.isEmpty()) {
            CommandLineField.this.historyPoint = CommandLineField.this.historyPoint + this.step;
            if (this.step < 0) {
               CommandLineField.this.historyPoint = Math.max(0, CommandLineField.this.historyPoint);
            } else {
               CommandLineField.this.historyPoint = Math.min(CommandLineField.this.historyPoint, CommandLineField.this.history.size() - 1);
            }

            CommandLineField.this.setDisplayedCommand(CommandLineField.this.history.get(CommandLineField.this.historyPoint));
         }
      }
   }

   private static final class NavigationFilterImpl extends NavigationFilter {
      private NavigationFilterImpl() {
      }

      @Override
      public void setDot(FilterBypass var1, int var2, Bias var3) {
         var1.setDot(Math.max(var2, CommandLineField.MARKER_LENGTH), var3);
      }

      @Override
      public void moveDot(FilterBypass var1, int var2, Bias var3) {
         var1.moveDot(Math.max(var2, CommandLineField.MARKER_LENGTH), var3);
      }
   }
}
