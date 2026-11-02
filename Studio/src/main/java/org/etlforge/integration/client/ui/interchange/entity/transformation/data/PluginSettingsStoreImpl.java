package org.etlforge.integration.client.ui.interchange.entity.transformation.data;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import org.etlforge.application.VersionFactory;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.entity.settings.EntitySettings;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.loadsource.client.LoadSourcePluginIdentifier;
import org.etlforge.integration.loadsource.client.PluginSettingsStore;
import org.etlforge.integration.plugin.PluginIdentifier;
import org.etlforge.integration.plugin.pipeline.PipelinePosition;
import org.etlforge.integration.plugin.pipeline.RelativePosition;
import org.etlforge.util.persist.Persistor;
import org.etlforge.util.version.Version;

final class PluginSettingsStoreImpl implements PluginSettingsStore {
   private static final String ROOT = "LoadSourcePlugins";
   private static final String ENTRY = "PipelinePosition";
   private static final String NAME = "name";
   private static final String VERSION = "version";
   private static final String POSITION = "pos";
   private final Transformation transformation;
   private final EntitySettings settings;

   public PluginSettingsStoreImpl(Transformation var1, EntitySettings var2) {
      this.transformation = var1;
      this.settings = var2;
   }

   @Override
   public LoadSourcePluginIdentifier[] load() {
      Persistor var1 = this.settings.getEntitySettings(this.transformation).getFirstChild("LoadSourcePlugins");
      return var1 == null ? new LoadSourcePluginIdentifier[0] : this.loadImpl(var1);
   }

   private LoadSourcePluginIdentifier[] loadImpl(Persistor var1) {
      ArrayList var2 = Lists.newArrayList();

      for (Persistor var4 : var1.getChildren("PipelinePosition")) {
         try {
            String var5 = var4.getString("name");
            Version var6 = VersionFactory.fromString(var4.getString("version"));
            RelativePosition var7 = RelativePosition.valueOf(var4.getString("pos"));
            var2.add(new LoadSourcePluginIdentifier(new PluginIdentifier(var5, var6), var7));
         } catch (Exception var8) {
            ErrorLog.log(PluginSettingsStoreImpl.class, "Invalid plugin entry found in the Transformation settings", var8);
         }
      }

      return (LoadSourcePluginIdentifier[]) var2.toArray(new LoadSourcePluginIdentifier[var2.size()]);
   }

   @Override
   public void store(PipelinePosition[] var1) {
      Persistor var2 = this.settings.getEntitySettings(this.transformation);
      Persistor var3 = var2.getFirstChild("LoadSourcePlugins");
      if (var3 == null) {
         var3 = var2.createChild("LoadSourcePlugins");
      }

      var3.removeChildren("PipelinePosition");

      for (PipelinePosition var7 : var1) {
         Persistor var8 = var3.createChild("PipelinePosition");
         var8.putString("name", var7.getPluginId().getName());
         var8.putString("version", var7.getPluginId().getVersion().toString());
         var8.putString("pos", var7.getRelativePosition().name());
      }

      this.settings.persist();
   }
}
