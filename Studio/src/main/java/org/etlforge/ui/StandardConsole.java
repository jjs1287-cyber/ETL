package org.etlforge.ui;

import java.awt.Color;
import java.awt.Font;
import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import org.etlforge.system.Disposable;
import org.etlforge.ui.util.FontUtils;
import org.etlforge.ui.widget.text.KongaTextPane;

public class StandardConsole implements UiProvider, Disposable {
   public static final StandardConsole.Mode REPLACE = StandardConsole.Mode.REPLACE;
   public static final StandardConsole.Mode TAP_IN = StandardConsole.Mode.TAP_IN;
   private final StandardConsole.Mode mode;
   private final JTextPane textPane;
   private PrintStream savedOut;
   private PrintStream savedErr;

   public StandardConsole(StandardConsole.Mode var1) {
      if (var1 == null) {
         throw new NullPointerException("mode");
      }

      this.mode = var1;
      this.textPane = this.createTextPane();
   }

   private JTextPane createTextPane() {
      KongaTextPane var1 = new KongaTextPane();
      var1.setEditable(false);
      String[] var2 = new String[]{"Courier New", "Courier"};
      Font var3 = FontUtils.getMatchingFont(var2, 12, 0);
      var1.setFont(var3);
      StandardConsole.StyledDocumentDefiner var4 = new StandardConsole.StyledDocumentDefiner();
      var4.addStylesToDocument(var1.getStyledDocument());
      return var1;
   }

   public void install() {
      this.installStdOut();
      this.installStdErr();
   }

   private void installStdOut() {
      this.savedOut = System.out;
      StandardConsole.Stream var1 = new StandardConsole.Stream("out", this.mode == REPLACE ? null : this.savedOut);
      PrintStream var2 = new PrintStream(var1);
      System.setOut(var2);
   }

   private void installStdErr() {
      this.savedErr = System.err;
      StandardConsole.Stream var1 = new StandardConsole.Stream("err", this.mode == REPLACE ? null : this.savedErr);
      PrintStream var2 = new PrintStream(var1);
      System.setErr(var2);
   }

   @Override
   public void dispose() {
      if (this.savedOut != null) {
         System.setOut(this.savedOut);
      }

      if (this.savedErr != null) {
         System.setErr(this.savedErr);
      }
   }

   public void clear() {
      this.textPane.setText("");
   }

   @Override
   public void decorate(UiCanvas var1) {
      JScrollPane var2 = new JScrollPane(this.textPane);
      var1.display(var2);
   }

   @Override
   public void requestFocus() {
      this.textPane.requestFocusInWindow();
   }

   private void insertText(String var1, String var2) {
      StyledDocument var3 = this.textPane.getStyledDocument();

      try {
         var3.insertString(var3.getLength(), var1, var3.getStyle(var2));
      } catch (BadLocationException var5) {
      }
   }

   public enum Mode {
      REPLACE,
      TAP_IN;
   }

   private class Stream extends FilterOutputStream {
      private final String style;
      private final PrintStream other;

      public Stream(String var2, PrintStream var3) {
         super(new ByteArrayOutputStream());
         this.style = var2;
         this.other = var3;
      }

      @Override
      public void write(byte[] var1, int var2, int var3) {
         if (this.other != null) {
            this.other.write(var1, var2, var3);
         }

         String var4 = new String(var1, var2, var3);
         StandardConsole.this.insertText(var4, this.style);
      }

      @Override
      public void write(byte[] var1) throws IOException {
         if (this.other != null) {
            this.other.write(var1);
         }

         String var2 = new String(var1);
         StandardConsole.this.insertText(var2, this.style);
      }

      @Override
      public void write(int var1) {
         if (this.other != null) {
            this.other.write(var1);
         }

         String var2 = new String(new char[]{(char)var1});
         StandardConsole.this.insertText(var2, this.style);
      }
   }

   private static final class StyledDocumentDefiner {
      public static final String OUT = "out";
      public static final String ERR = "err";

      private StyledDocumentDefiner() {
      }

      public void addStylesToDocument(StyledDocument var1) {
         Style var2 = StyleContext.getDefaultStyleContext().getStyle("default");
         var1.addStyle("out", var2);
         Style var3 = var1.addStyle("err", var2);
         StyleConstants.setForeground(var3, Color.RED);
      }
   }
}
