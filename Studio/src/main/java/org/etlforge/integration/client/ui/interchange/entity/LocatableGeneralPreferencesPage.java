package org.etlforge.integration.client.ui.interchange.entity;

import java.awt.Component;
import javax.swing.JLabel;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.prefs.AbstractPreferencesPage;
import org.etlforge.application.ui.prefs.FixedSetPreferenceUi;
import org.etlforge.application.ui.widget.AutoCompletionPreference;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.text.contentassist.AutoCompletionMode;
import org.etlforge.util.html.HtmlListGenerator;
import org.etlforge.util.html.HtmlStyleTag;

public final class LocatableGeneralPreferencesPage extends AbstractPreferencesPage {
   public static final String PATH = "Sources & Targets";
   private final LocatableGeneralPreferencesPage.AutoCompletionPreferenceUi autoCompletionUi = new LocatableGeneralPreferencesPage.AutoCompletionPreferenceUi();

   public LocatableGeneralPreferencesPage() {
      super(new String[]{"Sources & Targets"});
      this.addControls(this.autoCompletionUi);
   }

   private static class AutoCompletionPreferenceUi extends FixedSetPreferenceUi<AutoCompletionMode> {
      public AutoCompletionPreferenceUi() {
         super(AutoCompletionPreference.instance(), "", false);
      }

      @Override
      public void decorate(UiCanvas var1) {
         final OneColumnPanel var2 = new OneColumnPanel().withInsets(5, 0, 5, 0).withBorder(StyleSheet.getDefaultTitleBorder("Auto Completion"));
         var2.setWeightX(1.0);
         var2.add(this.getCaption());
         super.decorate(new UiCanvas() {
            @Override
            public void display(Component var1) {
               var2.noFill(var1);
            }
         });
         var2.decorate(var1);
      }

      private JLabel getCaption() {
         StringBuilder var1 = new StringBuilder("<html><table width=400px>");
         var1.append(
            "Most fields in the Source and Target pages provide auto-completion based on previous values for those fields. There are two modes of auto-completion to choose between:"
         );
         var1.append("<br>");
         HtmlListGenerator var2 = HtmlListGenerator.unordered();
         var2.add("<code>" + AutoCompletionMode.AUTOMATIC.toString() + "</code>: ", HtmlStyleTag.BOLD);
         var2.add(
            "When typing in a field, a list of matching values is automatically displayed under the field. You can navigate through the list using the arrow keys on your keyboard. Use <code>Enter</code> or <code>Tab</code> to select a value from the list."
         );
         var2.newItem();
         var2.add("<code>" + AutoCompletionMode.CODE_EDITOR.toString() + "</code>: ", HtmlStyleTag.BOLD);
         var2.add("Auto-completion is triggered by hitting <code>CTRL-SPACE</code> in the field (same as in the Formula Builder).");
         var1.append(var2.toHtml());
         var1.append(HtmlStyleTag.BOLD.wrap("Note: "));
         var1.append("Changes in this preference will not take effect in pages that are alredy open.");
         var1.append("</table></html>");
         return TextStyles.InfoText.makeLabel(var1.toString());
      }
   }
}
