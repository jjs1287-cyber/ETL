package org.etlforge.application.ui.look;

import com.google.common.base.Throwables;
import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import org.apache.commons.lang.SystemUtils;

public enum SupportedLookAndFeel {
   SYSTEM {
      @Override
      protected String getClassName() {
         return UIManager.getSystemLookAndFeelClassName();
      }

      @Override
      protected void customize() {
         if (SystemUtils.IS_OS_WINDOWS && !SystemUtils.IS_OS_WINDOWS_VISTA) {
            UIManager.put("Panel.background", new ColorUIResource(238, 238, 238));
            UIManager.put("Button.background", new ColorUIResource(238, 238, 238));
            UIManager.put("CheckBox.background", new ColorUIResource(238, 238, 238));
            UIManager.put("MenuBar.background", new ColorUIResource(238, 238, 238));
            UIManager.put("OptionPane.background", new ColorUIResource(238, 238, 238));
            UIManager.put("RadioButton.background", new ColorUIResource(238, 238, 238));
            UIManager.put("ScrollPane.background", new ColorUIResource(238, 238, 238));
            UIManager.put("SplitPane.background", new ColorUIResource(238, 238, 238));
            UIManager.put("TabbedPane.background", new ColorUIResource(238, 238, 238));
            UIManager.put("ToolBar.background", new ColorUIResource(238, 238, 238));
            UIManager.put("Viewport.background", new ColorUIResource(238, 238, 238));
            UIManager.put("konga.tableheader.background", new ColorUIResource(238, 238, 238));
            UIManager.put("konga.tableheader.foreground", new ColorUIResource(Color.BLACK));
         }
      }

      @Override
      protected String getNarrowTableStyle() {
         if (SystemUtils.IS_OS_WINDOWS_VISTA) {
            return "org.etlforge.application.ui.look.WindowsVistaNarrowTableStyle";
         } else {
            return SystemUtils.IS_OS_WINDOWS
               ? "org.etlforge.application.ui.look.WindowsNarrowTableStyle"
               : "org.etlforge.application.ui.look.CrossPlatformNarrowTableStyle";
         }
      }

      @Override
      protected String getNormalTableStyle() {
         if (SystemUtils.IS_OS_WINDOWS_VISTA) {
            return "org.etlforge.application.ui.look.WindowsVistaNormalTableStyle";
         } else {
            return SystemUtils.IS_OS_WINDOWS
               ? "org.etlforge.application.ui.look.WindowsNormalTableStyle"
               : "org.etlforge.application.ui.look.CrossPlatformNormalTableStyle";
         }
      }

      @Override
      protected String getWideTableStyle() {
         if (SystemUtils.IS_OS_WINDOWS_VISTA) {
            return "org.etlforge.application.ui.look.WindowsVistaWideTableStyle";
         } else {
            return SystemUtils.IS_OS_WINDOWS
               ? "org.etlforge.application.ui.look.WindowsWideTableStyle"
               : "org.etlforge.application.ui.look.CrossPlatformWideTableStyle";
         }
      }
   },
   CROSS_PLATFORM {
      @Override
      protected String getClassName() {
         return UIManager.getCrossPlatformLookAndFeelClassName();
      }

      @Override
      protected void customize() {
         if ("Metal".equals(UIManager.getLookAndFeel().getName())) {
            if (SystemUtils.IS_OS_WINDOWS) {
               if (!LookAndFeelCustomizer.IS_BOLD_METAL) {
                  LookAndFeelCustomizer.customizeWindowsLabelUI();
                  LookAndFeelCustomizer.customizeWindowsButtonUI();
               }

               LookAndFeelCustomizer.customizeWindowsToolbarUI();
               LookAndFeelCustomizer.customizeWindowsToolTipUI();
               LookAndFeelCustomizer.customizeWindowsTextComponentUIs();
            } else {
               LookAndFeelCustomizer.customizeNonWindowsTextComponentUIs();
            }

            LookAndFeelCustomizer.customizeMenuUI();
            LookAndFeelCustomizer.customizeCheckBoxMenuItemUI();
            LookAndFeelCustomizer.customizeRadioButtonMenuItemUI();
            LookAndFeelCustomizer.customizePopupMenuUI();
            LookAndFeelCustomizer.customizeTabbedPaneUI();
            UIManager.put("konga.tableheader.background", new ColorUIResource(54, 100, 160));
            UIManager.put("konga.tableheader.foreground", new ColorUIResource(Color.WHITE));
         }
      }

      @Override
      protected String getNarrowTableStyle() {
         return "org.etlforge.application.ui.look.CrossPlatformNarrowTableStyle";
      }

      @Override
      protected String getNormalTableStyle() {
         return "org.etlforge.application.ui.look.CrossPlatformNormalTableStyle";
      }

      @Override
      protected String getWideTableStyle() {
         return "org.etlforge.application.ui.look.CrossPlatformWideTableStyle";
      }
   },
   NIMBUS {
      @Override
      public boolean isSupported() {
         try {
            Class.forName(this.getClassName());
            return true;
         } catch (ClassNotFoundException var2) {
            return false;
         }
      }

      @Override
      protected String getClassName() {
         return "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";
      }

      @Override
      protected void customize() {
         UIManager.getLookAndFeelDefaults().put("Button.defaultButtonFollowsFocus", Boolean.TRUE);
         UIManager.put("etlforge.theme.text.color", UIManager.getColor("nimbusInfoBlue"));
         UIManager.put("konga.overlaydecorator.innerborder", new ColorUIResource(UIManager.getColor("nimbusBorder")));
         UIManager.put("konga.overlaydecorator.outerborder", new ColorUIResource(Colors.ETLFORGE_BLUE));
         UIManager.put("etlforge.mappingtree.folderColor", Color.BLACK);
         UIManager.put("etlforge.mappingtree.attributeColor", Color.BLACK);
         UIManager.put("etlforge.mappingtree.preconditionColor", UIManager.getColor("nimbusInfoBlue"));
         UIManager.put("nimbusSelectionBackground", new ColorUIResource(Colors.ETLFORGE_BLUE));
      }

      @Override
      protected String getNarrowTableStyle() {
         return "org.etlforge.application.ui.look.NimbusNarrowTableStyle";
      }

      @Override
      protected String getNormalTableStyle() {
         return "org.etlforge.application.ui.look.NimbusNormalTableStyle";
      }

      @Override
      protected String getWideTableStyle() {
         return "org.etlforge.application.ui.look.NimbusWideTableStyle";
      }
   };

   private static SupportedLookAndFeel installed;

   SupportedLookAndFeel() {
   }

   public boolean isInstalled() {
      return installed == this;
   }

   public final void install() {
      installed = null;

      try {
         UIManager.setLookAndFeel(this.getClassName());
         this.customize();
         this.installTableStyles();
         this.installOtherProperties();
         installed = this;
      } catch (Exception var2) {
         throw Throwables.propagate(var2);
      }
   }

   private void installTableStyles() {
      UIManager.put("konga.tablestyle.narrow", this.getNarrowTableStyle());
      UIManager.put("konga.tablestyle.normal", this.getNormalTableStyle());
      UIManager.put("konga.tablestyle.wide", this.getWideTableStyle());
   }

   private void installOtherProperties() {
      UIManager.put("konga.overlaydecorator.caption-style", TextStyles.BigCaption);
      UIManager.put("konga.dropdownpanel.title-style", TextStyles.DropDownPanelTitle);
      UIManager.put("konga.tablestyle.font", TextStyles.DefaultTableText.getFont());
   }

   protected abstract String getClassName();

   protected abstract String getNarrowTableStyle();

   protected abstract String getNormalTableStyle();

   protected abstract String getWideTableStyle();

   protected void customize() {
   }

   public static void installDefault() {
      SupportedLookAndFeel var0 = getDefault();
      var0.install();
   }

   public static SupportedLookAndFeel getDefault() {
      for (SupportedLookAndFeel var2 : preferredOrder()) {
         if (var2.isSupported()) {
            return var2;
         }
      }

      return CROSS_PLATFORM;
   }

   private static List<SupportedLookAndFeel> preferredOrder() {
      return SystemUtils.IS_OS_MAC ? Arrays.asList(SYSTEM, NIMBUS, CROSS_PLATFORM) : Arrays.asList(NIMBUS, SYSTEM, CROSS_PLATFORM);
   }

   public static SupportedLookAndFeel forClassName(String var0) {
      for (SupportedLookAndFeel var4 : values()) {
         if (var4.getClassName().equals(var0)) {
            return var4;
         }
      }

      return null;
   }

   public boolean isSupported() {
      return true;
   }
}
