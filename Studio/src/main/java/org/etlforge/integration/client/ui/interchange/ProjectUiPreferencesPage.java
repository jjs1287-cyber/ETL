package org.etlforge.integration.client.ui.interchange;

import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.application.ui.prefs.AbstractPreferencesPage;
import org.etlforge.application.ui.prefs.BooleanPreferenceUi;
import org.etlforge.application.ui.prefs.FixedSetPreferenceUi;
import org.etlforge.integration.client.ui.interchange.component.tree.ShowOperationContentsInTreePreference;
import org.etlforge.integration.client.ui.interchange.content.ProjectContentViewerPreference;
import org.etlforge.integration.client.ui.interchange.content.ProjectContentViewerType;
import org.etlforge.integration.client.ui.interchange.core.action.ConfirmDeletePreference;
import org.etlforge.integration.client.ui.project.trashcan.ReviveDependenciesWarningPreference;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.OneColumnPanel;

public class ProjectUiPreferencesPage extends AbstractPreferencesPage {
   public ProjectUiPreferencesPage() {
      super(new String[]{"Project", "UI"});
      this.addControls(
         new ProjectUiPreferencesPage.ContentViewerTypePreferenceUi(),
         new BooleanPreferenceUi(
            ShowOperationContentsInTreePreference.INSTANCE, "Show the contents of an operation as child nodes under the operation node in the project tree"
         ),
         new BooleanPreferenceUi(ConfirmDeletePreference.instance(), "Confirm before processing a delete request"),
         new BooleanPreferenceUi(ReviveDependenciesWarningPreference.instance(), "Ask before reviving required items from trash can")
      );
   }

   private static class ContentViewerTypePreferenceUi extends FixedSetPreferenceUi<ProjectContentViewerType> {
      public ContentViewerTypePreferenceUi() {
         super(ProjectContentViewerPreference.INSTANCE, "", false);
      }

      protected String getDisplayString(ProjectContentViewerType var1) {
         switch (var1) {
            case TREE:
               return "Traditional tree";
            case MULTI_TREE:
               return "Multi-tree";
            default:
               throw new IllegalStateException("Unexpected type: " + var1);
         }
      }

      @Override
      public void decorate(UiCanvas var1) {
         OneColumnPanel var2 = new OneColumnPanel();
         var2.setWeightX(1.0);
         super.decorate(var2);
         var2.withBorder(StyleSheet.getDefaultTitleBorder("Project Content Viewer Style")).decorate(var1);
      }
   }
}
