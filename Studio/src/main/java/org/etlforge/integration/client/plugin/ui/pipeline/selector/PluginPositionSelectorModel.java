package org.etlforge.integration.client.plugin.ui.pipeline.selector;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.etlforge.integration.client.plugin.PluginDisplayNameLookup;
import org.etlforge.integration.client.plugin.pipeline.cache.PipelinePluginManifestCache;
import org.etlforge.integration.client.server.KnownServer;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.util.ServerComparators;
import org.etlforge.integration.common.server.ServerGuid;
import org.etlforge.integration.plugin.PluginIdentifier;
import org.etlforge.integration.plugin.pipeline.PipelinePosition;
import org.etlforge.integration.plugin.pipeline.RelativePosition;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.ListProperty;
import org.etlforge.util.Property;

public final class PluginPositionSelectorModel extends AbstractPropertyBasedModel {
   public static final String BUSY = "busy";
   public static final String KNOWN_SERVERS = "knownServers";
   public static final String SELECTED_SERVER = "selectedServer";
   public static final String PLUGIN_POSITIONS = "pluginPositions";
   public static final String SELECTED_PLUGIN_POSITIONS = "selectedPluginPositions";
   public static final String SELECTED_RELATIVE_POSITIONS = "selectedRelativePosition";
   private final ListProperty<ServerInfo> knownServers;
   private final Property<ServerInfo> selectedServer;
   private final ListProperty<PipelinePosition> pluginPositions;
   private Set<PipelinePosition> selectedPluginPositions;
   private final Property<Boolean> busy;
   private final Property<EnumSet<RelativePosition>> selectedRelativePositions;
   private PluginDisplayNameLookup nameLookup;
   private EnumSet<RelativePosition> supportedRelativePositions = EnumSet.allOf(RelativePosition.class);
   private final Map<ServerGuid, PipelinePluginManifestCache> caches;
   private boolean checkSelectionEnabled;

   public PluginPositionSelectorModel() {
      this.selectedRelativePositions = this.newProperty("selectedRelativePosition", this.supportedRelativePositions);
      this.knownServers = this.newListProperty("knownServers");
      this.selectedServer = this.newProperty("selectedServer");
      this.pluginPositions = this.newListProperty("pluginPositions");
      this.selectedPluginPositions = Sets.newHashSet();
      this.busy = this.newProperty("busy", Boolean.FALSE);
      this.caches = new ConcurrentHashMap<>();
   }

   public List<ServerInfo> getKnownServers() {
      return this.knownServers.get();
   }

   public void setKnownServers(List<KnownServer> var1) {
      Comparator var2 = Collections.reverseOrder(ServerComparators.SORT_BY_LATEST_LOGIN);
      Collections.sort(var1, var2);
      ArrayList var3 = Lists.newArrayList();

      for (KnownServer var5 : var1) {
         var3.add(var5.getServerInfo());
      }

      this.knownServers.set(var3);
      if (var3.size() == 1) {
         this.setSelectedServer((ServerInfo)var3.get(0));
      }
   }

   public ServerInfo getSelectedServer() {
      return this.selectedServer.get();
   }

   public void setSelectedServer(ServerInfo var1) {
      this.selectedServer.set(var1);
   }

   public List<PipelinePosition> getPluginPositions() {
      return this.pluginPositions.get();
   }

   public void setPluginPositions(List<PipelinePosition> var1, PluginDisplayNameLookup var2) {
      this.nameLookup = var2;
      this.pluginPositions.set(var1);
      boolean var3 = this.selectedPluginPositions.retainAll(var1);
      if (var3) {
         this.sendChange("selectedPluginPositions", null, this.getSelectedPluginPositions());
      }
   }

   public EnumSet<RelativePosition> getSupportedRelativePositions() {
      return EnumSet.copyOf(this.supportedRelativePositions);
   }

   public void setSupportedRelativePositions(EnumSet<RelativePosition> var1) {
      this.supportedRelativePositions = EnumSet.copyOf(var1);
      EnumSet var2 = this.getSelectedRelativePositions();
      var2.retainAll(var1);
      this.setSelectedRelativePositions(var2);
   }

   public EnumSet<RelativePosition> getSelectedRelativePositions() {
      return EnumSet.copyOf(this.selectedRelativePositions.get());
   }

   public void setSelectedRelativePositions(EnumSet<RelativePosition> var1) {
      this.selectedRelativePositions.set(EnumSet.copyOf(var1));
   }

   public void removeAllPluginPositions() {
      this.setPluginPositions(null, null);
   }

   public PipelinePluginManifestCache getManifestCache(PluginIdentifier var1) {
      ServerInfo var2 = this.getSelectedServer();
      return var2 == null ? null : this.caches.get(var2.getGuid());
   }

   public void addCache(ServerGuid var1, PipelinePluginManifestCache var2) {
      this.caches.put(var1, var2);
   }

   public PluginDisplayNameLookup getNameLookup() {
      return this.nameLookup;
   }

   public boolean isBusy() {
      return this.busy.get();
   }

   public void setBusy(boolean var1) {
      this.busy.set(var1);
   }

   public void setSelectedPluginPositions(Iterable<PipelinePosition> var1) {
      Set var2 = this.selectedPluginPositions;
      this.selectedPluginPositions = Sets.newHashSet();

      for (PipelinePosition var4 : var1) {
         if (this.pluginPositions.contains(var4)) {
            this.selectedPluginPositions.add(var4);
         }
      }

      if (!var2.equals(var1)) {
         this.sendChange("selectedPluginPositions", null, this.getSelectedPluginPositions());
      }
   }

   public void setPluginPositionSelected(PipelinePosition var1, boolean var2) {
      if (this.pluginPositions.contains(var1)) {
         boolean var3 = false;
         if (var2) {
            var3 = this.selectedPluginPositions.add(var1);
         } else {
            var3 = this.selectedPluginPositions.remove(var1);
         }

         if (var3) {
            this.sendChange("selectedPluginPositions", null, this.getSelectedPluginPositions());
         }
      }
   }

   public Set<PipelinePosition> getSelectedPluginPositions() {
      return Sets.newHashSet(this.selectedPluginPositions);
   }

   public boolean isCheckSelectionEnabled() {
      return this.checkSelectionEnabled;
   }

   public void setCheckSelectionEnabled(boolean var1) {
      this.checkSelectionEnabled = var1;
   }

   public PipelinePosition findPosition(PluginIdentifier var1, RelativePosition var2) {
      for (PipelinePosition var4 : this.pluginPositions.get()) {
         if (var4.getPluginId().equals(var1) && var4.getRelativePosition() == var2) {
            return var4;
         }
      }

      return null;
   }

   public boolean isEmpty() {
      return this.pluginPositions.isEmpty();
   }
}
