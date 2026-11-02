package org.etlforge.application.ui.filestore;

import org.etlforge.application.filestore.FileCollisionResolution;
import org.etlforge.ui.util.option.Option;
import org.etlforge.ui.util.option.OptionSelector;

interface ResolutionOption extends Option {
   ResolutionOption USE_EXISTING = new ResolutionOption() {
      @Override
      public FileCollisionResolution.Action getAction() {
         return FileCollisionResolution.Action.USE_EXISTING;
      }

      @Override
      public void decorate(OptionSelector var1) {
         var1.setText(Strings.get("FileStore.Resolver.Option.UseExisting"));
      }
   };
   ResolutionOption REPLACE_EXISTING = new ResolutionOption() {
      @Override
      public FileCollisionResolution.Action getAction() {
         return FileCollisionResolution.Action.REPLACE_EXISTING;
      }

      @Override
      public void decorate(OptionSelector var1) {
         var1.setText(Strings.get("FileStore.Resolver.Option.Replace"));
      }
   };
   ResolutionOption RENAME_NEW = new ResolutionOption() {
      @Override
      public FileCollisionResolution.Action getAction() {
         return FileCollisionResolution.Action.RENAME_AND_USE_NEW;
      }

      @Override
      public void decorate(OptionSelector var1) {
         var1.setText(Strings.get("FileStore.Resolver.Option.Rename"));
      }
   };

   FileCollisionResolution.Action getAction();
}
