package org.etlforge.application.ui.help;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.io.InputSupplier;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import org.etlforge.application.config.FatalConfigurationError;
import org.etlforge.xml.sax.EndElementHandler;
import org.etlforge.xml.sax.KongaSaxParser;
import org.etlforge.xml.sax.RelativePathRoot;
import org.etlforge.xml.sax.StartElementHandler;
import org.etlforge.xml.sax.StartOfElement;

final class XmlBasedWebLinks implements ApplicationUiWebHelp.Links {
   private final URI home;
   private final ImmutableMap<String, URI> links;

   public XmlBasedWebLinks(InputSupplier<? extends InputStream> var1) {
      XmlBasedWebLinks.Handler var2 = parse(var1);
      this.home = var2.home;
      this.links = ImmutableMap.copyOf(var2.links);
   }

   private static XmlBasedWebLinks.Handler parse(InputSupplier<? extends InputStream> var0) {
      try {
         XmlBasedWebLinks.Handler var1 = new XmlBasedWebLinks.Handler();
         KongaSaxParser var2 = new KongaSaxParser(var1);
         var2.parseInput(var0);
         return var1;
      } catch (Exception var3) {
         FatalConfigurationError.throwError(5, XmlBasedWebLinks.class, var3, null);
         return null;
      }
   }

   @Override
   public URI getHomePage() {
      return this.home;
   }

   @Override
   public Optional<URI> getLink(String var1) {
      return Optional.fromNullable(this.links.get(var1));
   }

   @RelativePathRoot("help-resources")
   private static class Handler {
      private String base;
      public URI home;
      public Map<String, URI> links = Maps.newHashMap();
      private String currentId;

      private Handler() {
      }

      @EndElementHandler("base")
      public void base(String var1) {
         Preconditions.checkArgument(!Strings.isNullOrEmpty(var1), "The <base> element is empty");
         this.base = var1;
         if (!this.base.endsWith("/")) {
            this.base = this.base + "/";
         }
      }

      @EndElementHandler("home")
      public void home(String var1) {
         if (this.base == null) {
            throw new RuntimeException("The <base> element is missing");
         }

         try {
            this.home = new URI(this.base + var1);
         } catch (URISyntaxException var3) {
            throw new RuntimeException(var3);
         }
      }

      @StartElementHandler("link")
      public void startLink(StartOfElement var1) {
         this.currentId = var1.getAttribute("id");
         if (Strings.isNullOrEmpty(this.currentId)) {
            throw new RuntimeException("Missing id attribute in <link> element");
         }
      }

      @EndElementHandler("link")
      public void endLink(String var1) {
         try {
            this.links.put(this.currentId, new URI(this.base + var1));
            this.currentId = null;
         } catch (URISyntaxException var3) {
            throw new RuntimeException(var3);
         }
      }
   }
}
