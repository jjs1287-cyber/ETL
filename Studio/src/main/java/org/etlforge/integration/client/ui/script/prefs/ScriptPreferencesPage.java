package org.etlforge.integration.client.ui.script.prefs;

import javax.swing.JLabel;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.application.ui.prefs.AbstractPreferencesPage;
import org.etlforge.application.ui.prefs.BooleanPreferenceUi;
import org.etlforge.application.ui.prefs.IntPreferenceUi;
import org.etlforge.application.ui.prefs.PreferenceUi;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.Grid;

public final class ScriptPreferencesPage extends AbstractPreferencesPage {
   private final PreferenceUi layoutTypePreference = this.createLayoutTypePref();
   private final PreferenceUi dataElementListPreference = new BooleanPreferenceUi(
      ShowReferencedDataElementsOnlyPreference.INSTANCE, "Show only those data elements that are referenced in the project in the Formula Builder"
   );
   private final PreferenceUi dataElementCompletionPreference = new BooleanPreferenceUi(
      UseReferencedDataElementsOnlyInAutoCompletion.INSTANCE, "Include only those data elements that are referenced in the project in auto-completion"
   );
   private final PreferenceUi autoSuggestFuncArgsPreference = AutoSuggestFunctionArgumentsPreference.createUi();
   private final PreferenceUi tabSizePreference = this.createTabSizePrefUi();
   private final PreferenceUi autoAssistParenthesisPreference = new BooleanPreferenceUi(AutoAssistParenthesisPreference.INSTANCE, "Opening parenthesis");
   private final PreferenceUi autoAssistDoubleQuotePreference = new BooleanPreferenceUi(AutoAssistDoubleQuotesPreference.INSTANCE, "Double-quote");
   private final PreferenceUi autoAssistSingleQuotePreference = new BooleanPreferenceUi(AutoAssistSingleQuotesPreference.INSTANCE, "Single-quote");

   public ScriptPreferencesPage() {
      super(new String[]{"Scripts"});
      this.addControls(
         this.layoutTypePreference,
         this.dataElementListPreference,
         this.dataElementCompletionPreference,
         this.autoSuggestFuncArgsPreference,
         this.tabSizePreference,
         this.autoAssistParenthesisPreference,
         this.autoAssistDoubleQuotePreference,
         this.autoAssistSingleQuotePreference
      );
   }

   private PreferenceUi createLayoutTypePref() {
      return LayoutTypePreferenceUiFactory.build(
         "Script Builder Layout",
         "<html>Controls the layout of the script builder.<br>Changing this preference does not affect script pages that are already open.</html>"
      );
   }

   private IntPreferenceUi createTabSizePrefUi() {
      IntPreferenceUi var1 = new IntPreferenceUi(TabSizePreference.instance(), "Tab size (does not affect formula builders that are currently open):", 3);
      var1.setSelectAllWhenFocused(true);
      return var1;
   }

   @Override
   protected void layoutControls(UiCanvas var1) {
      this.layoutTypePreference.decorate(var1);
      this.dataElementListPreference.decorate(var1);
      this.dataElementCompletionPreference.decorate(var1);
      this.autoSuggestFuncArgsPreference.decorate(var1);
      this.tabSizePreference.decorate(var1);
      this.getAutoAssistPreferencesLayout().decorate(var1);
   }

   private UiProvider getAutoAssistPreferencesLayout() {
      Grid var1 = Grid.oneColumn(10).withBorder(StyleSheet.getDefaultTitleBorder("Auto-Assist"));
      var1.add("Automatically insert matching character when typing:");
      var1.addAll(this.autoAssistParenthesisPreference, this.autoAssistDoubleQuotePreference, this.autoAssistSingleQuotePreference);
      JLabel var2 = new JLabel("Changes do not affect currently open Scripts and Transformations", CommonIcons.INFO_16, 10);
      var1.add(var2);
      return var1;
   }
}
