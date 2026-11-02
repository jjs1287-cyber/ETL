package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.prefs;

import org.etlforge.application.ui.prefs.AbstractPreferencesPage;
import org.etlforge.application.ui.prefs.BooleanPreferenceUi;
import org.etlforge.application.ui.prefs.FixedSetPreferenceUi;
import org.etlforge.application.ui.prefs.PreferenceUi;
import org.etlforge.application.ui.prefs.PreferenceUiWrapper;
import org.etlforge.application.ui.prefs.TitledPreferenceUi;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.UndoRedoMode;

public final class OperationGraphPreferencesPage extends AbstractPreferencesPage {
   private static final String[] PATH = new String[]{"Operations", "Graph"};

   public static String[] PATH() {
      return (String[])PATH.clone();
   }

   public OperationGraphPreferencesPage() {
      super(PATH);
      this.addControls(this.createEdgeTypePref(), new OperationGraphPreferencesPage.UndoRedoModePreferenceUi(), this.createRenamePref());
   }

   private BooleanPreferenceUi createRenamePref() {
      return new BooleanPreferenceUi(AutomaticRenamePreference.INSTANCE, Strings.get("Prefs.AutomaticRename"));
   }

   private PreferenceUi createEdgeTypePref() {
      FixedSetPreferenceUi var1 = new FixedSetPreferenceUi<>(EdgeTypePreference.instance(), "", false);
      return new TitledPreferenceUi(var1, Strings.get("Prefs.EdgeType.Title"), Strings.get("Prefs.EdgeType.Caption"));
   }

   private static String appendNotAffectingOpenGraphsMessage(String var0) {
      return "<html>" + var0 + "<br><br>" + Strings.get("Prefs.OpenGraphsNotAffected") + "</html>";
   }

   private class UndoRedoModePreferenceUi extends PreferenceUiWrapper {
      public UndoRedoModePreferenceUi() {
         super(new FixedSetPreferenceUi<UndoRedoMode>(UndoRedoModePreference.INSTANCE, "", false) {
            protected String getDisplayString(UndoRedoMode var1) {
               return Strings.get("Prefs.UndoRedoMode." + var1.name());
            }
         });
         this.getLayout().setTitle(this.getTitle()).setCaption(this.getCaption());
      }

      private String getTitle() {
         return Strings.get("Prefs.UndoRedoMode.Title");
      }

      private String getCaption() {
         return OperationGraphPreferencesPage.appendNotAffectingOpenGraphsMessage(Strings.get("Prefs.UndoRedoMode.Caption"));
      }
   }
}
