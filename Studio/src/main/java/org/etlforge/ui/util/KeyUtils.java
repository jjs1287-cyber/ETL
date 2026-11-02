package org.etlforge.ui.util;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;
import org.apache.commons.lang.SystemUtils;
import org.etlforge.ui.MnemonicString;

public final class KeyUtils {
   private static final int MENU_SHORTCUT_KEY_MASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
   public static final KeyStroke COPY = getCtrlDown(67);
   public static final KeyStroke ENTER = KeyStroke.getKeyStroke(10, 0);
   public static final KeyStroke SPACE = KeyStroke.getKeyStroke(32, 0);
   public static final KeyStroke DELETE = KeyStroke.getKeyStroke(127, 0);
   public static final KeyStroke ESCAPE = KeyStroke.getKeyStroke(27, 0);
   public static final KeyStroke TAB = KeyStroke.getKeyStroke(9, 0);
   public static final KeyStroke F1 = KeyStroke.getKeyStroke(112, 0);
   public static final KeyStroke F2 = KeyStroke.getKeyStroke(113, 0);
   public static final KeyStroke F5 = KeyStroke.getKeyStroke(116, 0);
   public static final KeyStroke F6 = KeyStroke.getKeyStroke(117, 0);
   public static final KeyStroke F8 = KeyStroke.getKeyStroke(119, 0);
   public static final KeyStroke F11 = KeyStroke.getKeyStroke(122, 0);
   public static final KeyStroke F12 = KeyStroke.getKeyStroke(123, 0);
   public static final KeyStroke UNDO = getCtrlDown(90);
   public static final KeyStroke REDO = SystemUtils.IS_OS_MAC ? getCtrlShiftDown(90) : getCtrlDown(89);
   public static final KeyStroke REFRESH = F5;
   public static final KeyStroke SAVE = getCtrlDown(83);
   public static final KeyStroke FIND = getCtrlDown(70);
   public static final KeyStroke FIND_AND_REPLACE = getCtrlDown(72);
   public static final KeyStroke DOWN = KeyStroke.getKeyStroke(40, 0);
   public static final KeyStroke UP = KeyStroke.getKeyStroke(38, 0);
   public static final KeyStroke LEFT = KeyStroke.getKeyStroke(37, 0);
   public static final KeyStroke RIGHT = KeyStroke.getKeyStroke(39, 0);
   public static final KeyStroke HELP = F1;
   public static final KeyStroke PRINT = getCtrlDown(80);
   public static final KeyStroke CONTEXT_MENU = getShiftDown(121);
   public static final KeyStroke EXPAND_EVERYTHING = KeyStroke.getKeyStroke(106, 0);
   public static final KeyStroke NUMPAD_PLUS = KeyStroke.getKeyStroke(107, 0);
   public static final KeyStroke NUMPAD_MINUS = KeyStroke.getKeyStroke(109, 0);
   public static final KeyStroke CONTENT_ASSIST = KeyStroke.getKeyStroke(32, 128);

   public static KeyStroke getCtrlDown(int var0) {
      return KeyStroke.getKeyStroke(var0, MENU_SHORTCUT_KEY_MASK);
   }

   public static KeyStroke getShiftDown(int var0) {
      return KeyStroke.getKeyStroke(var0, 64);
   }

   public static KeyStroke getCtrlShiftDown(int var0) {
      return KeyStroke.getKeyStroke(var0, MENU_SHORTCUT_KEY_MASK | 64);
   }

   public static KeyStroke getAltDown(int var0) {
      return KeyStroke.getKeyStroke(var0, 512);
   }

   public static KeyStroke getOpenKey() {
      return getCtrlDown(79);
   }

   public static KeyStroke getFindKey() {
      return getCtrlDown(70);
   }

   public static boolean isCtrlDown(KeyEvent var0) {
      return (var0.getModifiersEx() & 128) != 0;
   }

   public static boolean isShiftDown(KeyEvent var0) {
      return (var0.getModifiersEx() & 64) != 0;
   }

   public static Integer getMnemonicKeyCode(MnemonicString var0) {
      char var1 = var0.getMnemonic();
      if (Character.isLetter(var1)) {
         return Integer.valueOf(Character.toUpperCase(var1));
      } else {
         return Character.isDigit(var1) ? Integer.valueOf(var1) : null;
      }
   }

   private KeyUtils() {
   }
}
