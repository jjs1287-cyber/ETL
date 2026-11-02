package org.etlforge.ui.widget.text;

import com.google.common.base.Charsets;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.Utilities;
import org.etlforge.io.KongaIoUtils;
import org.etlforge.lang.exception.ExceptionHandler;
import org.etlforge.lang.exception.ExceptionUtils;
import org.etlforge.util.Interval;

public final class TextComponentUtils {
   public static void selectAllWhenFocused(JTextComponent var0) {
      TextComponentUtils.SelectAllFocusListener.install(var0);
   }

   public static void disableSelectAllWhenFocused(JTextComponent var0) {
      FocusListener[] var1 = var0.getFocusListeners();

      for (FocusListener var5 : var1) {
         if (var5 instanceof TextComponentUtils.SelectAllFocusListener) {
            ((TextComponentUtils.SelectAllFocusListener)var5).uninstall();
         }
      }
   }

   public static void loadFile(File var0, JTextComponent var1, Object var2, ExceptionHandler<? super IOException> var3) {
      loadFile(var0, Charset.defaultCharset(), var1, var2, var3);
   }

   public static void loadFile(File var0, Charset var1, JTextComponent var2, Object var3, ExceptionHandler<? super IOException> var4) {
      InputStreamReader var5 = null;

      try {
         var5 = new InputStreamReader(new FileInputStream(var0), var1 != null ? var1 : Charset.defaultCharset());
         var2.read(var5, var3);
      } catch (FileNotFoundException var11) {
         ExceptionUtils.handle(var11, var4);
      } catch (IOException var12) {
         ExceptionUtils.handle(var12, var4);
      } finally {
         KongaIoUtils.close(var5);
      }
   }

   public static void saveFile(File var0, JTextComponent var1) throws IOException {
      OutputStreamWriter var2 = new OutputStreamWriter(new FileOutputStream(var0), Charsets.UTF_8);

      try {
         var1.write(var2);
      } finally {
         var2.close();
      }
   }

   public static MutableAttributeSet fromFont(Font var0, Color var1, Color var2) {
      SimpleAttributeSet var3 = new SimpleAttributeSet();
      StyleConstants.setFontSize(var3, var0.getSize());
      StyleConstants.setFontFamily(var3, var0.getFamily());
      int var4 = var0.getStyle();
      StyleConstants.setBold(var3, (var4 & 1) != 0);
      StyleConstants.setItalic(var3, (var4 & 2) != 0);
      StyleConstants.setBackground(var3, var2);
      StyleConstants.setForeground(var3, var1);
      return var3;
   }

   public static void select(JTextComponent var0, Interval var1) {
      int var2 = var1.startsAt();
      int var3 = var2 + var1.length();
      if (var2 != var0.getSelectionStart() || var3 != var0.getSelectionEnd()) {
         var0.setCaretPosition(var2);
         var0.moveCaretPosition(var3);
      }
   }

   public static void selectLine(JTextComponent var0, int var1) {
      try {
         int var2 = Utilities.getRowStart(var0, var1);
         int var3 = Utilities.getRowEnd(var0, var1);
         var0.setCaretPosition(var2);
         var0.moveCaretPosition(var3);
      } catch (BadLocationException var4) {
         var4.printStackTrace();
      }
   }

   public static boolean isStartOfLine(JTextComponent var0, int var1) {
      try {
         return Utilities.getRowStart(var0, var1) == var1;
      } catch (BadLocationException var3) {
         var3.printStackTrace();
         return false;
      }
   }

   private TextComponentUtils() {
   }

   private static final class SelectAllFocusListener extends FocusAdapter {
      private final JTextComponent component;

      private SelectAllFocusListener(JTextComponent var1) {
         this.component = var1;
      }

      public static void install(JTextComponent var0) {
         TextComponentUtils.SelectAllFocusListener var1 = new TextComponentUtils.SelectAllFocusListener(var0);
         var0.addFocusListener(var1);
      }

      @Override
      public void focusGained(FocusEvent var1) {
         if (var1.getSource() == this.component) {
            this.component.selectAll();
         }
      }

      public void uninstall() {
         this.component.removeFocusListener(this);
      }
   }
}
