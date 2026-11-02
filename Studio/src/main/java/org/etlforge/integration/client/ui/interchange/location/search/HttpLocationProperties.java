package org.etlforge.integration.client.ui.interchange.location.search;

import com.google.common.collect.ImmutableSet;
import java.util.List;
import java.util.Set;
import org.etlforge.integration.client.entity.search.ReplaceableKeyValueBooleanProperty;
import org.etlforge.integration.client.entity.search.ReplaceableKeyValueStringProperty;
import org.etlforge.integration.client.entity.search.ReplaceableProperty;
import org.etlforge.integration.client.entity.search.SearchableEnumProperty;
import org.etlforge.integration.client.entity.search.SearchableProperty;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.location.HttpLocation;
import org.etlforge.integration.http.HttpVerb;
import org.etlforge.integration.http.SslVersion;

final class HttpLocationProperties {
   public static void collect(SourceTarget var0, List<SearchableProperty> var1) {
      var1.add(stringProperty("URL", "httpurl"));
      var1.add(stringProperty("Login", "login"));
      var1.add(new ReplaceableKeyValueBooleanProperty("Use Basic Authentication", "use_basic_http_authentication", false, TargetFunctions.TO_LOCATION));
      var1.add(new HttpLocationProperties.HttpVerbProperty(HttpLocation.getSupportedVerbs()));
      var1.add(new HttpLocationProperties.SslVersionProperty(SslVersion.getSupportedVersions()));
      switch (var0) {
         case Source:
            var1.add(FileLocationProperties.IGNORE_FIRST);
            var1.add(FileLocationProperties.IGNORE_LAST);
            break;
         case Target:
            var1.add(stringProperty("Content-Type", "httpContentType"));
      }
   }

   private static SearchableProperty<String> stringProperty(String var0, String var1) {
      return new ReplaceableKeyValueStringProperty(var0, var1, TargetFunctions.TO_LOCATION);
   }

   private HttpLocationProperties() {
   }

   private static final class HttpVerbProperty implements ReplaceableProperty<HttpVerb>, SearchableEnumProperty<HttpVerb> {
      private final ImmutableSet<HttpVerb> allowedValues;

      private HttpVerbProperty(ImmutableSet<HttpVerb> var1) {
         this.allowedValues = var1;
      }

      @Override
      public String getName() {
         return "HTTP Verb";
      }

      @Override
      public Class<HttpVerb> getValueType() {
         return HttpVerb.class;
      }

      public HttpVerb getValue(IntegrationEntity var1) {
         DataLocation var2 = ((Locatable)var1).getLocation();
         return ((HttpLocation)var2).getHttpVerb();
      }

      public void setValue(IntegrationEntity var1, HttpVerb var2) {
         DataLocation var3 = ((Locatable)var1).getLocation();
         ((HttpLocation)var3).setHttpVerb(var2);
      }

      @Override
      public Set<HttpVerb> getSupportedValues() {
         return this.allowedValues;
      }
   }

   private static final class SslVersionProperty implements ReplaceableProperty<SslVersion>, SearchableEnumProperty<SslVersion> {
      private final ImmutableSet<SslVersion> allowedValues;

      private SslVersionProperty(ImmutableSet<SslVersion> var1) {
         this.allowedValues = var1;
      }

      @Override
      public String getName() {
         return "SSL Version";
      }

      @Override
      public Class<SslVersion> getValueType() {
         return SslVersion.class;
      }

      public SslVersion getValue(IntegrationEntity var1) {
         DataLocation var2 = ((Locatable)var1).getLocation();
         return ((HttpLocation)var2).getSslVersion();
      }

      public void setValue(IntegrationEntity var1, SslVersion var2) {
         DataLocation var3 = ((Locatable)var1).getLocation();
         ((HttpLocation)var3).setSslVersion(var2);
      }

      @Override
      public Set<SslVersion> getSupportedValues() {
         return this.allowedValues;
      }
   }
}
