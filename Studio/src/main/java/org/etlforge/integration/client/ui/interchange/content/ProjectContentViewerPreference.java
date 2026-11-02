package org.etlforge.integration.client.ui.interchange.content;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.EnumPreference;
import org.etlforge.application.prefs.PreferenceFactory;

public final class ProjectContentViewerPreference {
   public static final EnumPreference<ProjectContentViewerType> INSTANCE = createPref();

   public static ProjectContentViewerType get() {
      return INSTANCE.get();
   }

   private static EnumPreference<ProjectContentViewerType> createPref() {
      PreferenceFactory var0 = ApplicationConfiguration.getPreferenceFactory();
      String[] var1 = new String[]{"Project"};
      String var2 = "ProjectContentViewer";
      ProjectContentViewerType var3 = ProjectContentViewerType.MULTI_TREE;
      return var0.newEnumPreference(var1, var2, var3, ProjectContentViewerType.class);
   }

   private ProjectContentViewerPreference() {
   }
}
