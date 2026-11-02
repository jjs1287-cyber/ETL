package org.etlforge.integration.client.project;

import com.google.common.base.Optional;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.etlforge.application.VersionFactory;
import org.etlforge.application.cache.DataCacheException;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.plugin.pipeline.AppliedPipelinePlugin;
import org.etlforge.integration.client.plugin.pipeline.AppliedPipelinePluginStoreListener;
import org.etlforge.integration.client.plugin.pipeline.MutableAppliedPipelinePluginStore;
import org.etlforge.integration.client.plugin.pipeline.cache.PipelinePluginManifestCache;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.plugin.PluginIdentifier;
import org.etlforge.integration.plugin.pipeline.PipelinePluginManifest;
import org.etlforge.integration.plugin.pipeline.PipelinePosition;
import org.etlforge.integration.plugin.pipeline.RelativePosition;
import org.etlforge.util.version.Version;
import org.etlforge.xml.Elements;
import org.etlforge.xml.KongaXmlUtils;
import org.etlforge.xml.XmlStringBuilder;
import org.etlforge.xml.dom.DomParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

public final class ProjectPipelinePluginStore implements MutableAppliedPipelinePluginStore {
   private final Multimap<IntegrationEntityId, AppliedPipelinePlugin> plugins = HashMultimap.create();
   private final List<AppliedPipelinePluginStoreListener> listeners = Lists.newCopyOnWriteArrayList();
   private Optional<PipelinePluginManifestCache> manifests = Optional.absent();

   public void setManifests(PipelinePluginManifestCache var1) {
      this.manifests = Optional.of(var1);
   }

   void addAllFrom(ProjectPipelinePluginStore var1) {
      for (AppliedPipelinePlugin var3 : var1.plugins.values()) {
         this.plugins.put(var3.getEntityId(), var3);
      }
   }

   @Override
   public void setAssociations(IntegrationEntity var1, Iterable<AppliedPipelinePlugin> var2) {
      this.plugins.replaceValues(var1.getID(), var2);
      this.fireChange(var1);
   }

   @Override
   public AppliedPipelinePlugin[] getAllAssociations(IntegrationEntity var1) {
      Collection var2 = this.plugins.get(var1.getID());
      AppliedPipelinePlugin[] var3 = (AppliedPipelinePlugin[]) var2.toArray(new AppliedPipelinePlugin[var2.size()]);
      if (this.manifests.isPresent()) {
         updateDisplayNames(var3, this.manifests.get());
      }

      return var3;
   }

   private static void updateDisplayNames(AppliedPipelinePlugin[] var0, PipelinePluginManifestCache var1) {
      for (AppliedPipelinePlugin var5 : var0) {
         setDisplayName(var5, var1);
      }
   }

   private static void setDisplayName(AppliedPipelinePlugin var0, PipelinePluginManifestCache var1) {
      try {
         PipelinePluginManifest var2 = var1.getCachedManifest(var0.getPluginId().getName());
         if (var2 != null) {
            var0.setDisplayName(var2.getDisplayName());
         }
      } catch (DataCacheException var3) {
         ErrorLog.log(ProjectPipelinePluginStore.class, "An error occurred when looking up the cached manifest for the plugin " + var0.getPluginId(), var3);
      }
   }

   @Override
   public boolean removeAll(IntegrationEntity var1) {
      IntegrationEntityId var2 = var1.getID();
      if (this.plugins.containsKey(var2)) {
         this.plugins.removeAll(var2);
         this.fireChange(var1);
         return true;
      } else {
         return false;
      }
   }

   @Override
   public void copy(IntegrationEntity var1, IntegrationEntity var2) {
      ArrayList var3 = Lists.newArrayList();

      for (AppliedPipelinePlugin var7 : this.getAllAssociations(var1)) {
         AppliedPipelinePlugin var8 = new AppliedPipelinePlugin(
            var2.getID(), var7.getPluginId().getName(), var7.getPluginId().getVersion(), var7.getPipelinePosition()
         );
         var3.add(var8);
      }

      this.setAssociations(var2, var3);
   }

   @Override
   public void addListener(AppliedPipelinePluginStoreListener var1) {
      this.listeners.add(var1);
   }

   @Override
   public void removeListener(AppliedPipelinePluginStoreListener var1) {
      this.listeners.remove(var1);
   }

   @Override
   public void load(Reader var1) {
      this.plugins.clear();

      try {
         ProjectPipelinePluginStore.Loader var2 = new ProjectPipelinePluginStore.Loader();
         var2.load(var1);
      } catch (Exception var3) {
         ErrorLog.log(ProjectPipelinePluginStore.class, "Failed to load project plugin information.", var3);
      }
   }

   @Override
   public void persist(Writer var1) throws IOException {
      String var2 = new ProjectPipelinePluginStore.XmlBuilder().build();
      var1.write(var2);
   }

   private void fireChange(IntegrationEntity var1) {
      for (AppliedPipelinePluginStoreListener var3 : this.listeners) {
         var3.assignedPluginsChange(this, var1);
      }
   }

   private class Loader {
      private IntegrationEntityId entityId;
      private String pluginName;
      private Version pluginVersion;
      private PipelinePosition pipelinePosition;

      private Loader() {
      }

      public void load(Reader var1) throws Exception {
         Document var2 = DomParser.parseInputSource(new InputSource(var1));

         for (Element var4 : Elements.matching("PipelinePlugins/Entity", var2)) {
            this.parseEntity(var4);
         }
      }

      private void parseEntity(Element var1) throws Exception {
         this.entityId = this.identifyEntity(var1);

         for (Element var3 : Elements.matching("PipelinePlugin", var1)) {
            this.parsePlugin(var3);
            if (this.isEntryComplete()) {
               AppliedPipelinePlugin var4 = new AppliedPipelinePlugin(this.entityId, this.pluginName, this.pluginVersion, this.pipelinePosition);
               ProjectPipelinePluginStore.this.plugins.put(this.entityId, var4);
            }

            this.resetPlugin();
         }
      }

      private IntegrationEntityId identifyEntity(Element var1) {
         EntityType var2 = EntityType.valueOf(var1.getAttribute("type"));
         return var2.getIntegrationId(var1.getAttribute("eid"));
      }

      private void parsePlugin(Element var1) throws Exception {
         this.pluginName = KongaXmlUtils.getChildElementValue(var1, "Name");
         this.pluginVersion = VersionFactory.fromString(KongaXmlUtils.getChildElementValue(var1, "Version"));
         Element var2 = Elements.matching("PipelinePosition", var1).item(0);
         this.pipelinePosition = this.parsePipelinePosition(var2);
      }

      private PipelinePosition parsePipelinePosition(Element var1) throws Exception {
         try {
            PluginIdentifier var2 = new PluginIdentifier(this.pluginName, this.pluginVersion);
            EntityType var3 = EntityType.valueOf(KongaXmlUtils.getChildElementValue(var1, "PipelineSubject"));
            int var4 = 50;

            try {
               var4 = Integer.parseInt(KongaXmlUtils.getChildElementValue(var1, "ExecutionOrder"));
            } catch (Exception var7) {
            }

            RelativePosition var5 = RelativePosition.fromString(KongaXmlUtils.getChildElementValue(var1, "Relative"));
            boolean var6 = Boolean.parseBoolean(KongaXmlUtils.getChildElementValue(var1, "Optional"));
            if (var3 != null && var5 != null) {
               return new PipelinePosition(var2, var3, var5, var4, var6);
            }
         } catch (Exception var8) {
         }

         return null;
      }

      private boolean isEntryComplete() {
         return this.pluginName != null && this.pluginVersion != null && this.pipelinePosition != null;
      }

      private void resetPlugin() {
         this.pluginName = null;
         this.pluginVersion = null;
         this.pipelinePosition = null;
      }
   }

   private class XmlBuilder {
      private final XmlStringBuilder xml = new XmlStringBuilder();

      public XmlBuilder() {
      }

      public String build() {
         this.xml.addElement("PipelinePlugins");

         for (IntegrationEntityId var2 : ProjectPipelinePluginStore.this.plugins.keySet()) {
            this.writeEntity(var2);
         }

         this.xml.closeElement();
         return this.xml.getXml();
      }

      private void writeEntity(IntegrationEntityId var1) {
         this.openEntityElement(var1);

         for (AppliedPipelinePlugin var3 : ProjectPipelinePluginStore.this.plugins.get(var1)) {
            this.writePluginData(var3);
         }

         this.closeEntityElement();
      }

      private void openEntityElement(IntegrationEntityId var1) {
         this.xml.addElement("Entity");
         this.xml.addAttribute("type", var1.getEntityType().name());
         this.xml.addAttribute("eid", var1.toString());
      }

      private void closeEntityElement() {
         this.xml.closeElement();
      }

      private void writePluginData(AppliedPipelinePlugin var1) {
         this.xml.addElement("PipelinePlugin");
         this.writePluginName(var1);
         this.writePluginVersion(var1);
         this.writePipelinePosition(var1.getPipelinePosition());
         this.xml.closeElement();
      }

      private void writePluginName(AppliedPipelinePlugin var1) {
         this.xml.addElement("Name");
         this.xml.setElementValue(var1.getPluginId().getName());
         this.xml.closeElement();
      }

      private void writePluginVersion(AppliedPipelinePlugin var1) {
         this.xml.addElement("Version");
         this.xml.setElementValue(var1.getPluginId().getVersion().toString());
         this.xml.closeElement();
      }

      private void writePipelinePosition(PipelinePosition var1) {
         this.xml.addElement("PipelinePosition");
         this.writePipelineEntity(var1);
         this.writeRelativePosition(var1);
         this.writeExecutionOrder(var1);
         this.writeOptional(var1);
         this.xml.closeElement();
      }

      private void writePipelineEntity(PipelinePosition var1) {
         this.xml.addElement("PipelineSubject");
         this.xml.setElementValue(var1.getEntityType().toString());
         this.xml.closeElement();
      }

      private void writeRelativePosition(PipelinePosition var1) {
         this.xml.addElement("Relative");
         this.xml.setElementValue(var1.getRelativePosition().toString());
         this.xml.closeElement();
      }

      private void writeExecutionOrder(PipelinePosition var1) {
         this.xml.addElement("ExecutionOrder");
         this.xml.setElementValue(String.valueOf(var1.getExecutionOrder()));
         this.xml.closeElement();
      }

      private void writeOptional(PipelinePosition var1) {
         this.xml.addElement("Optional");
         this.xml.setElementValue(String.valueOf(var1.isOptional()));
         this.xml.closeElement();
      }
   }

   static final class XmlConstants {
      static final String ROOT = "PipelinePlugins";
      static final String ENTITY = "Entity";
      static final String ENTITY_ID = "eid";
      static final String ENTITY_TYPE = "type";
      static final String PLUGIN = "PipelinePlugin";
      static final String PLUGIN_NAME = "Name";
      static final String PLUGIN_VERSION = "Version";
      static final String POSITION = "PipelinePosition";
      static final String POSITION_SUBJECT = "PipelineSubject";
      static final String RELATIVE_POSITION = "Relative";
      static final String EXECUTION_ORDER = "ExecutionOrder";
      static final String OPTIONAL = "Optional";

      private XmlConstants() {
      }
   }
}
