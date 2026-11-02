package org.etlforge.application.ui.look;

import java.awt.Color;
import javax.swing.JLabel;
import org.apache.commons.lang.SystemUtils;
import org.etlforge.ui.style.TextStyle;

public final class TextStyles {
   public static final String FONT_FAMILIES = "Dialog, Lucida-Console, Tahoma, Verdana, Geneva, Arial, Helvetica, sans-serif";
   public static final String FONT_FAMILIES_2 = "Verdana, Geneva, Arial, Helvetica, sans-serif";
   static final TextStyle WindowsText = new TextStyle("Tahoma, Verdana, Geneva, Arial, Helvetica, sans-serif", 12, 0, Colors.TEXT_FOREGROUND);
   public static final TextStyle DefaultText = getDefaultText();
   public static final TextStyle DefaultItalic = TextStyle.newStyle(DefaultText, 2);
   public static final TextStyle DefaultErrorText = TextStyle.newColor(DefaultText, Colors.ERROR_TEXT_COLOR);
   public static final TextStyle DefaultValidText = TextStyle.newColor(DefaultText, Colors.VALID_TEXT_COLOR);
   public static final TextStyle BlueWarningText = TextStyle.newColor(DefaultText, new Color(142, 193, 238));
   public static final TextStyle DefaultMonoSpaced = new TextStyle("Monospaced, Courier New, Courier", 12, 0, Colors.TEXT_FOREGROUND);
   public static final TextStyle LargeMonoSpaced = TextStyle.adjustSize(DefaultMonoSpaced, 2);
   public static final TextStyle DefaultTextSmall = TextStyle.adjustSize(DefaultText, -1);
   public static final TextStyle DefaultBold = TextStyle.newStyle(DefaultText, 1);
   public static final TextStyle DefaultBoldLarger = TextStyle.adjustSize(DefaultBold, 1);
   public static final TextStyle DefaultBoldItalic = TextStyle.newStyle(DefaultText, 3);
   public static final TextStyle SystemOutText = TextStyle.newColor(DefaultMonoSpaced, new Color(142, 193, 238));
   public static final TextStyle SystemErrText = TextStyle.newColor(DefaultMonoSpaced, Color.RED);
   public static final TextStyle DefaultText2 = new TextStyle("Verdana, Geneva, Arial, Helvetica, sans-serif", 12, 0, Colors.TEXT_FOREGROUND);
   public static final TextStyle DefaultComboBoxText = DefaultText;
   public static final TextStyle DefaultCheckBoxText = DefaultText;
   public static final TextStyle DefaultRadioButtonText = DefaultText;
   public static final TextStyle DefaultListText = DefaultText;
   public static final TextStyle SmallListText = TextStyle.adjustSize(DefaultListText, -1);
   public static final TextStyle DefaultTableText = TextStyle.adjustSize(DefaultText2, -1);
   public static final TextStyle DefaultMenuText = DefaultText;
   public static final TextStyle InputLabelText = DefaultText;
   public static final TextStyle CaptionText = LookAndFeelCustomizer.isSystemLookAndFeel() ? DefaultBold : TextStyle.adjustSize(DefaultBold, -1);
   public static final TextStyle InfoText = new TextStyle("Dialog, Lucida-Console, Tahoma, Verdana, Geneva, Arial, Helvetica, sans-serif", 12, 0, Colors.TEXT_FOREGROUND);
   public static final TextStyle LargeInfoText = TextStyle.adjustSize(InfoText, 2);
   public static final TextStyle ErrorInfoText = TextStyle.newColor(InfoText, Colors.ERROR_TEXT_COLOR);
   public static final TextStyle SmallInfoText = DefaultTextSmall;
   public static final TextStyle AdminHint = new TextStyle("Verdana, Geneva, Arial, Helvetica, sans-serif", 12, 0, Colors.THEME_TEXT_COLOR);
   public static final TextStyle SmallAdminHint = TextStyle.adjustSize(AdminHint, -1);
   public static final TextStyle AdminHintBold = TextStyle.newStyle(AdminHint, 1);
   public static final TextStyle SmallAdminHintBold = TextStyle.newStyle(SmallAdminHint, 1);
   public static final TextStyle BigAdminHintBold = TextStyle.newSize(AdminHintBold, 15);
   public static final TextStyle InfoTextHeader = BigAdminHintBold;
   public static final TextStyle GenericEditorTitleMain = new TextStyle("Verdana, Geneva, Arial, Helvetica, sans-serif", 13, 1, Colors.THEME_TEXT_COLOR);
   public static final TextStyle GenericEditorTitleSub = new TextStyle("Verdana, Geneva, Arial, Helvetica, sans-serif", 12, 3, Colors.TEXT_FOREGROUND);
   public static final TextStyle TopPanelCaptionText = TextStyle.adjustSize(DefaultBold, -1);
   public static final TextStyle TopPanelPlainText = DefaultTextSmall;
   public static final TextStyle ComponentTableHeaderText = DefaultBold;
   public static final TextStyle ComponentTableEntryText = DefaultText;
   public static final TextStyle EntityPageTableHeaderText = TextStyle.newColor(DefaultBold, Color.WHITE);
   public static final TextStyle DropDownPanelTitle = TextStyle.adjustSize(BigAdminHintBold, 1);
   public static final TextStyle SmallCaption = AdminHint;
   public static final TextStyle SmallBoldCaption = TextStyle.newStyle(SmallCaption, 1);
   public static final TextStyle MediumCaption = TextStyle.adjustSize(SmallCaption, 2);
   public static final TextStyle MediumBoldCaption = TextStyle.newStyle(MediumCaption, 1);
   public static final TextStyle BigCaption = TextStyle.adjustSize(MediumCaption, 2);
   public static final TextStyle TreeRootText = new TextStyle("Verdana, Geneva, Arial, Helvetica, sans-serif", 12, 0, Colors.TEXT_FOREGROUND);
   public static final TextStyle TreeLeafText = new TextStyle("Verdana, Geneva, Arial, Helvetica, sans-serif", 11, 0);
   public static final TextStyle TreeFolderText = TreeLeafText;
   public static final TextStyle TreeRootFolderText = new TextStyle("Verdana, Geneva, Arial, Helvetica, sans-serif", 12, 1, Colors.TEXT_FOREGROUND);
   static final TextStyle TreeDeployDirtyNodeText = TreeLeafText;
   static final TextStyle TreeDeployDirtyRootFolderText = TreeRootFolderText;
   static final TextStyle TreeDeployDirtyRootText = TreeRootText;
   public static final TextStyle TreeTitleFocusedText = DefaultText;
   public static final TextStyle TreeTitleUnfocusedText = DefaultText;
   public static final TextStyle DefaultTitleBorderText = TextStyle.newColor(DefaultBold, Colors.THEME_TEXT_COLOR);

   private static TextStyle getDefaultText() {
      if (LookAndFeelCustomizer.isSystemLookAndFeel()) {
         return new TextStyle(new JLabel("abc"));
      } else {
         return SystemUtils.IS_OS_WINDOWS
            ? WindowsText
            : new TextStyle("Dialog, Lucida-Console, Tahoma, Verdana, Geneva, Arial, Helvetica, sans-serif", 12, 0, Colors.TEXT_FOREGROUND);
      }
   }

   private TextStyles() {
   }
}

