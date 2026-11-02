package org.etlforge.integration.client.ui.interchange.entity.transformation.prefs;

import com.google.common.base.Function;
import java.awt.Container;
import java.util.List;
import javax.swing.BorderFactory;
import org.etlforge.application.prefs.BooleanPreference;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.prefs.AbstractPreferencesPage;
import org.etlforge.application.ui.prefs.BooleanPreferenceUi;
import org.etlforge.application.ui.prefs.FixedSetPreferenceUi;
import org.etlforge.application.ui.prefs.PreferenceUi;
import org.etlforge.application.ui.prefs.TitledPreferenceUi;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.client.ui.interchange.component.menu.prefs.IncludeTransformationMenuPreference;
import org.etlforge.integration.client.ui.interchange.entity.transformation.data.RememberTreeMapperTestDataPreference;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.MapAreaDisplayOption;
import org.etlforge.integration.client.ui.script.prefs.LayoutTypePreferenceUiFactory;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.OneColumnPanel;

public final class TransformationPreferencesPage extends AbstractPreferencesPage {
   public static final String PATH = "Transformations";
   private final PreferenceUi includeTransformationMenuPref = this.createBooleanPreferenceUi(
      IncludeTransformationMenuPreference.INSTANCE, "Include a top-level Transformation menu in the main window"
   );
   private final PreferenceUi rememberTestDataPref = this.createBooleanPreferenceUi(
      RememberTreeMapperTestDataPreference.INSTANCE, "Remember Transformation test data when closing the page"
   );
   private final PreferenceUi formulaBuilderDisplayModePref = this.createFormulaBuilderDisplayModePref();
   private final PreferenceUi formulaBuilderLayoutPreference = this.createFormulaBuilderLayoutTypePref();
   private final PreferenceUi cancelFormulaBuilderOnEscapePref = this.createBooleanPreferenceUi(
      CancelFormulaBuilderOnEscapePreference.INSTANCE, "ESCAPE cancels the formula builder"
   );
   private final List<PreferenceUi> mapAreaDisplayPrefs = KongaListUtils.transform(
      MapAreaDisplayOption.values(), new TransformationPreferencesPage.ControlFactory()
   );

   public TransformationPreferencesPage() {
      super(new String[]{"Transformations"});
      this.addControls(this.mapAreaDisplayPrefs);
   }

   private PreferenceUi createBooleanPreferenceUi(BooleanPreference var1, String var2) {
      BooleanPreferenceUi var3 = new BooleanPreferenceUi(var1, var2);
      this.addControl(var3);
      return var3;
   }

   private PreferenceUi createFormulaBuilderDisplayModePref() {
      FixedSetPreferenceUi var1 = new FixedSetPreferenceUi<>(FormulaBuilderDisplayModePreference.INSTANCE, null, false);
      TitledPreferenceUi var2 = new TitledPreferenceUi(
         var1,
         "Formula Builder Display Mode",
         "<html>Controls how the formula builder is displayed.<br>Changing this preference does not affect transformation pages that are already open."
      );
      this.addControl(var2);
      return var2;
   }

   private PreferenceUi createFormulaBuilderLayoutTypePref() {
      PreferenceUi var1 = LayoutTypePreferenceUiFactory.build(
         "Formula Builder Layout",
         "<html>Controls the layout of the formula builder.<br>Changing this preference does not affect transformation pages that are already open.</html>"
      );
      this.addControl(var1);
      return var1;
   }

   @Override
   public void display(Container var1) {
      OneColumnPanel var2 = new OneColumnPanel().withInsets(5, 5, 10, 5).withBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)).setWeightX(1.0);
      var2.addAll(
         this.includeTransformationMenuPref,
         this.rememberTestDataPref,
         this.formulaBuilderDisplayModePref,
         this.formulaBuilderLayoutPreference,
         this.cancelFormulaBuilderOnEscapePref
      );
      new TransformationPreferencesPage.MapAreaPreferencesLayout(var2);
      BorderLayoutBuilder.from(var1).north(var2);
   }

   private static class ControlFactory implements Function<MapAreaDisplayOption, PreferenceUi> {
      private ControlFactory() {
      }

      public PreferenceUi apply(MapAreaDisplayOption var1) {
         return new BooleanPreferenceUi(var1.getPreference(), var1.toString());
      }
   }

   private class MapAreaPreferencesLayout {
      private final OneColumnPanel layout = new OneColumnPanel()
         .withInsets(0, 0, 10, 0)
         .withBorder(StyleSheet.getDefaultTitleBorder("Map Area Display Options"))
         .setWeightX(1.0);

      public MapAreaPreferencesLayout(OneColumnPanel var2) {
         this.addCaption();
         this.addControls();
         var2.add(this.layout);
      }

      private void addCaption() {
         this.layout.add(TextStyles.InfoText.makeLabel("Choose the display options to select by default:"));
      }

      private void addControls() {
         for (PreferenceUi var2 : TransformationPreferencesPage.this.mapAreaDisplayPrefs) {
            this.layout.add(var2);
         }
      }
   }
}
