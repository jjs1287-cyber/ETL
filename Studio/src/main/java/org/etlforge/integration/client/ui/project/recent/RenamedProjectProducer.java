package org.etlforge.integration.client.ui.project.recent;

import java.util.concurrent.Callable;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.ask.Answer;
import org.etlforge.application.ui.ask.Ask;
import org.etlforge.integration.client.project.ProjectLocation;
import org.etlforge.integration.client.project.rename.ProjectLocationRenamer;
import org.etlforge.integration.client.project.rename.ProjectNameConflictResolver;

final class RenamedProjectProducer implements Callable<ProjectLocation> {
   private final ProjectLocation location;
   private final String newName;

   public RenamedProjectProducer(ProjectLocation var1, String var2) {
      this.location = var1;
      this.newName = var2;
   }

   public ProjectLocation call() throws Exception {
      ProjectLocationRenamer var1 = new ProjectLocationRenamer(this.location);
      var1.setConflictResolver(new RenamedProjectProducer.NameCollisionHandler());
      return var1.rename(this.newName);
   }

   private static final class NameCollisionHandler implements ProjectNameConflictResolver {
      private NameCollisionHandler() {
      }

      @Override
      public ProjectNameConflictResolver.Resolution resolve(ProjectLocation var1) {
         String var2 = Strings.format("Recent.Rename.Overwrite", var1.getLocationPath());
         Answer var3 = Ask.yesOrNo(null, var2, Strings.get("Recent.Rename.Overwrite.Title"), Alert.Option.NO, Alert.Type.WARNING);
         return var3.isYes() ? ProjectNameConflictResolver.Resolution.OVERWRITE : ProjectNameConflictResolver.Resolution.CANCEL;
      }
   }
}
