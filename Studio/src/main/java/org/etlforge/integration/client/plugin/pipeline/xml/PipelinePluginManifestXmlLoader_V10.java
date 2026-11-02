package org.etlforge.integration.client.plugin.pipeline.xml;

import com.google.common.base.Objects;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.VersionFactory;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.dataelement.DataElementType;
import org.etlforge.integration.plugin.PluginDataElementTemplate;
import org.etlforge.integration.plugin.PluginIdentifier;
import org.etlforge.integration.plugin.pipeline.PipelinePluginManifest;
import org.etlforge.integration.plugin.pipeline.PipelinePluginManifestLoader;
import org.etlforge.integration.plugin.pipeline.PipelinePluginManifestLoadingException;
import org.etlforge.integration.plugin.pipeline.PipelinePosition;
import org.etlforge.integration.plugin.pipeline.RelativePosition;
import org.etlforge.lang.KongaStringUtils;
import org.etlforge.util.version.IllegalVersionException;
import org.etlforge.util.version.Version;
import org.etlforge.xml.sax.EndElementHandler;
import org.etlforge.xml.sax.EndOfElement;
import org.etlforge.xml.sax.KongaSaxParser;
import org.etlforge.xml.sax.RelativePathRoot;
import org.etlforge.xml.sax.StartElementHandler;
import org.xml.sax.Attributes;

public final class PipelinePluginManifestXmlLoader_V10 implements PipelinePluginManifestLoader {
   @Override
   public PipelinePluginManifest[] loadManifests(String var1) throws PipelinePluginManifestLoadingException {
      try {
         KongaSaxParser var2 = new KongaSaxParser();
         PipelinePluginManifestXmlLoader_V10.Handler var3 = new PipelinePluginManifestXmlLoader_V10.Handler(var1);
         var3.registerWithParser(var2);
         var2.parseString(var1);
         return var3.getManifests();
      } catch (Exception var4) {
         throw new PipelinePluginManifestLoadingException(var4);
      }
   }

   private static boolean isTrue(Attributes var0, String var1, boolean var2) {
      String var3 = var0.getValue(var1);
      return var3 == null ? var2 : var3.equalsIgnoreCase("true") || var3.equals("1");
   }

   private static final class DataElementImpl implements PluginDataElementTemplate {
      private final String name;
      private final DataElementType type;
      private final String defaultValue;
      private final String description;
      private final boolean required;

      public DataElementImpl(String var1, DataElementType var2, String var3, String var4, boolean var5) {
         this.name = var1;
         this.type = var2;
         this.defaultValue = var3;
         this.description = var4;
         this.required = var5;
      }

      @Override
      public String getName() {
         return this.name;
      }

      @Override
      public DataElementType getType() {
         return this.type;
      }

      @Override
      public Object getDefaultValue() {
         return this.defaultValue;
      }

      @Override
      public String getDescription() {
         return this.description;
      }

      @Override
      public boolean isRequired() {
         return this.required;
      }
   }

   @RelativePathRoot("ETLForgePlugin/PipelinePlugin")
   private class Handler {
      private final String xml;
      private final Version pluginSpecVersion = VersionFactory.newVersion(1, 0, 0);
      private final List<PipelinePluginManifest> manifests = Lists.newArrayList();
      private Map<String, String> values;
      private List<PluginDataElementTemplate> dataElements;
      private List<PipelinePosition> positions;

      public Handler(String var2) {
         this.xml = var2;
      }

      public void registerWithParser(KongaSaxParser var1) {
         var1.registerHandler(this);
         var1.registerHandler(new PipelinePluginManifestXmlLoader_V10.Handler.DataElementHandler());
         var1.registerHandler(new PipelinePluginManifestXmlLoader_V10.Handler.PositionHandler());
      }

      public PipelinePluginManifest[] getManifests() {
         return this.manifests.toArray(new PipelinePluginManifest[this.manifests.size()]);
      }

      @StartElementHandler("/")
      public void newPlugin() {
         this.values = Maps.newHashMap();
         this.dataElements = Lists.newArrayList();
         this.positions = Lists.newArrayList();
      }

      @EndElementHandler("*")
      public void value(EndOfElement var1) {
         String var2 = var1.localName();
         String var3 = var1.value().trim();
         this.values.put(var2, var3);
      }

      @EndElementHandler("/")
      public void doneWithPlugin() {
         PipelinePluginManifestXmlLoader_V10.ManifestImpl var1 = new PipelinePluginManifestXmlLoader_V10.ManifestImpl(
            this.xml, this.pluginSpecVersion, this.values, this.dataElements, this.positions
         );
         this.manifests.add(var1);
      }

      @RelativePathRoot("ETLForgePlugin/PipelinePlugin/DataElements/DataElement")
      private class DataElementHandler {
         private String name;
         private DataElementType type;
         private boolean required;
         private String documentation;
         private String defaultValue;

         private DataElementHandler() {
         }

         @StartElementHandler("/")
         public void start(Attributes var1) {
            this.name = var1.getValue("Name");
            this.required = PipelinePluginManifestXmlLoader_V10.isTrue(var1, "Required", false);
            String var2 = var1.getValue("Type");
            this.type = StringUtils.isEmpty(var2) ? DataElementType.UNDEFINED : DataElementType.fromString(var2);
         }

         @EndElementHandler("Documentation")
         public void documentation(String var1) {
            this.documentation = var1.trim();
         }

         @EndElementHandler("DefaultValue")
         public void defaultValue(String var1) {
            this.defaultValue = var1.trim();
         }

         @EndElementHandler("/")
         public void end() {
            PipelinePluginManifestXmlLoader_V10.DataElementImpl var1 = new PipelinePluginManifestXmlLoader_V10.DataElementImpl(
               this.name, this.type, this.defaultValue, this.documentation, this.required
            );
            Handler.this.dataElements.add(var1);
         }
      }

      @RelativePathRoot("ETLForgePlugin/PipelinePlugin/PipelinePosition")
      private class PositionHandler {
         private boolean optional;
         private RelativePosition relative;
         private int executionOrder;

         private PositionHandler() {
         }

         @StartElementHandler("/")
         public void start(Attributes var1) {
            this.optional = PipelinePluginManifestXmlLoader_V10.isTrue(var1, "Optional", true);
            this.relative = RelativePosition.fromString(var1.getValue("Relative"));
            String var2 = var1.getValue("ExecutionOrder");
            if (StringUtils.isEmpty(var2)) {
               this.executionOrder = 50;
            } else {
               this.executionOrder = Integer.parseInt(var2);
            }
         }

         @EndElementHandler("/")
         public void end(String var1) {
            try {
               EntityType var2 = EntityType.valueOf(var1);
               PluginIdentifier var3 = new PluginIdentifier(Handler.this.values.get("Name"), VersionFactory.fromString(Handler.this.values.get("Version")));
               PipelinePosition var4 = new PipelinePosition(var3, var2, this.relative, this.executionOrder, this.optional);
               Handler.this.positions.add(var4);
            } catch (IllegalVersionException var5) {
               Throwables.propagate(var5);
            }
         }
      }
   }

   private static class ManifestImpl implements PipelinePluginManifest {
      private final String xml;
      private final Version pluginSpecVersion;
      private final Version version;
      private final ImmutableMap<String, String> keyValuePairs;
      private final ImmutableList<PluginDataElementTemplate> dataElements;
      private final ImmutableList<PipelinePosition> positions;

      public ManifestImpl(String var1, Version var2, Map<String, String> var3, List<PluginDataElementTemplate> var4, List<PipelinePosition> var5) {
         this.xml = var1;
         this.pluginSpecVersion = var2;
         this.keyValuePairs = ImmutableMap.copyOf(var3);
         this.dataElements = ImmutableList.copyOf(var4);
         this.positions = ImmutableList.copyOf(var5);
         this.version = this.constructVersion();
      }

      private Version constructVersion() {
         try {
            String var1 = this.keyValuePairs.get("Version");
            return VersionFactory.fromString(var1);
         } catch (IllegalVersionException var2) {
            var2.printStackTrace();
         } catch (RuntimeException var3) {
            var3.printStackTrace();
         }

         return null;
      }

      private String getSafeString(String var1) {
         String var2 = this.keyValuePairs.get(var1);
         return var2 != null ? var2 : "";
      }

      @Override
      public String getDefinition() {
         return this.xml;
      }

      @Override
      public String getCommand() {
         return this.getSafeString("Command");
      }

      @Override
      public String getArguments() {
         return this.getSafeString("Arguments");
      }

      @Override
      public String getAuthor() {
         return this.getSafeString("Author");
      }

      @Override
      public PluginDataElementTemplate[] getDataElements() {
         return this.dataElements.toArray(new PluginDataElementTemplate[this.dataElements.size()]);
      }

      @Override
      public String getDescription() {
         return this.getSafeString("Description");
      }

      @Override
      public String getDisplayName() {
         String var1 = this.getSafeString("DisplayName");
         if (var1.length() == 0) {
            var1 = this.getName();
         }

         return var1;
      }

      @Override
      public URL getDocumentationUrl() {
         return this.getUrl("DocumentationUrl");
      }

      @Override
      public String getName() {
         return this.getSafeString("Name");
      }

      @Override
      public Version getVersion() {
         return this.version;
      }

      @Override
      public PipelinePosition[] getPipelinePositions() {
         return this.positions.toArray(new PipelinePosition[this.positions.size()]);
      }

      @Override
      public URL getUpdateUrl() {
         return this.getUrl("UpdateUrl");
      }

      @Override
      public Version getSpecificationVersion() {
         return this.pluginSpecVersion;
      }

      @Override
      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         } else {
            return var1 instanceof PipelinePluginManifest ? this.equals((PipelinePluginManifest)var1) : false;
         }
      }

      private boolean equals(PipelinePluginManifest var1) {
         return KongaStringUtils.areEqual(this.getName(), var1.getName()) && this.getVersion().equals(var1.getVersion());
      }

      @Override
      public int hashCode() {
         return Objects.hashCode(this.getName(), this.getVersion());
      }

      private URL getUrl(String var1) {
         try {
            String var2 = this.keyValuePairs.get(var1);
            if (var2 != null && var2.length() > 0) {
               return new URL(var2);
            }
         } catch (MalformedURLException var3) {
            var3.printStackTrace();
         }

         return null;
      }
   }
}
