package org.etlforge.application.ui.look;

import java.awt.Color;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.plaf.InsetsUIResource;
import org.etlforge.system.SystemUtils;
import org.etlforge.ui.UiResources;
import org.etlforge.ui.util.rename.ItemRenamer;
import org.etlforge.ui.util.time.DayOfTheWeekCheckBoxPanel;
import org.etlforge.ui.util.time.TimeOfTheDayControl;
import org.etlforge.util.properties.ReadOnlyProperties;

public final class LookAndFeelCustomizer {
   private static final boolean USE_SYSTEM_LF;
   public static final boolean IS_BOLD_METAL;
   private static final boolean USE_WINDOWS_BUTTONS_ON_WINDOWS;
   private static final boolean CUSTOMIZE_WINDOWS_TOOLBAR;

   private static boolean shouldUseSystemAppearance(ReadOnlyProperties var0) {
      return var0.getBoolean("UseSystemLF", false) ? true : SystemUtils.IS_OS_WINDOWS_VISTA;
   }

   private LookAndFeelCustomizer() {
   }

   public static void customize() {
      if (Boolean.parseBoolean(System.getProperty("etlforge.studio.dark", "true"))) {
         installEtlForgeDarkAppearance();
         configureEtlForgeUiResources();
         configureEtlForgeTooltips();
         return;
      }
      String var0 = System.getProperty("swing.defaultlaf");
      if (var0 != null) {
         installConfiguredAppearance(var0);
      } else {
         SupportedLookAndFeel var1 = LookAndFeelPreference.get();
         if (var1 != null) {
            installSelectedAppearance(var1);
         }

         if (org.apache.commons.lang.SystemUtils.IS_OS_WINDOWS) {
            customizeWindowsOptionPaneButtons();
         }

         UIManager.put("Button.defaultButtonFollowsFocus", Boolean.TRUE);
         configureEtlForgeUiResources();
         configureEtlForgeTooltips();
      }
   }

   private static void installEtlForgeDarkAppearance() {
      try {
         UIManager.setLookAndFeel(org.apache.commons.lang.SystemUtils.IS_OS_WINDOWS
            ? "com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel"
            : UIManager.getCrossPlatformLookAndFeelClassName());
      } catch (Exception ex) {
         throw new IllegalStateException("Cannot initialize the Studio appearance", ex);
      }
      javax.swing.plaf.ColorUIResource panel = new javax.swing.plaf.ColorUIResource(0, 0, 0);
      javax.swing.plaf.ColorUIResource field = new javax.swing.plaf.ColorUIResource(24, 24, 24);
      javax.swing.plaf.ColorUIResource text = new javax.swing.plaf.ColorUIResource(255, 255, 255);
      javax.swing.plaf.ColorUIResource muted = new javax.swing.plaf.ColorUIResource(255, 255, 255);
      javax.swing.plaf.ColorUIResource selected = new javax.swing.plaf.ColorUIResource(64, 64, 64);
      javax.swing.plaf.ColorUIResource border = new javax.swing.plaf.ColorUIResource(77, 81, 88);
                                                                                
      java.util.List<Object> keys = new java.util.ArrayList<Object>(UIManager.getLookAndFeelDefaults().keySet());
      for (Object key : keys) {
         Object value = UIManager.get(key);
         if (value instanceof Color) {
            String name = key.toString().toLowerCase(java.util.Locale.ROOT);
            Color color = name.contains("background") ? panel : text;
            if (name.contains("selectionbackground")) color = selected;
            else if (name.contains("selectionforeground")) color = text;
            else if (name.contains("disabled") || name.contains("inactiveforeground")) color = muted;
            else if (name.contains("shadow") || name.contains("border") || name.contains("grid") || name.contains("highlight")) color = border;
            UIManager.put(key, color);
         } else if (value instanceof java.awt.Font) {
            UIManager.put(key, new javax.swing.plaf.FontUIResource("Segoe UI", java.awt.Font.PLAIN, 12));
         }
      }
      for (String name : new String[]{"TextField", "FormattedTextField", "PasswordField", "TextArea", "TextPane", "EditorPane", "List", "Table", "Tree", "Viewport", "Desktop"}) {
         UIManager.put(name + ".background", field);
         UIManager.put(name + ".foreground", text);
         UIManager.put(name + ".caretForeground", text);
         UIManager.put(name + ".selectionBackground", selected);
         UIManager.put(name + ".selectionForeground", text);
      }
      UIManager.put("control", panel); UIManager.put("window", field);
      UIManager.put("text", field); UIManager.put("textText", text);
      UIManager.put("controlText", text); UIManager.put("menuText", text);
      UIManager.put("textHighlight", selected); UIManager.put("textHighlightText", text);
      UIManager.put("controlHighlight", border); UIManager.put("controlLtHighlight", border);
      UIManager.put("controlShadow", field); UIManager.put("controlDkShadow", field);
      UIManager.put("activeCaption", panel); UIManager.put("activeCaptionText", text);
      UIManager.put("inactiveCaption", panel); UIManager.put("inactiveCaptionText", muted);
      UIManager.put("InternalFrame.activeTitleBackground", panel);
      UIManager.put("InternalFrame.activeTitleGradient", panel);
      UIManager.put("InternalFrame.activeTitleForeground", text);
      UIManager.put("InternalFrame.inactiveTitleBackground", panel);
      UIManager.put("InternalFrame.inactiveTitleGradient", panel);
      UIManager.put("InternalFrame.inactiveTitleForeground", muted);
      UIManager.put("Table.gridColor", border);
      javax.swing.plaf.ColorUIResource edge = new javax.swing.plaf.ColorUIResource(176, 176, 176);
      javax.swing.plaf.ColorUIResource thumb = new javax.swing.plaf.ColorUIResource(120, 120, 120);
      UIManager.put("ScrollBar.background", new javax.swing.plaf.ColorUIResource(48, 48, 48));
      UIManager.put("ScrollBar.track", new javax.swing.plaf.ColorUIResource(48, 48, 48));
      UIManager.put("ScrollBar.trackHighlight", new javax.swing.plaf.ColorUIResource(64, 64, 64));
      UIManager.put("ScrollBar.thumb", thumb);
      UIManager.put("ScrollBar.thumbHighlight", new javax.swing.plaf.ColorUIResource(144, 144, 144));
      UIManager.put("ScrollBar.thumbShadow", new javax.swing.plaf.ColorUIResource(88, 88, 88));
      UIManager.put("ScrollBar.thumbDarkShadow", new javax.swing.plaf.ColorUIResource(72, 72, 72));
      UIManager.put("ScrollBar.foreground", text);
      UIManager.put("Separator.foreground", edge);
      UIManager.put("Button.border", new javax.swing.plaf.BorderUIResource.CompoundBorderUIResource(
         new javax.swing.plaf.BorderUIResource.LineBorderUIResource(edge), javax.swing.BorderFactory.createEmptyBorder(3, 8, 3, 8)));
      UIManager.put("ToggleButton.border", UIManager.get("Button.border"));
      UIManager.put("TableHeader.cellBorder", new javax.swing.plaf.BorderUIResource.LineBorderUIResource(edge));
      UIManager.put("PopupMenu.border", new javax.swing.plaf.BorderUIResource.LineBorderUIResource(edge));
      UIManager.put("InternalFrame.border", new javax.swing.plaf.BorderUIResource.LineBorderUIResource(edge, 2));
      for (String name : new String[]{"ScrollPane", "TextField", "PasswordField", "FormattedTextField", "Table", "List", "Tree"}) {
         UIManager.put(name + ".border", new javax.swing.plaf.BorderUIResource.LineBorderUIResource(edge));
      }
      UIManager.put("InternalFrame.closeIcon", new CaptionIcon(0));
      UIManager.put("InternalFrame.iconifyIcon", new CaptionIcon(1));
      UIManager.put("InternalFrame.maximizeIcon", new CaptionIcon(2));
      UIManager.put("InternalFrame.minimizeIcon", new CaptionIcon(3));
      for (String name : new String[]{"Button", "ToggleButton", "Label", "Menu", "MenuItem", "CheckBoxMenuItem", "RadioButtonMenuItem", "CheckBox", "RadioButton", "ComboBox", "TabbedPane", "TableHeader", "ToolTip"}) {
         UIManager.put(name + ".foreground", text);
         UIManager.put(name + ".disabledText", muted);
         UIManager.put(name + ".disabledForeground", muted);
         UIManager.put(name + ".background", panel);
      }
      UIManager.put("konga.tableheader.background", panel); UIManager.put("konga.tableheader.foreground", text);
      UIManager.put("konga.overlaydecorator.foreground", text);
      UIManager.put("konga.overlaydecorator.background", field);
      UIManager.put("konga.overlaydecorator.content.background", panel);
      UIManager.put("konga.overlaydecorator.outerborder", edge);
      UIManager.put("konga.overlaydecorator.innerborder", edge);
      UIManager.put("konga.overlay.scrim", new Color(0, 0, 0, 166));
      UIManager.put("etlforge.theme.text.color", new javax.swing.plaf.ColorUIResource(255, 255, 255));
      UIManager.put("etlforge.mappingtree.backgroundColor", field);
      UIManager.put("etlforge.mappingtree.folderColor", text);
      UIManager.put("etlforge.mappingtree.attributeColor", text);
      UIManager.put("etlforge.mappingtree.preconditionColor", new javax.swing.plaf.ColorUIResource(255, 255, 255));
      UIManager.put("Button.defaultButtonFollowsFocus", Boolean.TRUE);
      String tableStyle = EtlForgeDarkTableStyle.class.getName();
      UIManager.put("konga.tablestyle.narrow", tableStyle);
      UIManager.put("konga.tablestyle.normal", tableStyle);
      UIManager.put("konga.tablestyle.wide", tableStyle);
      UIManager.put("konga.tablestyle.font", TextStyles.DefaultTableText.getFont());
      UIManager.put("konga.overlaydecorator.caption-style", TextStyles.BigCaption);
      UIManager.put("konga.dropdownpanel.title-style", TextStyles.DropDownPanelTitle);
   }

   private static final class CaptionIcon implements javax.swing.Icon, javax.swing.plaf.UIResource {
      private final int kind;
      CaptionIcon(int kind) { this.kind = kind; }
      public int getIconWidth() { return 16; }
      public int getIconHeight() { return 14; }
      public void paintIcon(java.awt.Component c, java.awt.Graphics graphics, int x, int y) {
         java.awt.Graphics2D g = (java.awt.Graphics2D) graphics.create();
         try {
            g.translate(x, y);
            g.setColor(Color.WHITE);
            g.setStroke(new java.awt.BasicStroke(1.5f));
            if (kind == 0) { g.drawLine(4, 3, 12, 11); g.drawLine(12, 3, 4, 11); }
            else if (kind == 1) { g.drawLine(4, 11, 12, 11); }
            else if (kind == 2) { g.drawRect(3, 3, 10, 8); g.drawLine(3, 4, 13, 4); }
            else { g.drawRect(6, 2, 7, 6); g.setColor(Color.BLACK); g.fillRect(3, 5, 8, 7); g.setColor(Color.WHITE); g.drawRect(3, 5, 8, 7); }
         } finally { g.dispose(); }
      }
   }

   private static void installConfiguredAppearance(String var0) {
      SupportedLookAndFeel var1 = SupportedLookAndFeel.forClassName(var0);
      if (var1 != null) {
         var1.customize();
      }
   }

   private static void installSelectedAppearance(SupportedLookAndFeel var0) {
      try {
         var0.install();
      } catch (Exception var2) {
         var2.printStackTrace();
         if (var0 != SupportedLookAndFeel.getDefault()) {
            installSelectedAppearance(SupportedLookAndFeel.getDefault());
         }
      }
   }

   private static void configureEtlForgeUiResources() {
      UiResources var0 = UiResources.getDefault();
      var0.putFont(DayOfTheWeekCheckBoxPanel.FONT_RESOURCE_KEY, TextStyles.DefaultTextSmall.getFont());
      var0.putFont(ItemRenamer.FONT_RESOURCE_KEY, TextStyles.TreeLeafText.getFont());
      var0.putFont(TimeOfTheDayControl.FONT_RESOURCE_KEY, TextStyles.DefaultComboBoxText.getFont());
   }

   private static void configureEtlForgeTooltips() {
      ToolTipManager var0 = ToolTipManager.sharedInstance();
      var0.setReshowDelay(0);
   }

   public static boolean isNimbus() {
      return SupportedLookAndFeel.NIMBUS.getClassName().equals(UIManager.getLookAndFeel().getClass().getName());
   }

   public static boolean isSystemLookAndFeel() {
      return USE_SYSTEM_LF;
   }

   public static void makeOver(JComboBox var0) {
      TextStyles.DefaultComboBoxText.makeOver(var0);
   }

   public static void makeOver(JList var0) {
      if (IS_BOLD_METAL) {
         TextStyles.DefaultListText.makeOver(var0);
      }
   }

   public static void makeOver(JTable var0) {
      if (IS_BOLD_METAL) {
         TextStyles.DefaultText.makeOver(var0);
      }
   }

   public static void customizeWindowsOptionPaneButtons() {
      UIManager.put("OptionPane.buttonOrientation", 4);
      UIManager.put("OptionPane.buttonPadding", 10);
   }

   public static void customizeWindowsToolTipUI() {
      UIManager.put("ToolTip.background", new Color(255, 255, 225));
   }

   public static void customizeWindowsLabelUI() {
      UIManager.put("LabelUI", "com.sun.java.swing.plaf.windows.WindowsLabelUI");
   }

   public static void customizeWindowsButtonUI() {
      if (USE_WINDOWS_BUTTONS_ON_WINDOWS) {
         UIManager.put("ButtonUI", "com.sun.java.swing.plaf.windows.WindowsButtonUI");
      }
   }

   public static void customizeWindowsToolbarUI() {
      if (CUSTOMIZE_WINDOWS_TOOLBAR) {
         UIManager.put("ToolBarUI", "com.sun.java.swing.plaf.windows.WindowsToolBarUI");
         UIManager.put("ToolBarSeparatorUI", "com.sun.java.swing.plaf.windows.WindowsToolBarSeparatorUI");
      }
   }

   public static void customizeWindowsTextComponentUIs() {
      UIManager.put("TextField.margin", new InsetsUIResource(2, 2, 2, 2));
      UIManager.put("PasswordField.margin", new InsetsUIResource(2, 2, 2, 2));
      UIManager.put("TextField.font", TextStyles.WindowsText.getFont());
      UIManager.put("TextArea.font", TextStyles.WindowsText.getFont());
   }

   public static void customizeNonWindowsTextComponentUIs() {
      UIManager.put("TextField.font", TextStyles.DefaultText.getFont());
      UIManager.put("TextArea.font", TextStyles.DefaultText.getFont());
   }

   public static void customizeComboBoxUI() {
      UIManager.put("ComboBox.background", Color.WHITE);
      String var0;
      if (org.apache.commons.lang.SystemUtils.IS_OS_WINDOWS && USE_SYSTEM_LF) {
         var0 = "org.etlforge.ui.widget.combobox.KongaWindowsComboBoxUI";
      } else {
         var0 = "org.etlforge.ui.widget.combobox.KongaMetalComboBoxUI";
      }

      UIManager.put("ComboBoxUI", var0);
   }

   public static void customizeMenuUI() {
      UIManager.put("Menu.font", TextStyles.DefaultMenuText.getFont());
      UIManager.put("Menu.background", Color.WHITE);
      UIManager.put("MenuItem.font", TextStyles.DefaultMenuText.getFont());
      UIManager.put("MenuItem.background", Color.WHITE);
      UIManager.put("menu", Color.WHITE);
   }

   public static void customizeCheckBoxMenuItemUI() {
      UIManager.put("CheckBoxMenuItem.font", TextStyles.DefaultMenuText.getFont());
      UIManager.put("CheckBoxMenuItem.background", Color.WHITE);
   }

   public static void customizeRadioButtonMenuItemUI() {
      UIManager.put("RadioButtonMenuItem.font", TextStyles.DefaultMenuText.getFont());
      UIManager.put("RadioButtonMenuItem.background", Color.WHITE);
   }

   public static void customizePopupMenuUI() {
      UIManager.put("PopupMenu.font", TextStyles.DefaultMenuText.getFont());
      UIManager.put("PopupMenu.background", Color.WHITE);
   }

   public static void customizeTabbedPaneUI() {
      UIManager.put("TabbedPane.font", TextStyles.DefaultText.getFont());
   }

   static {
      ReadOnlyProperties var0 = StyleProperties.getInstance();
      USE_SYSTEM_LF = shouldUseSystemAppearance(var0);
      IS_BOLD_METAL = !USE_SYSTEM_LF && Boolean.getBoolean("swing.boldMetal");
      USE_WINDOWS_BUTTONS_ON_WINDOWS = var0.getBoolean("UseWindowButtonsOnWindows", false) && !SystemUtils.IS_JAVA_1_6;
      CUSTOMIZE_WINDOWS_TOOLBAR = USE_WINDOWS_BUTTONS_ON_WINDOWS;
   }
}


