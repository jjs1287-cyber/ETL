package org.etlforge.integration.data.script;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.Resources;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.VersionFactory;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.lang.KongaStringUtils;
import org.etlforge.util.version.IllegalVersionException;
import org.etlforge.util.version.Version;
import org.etlforge.xml.KongaXmlUtils;
import org.etlforge.xml.sax.EndElementHandler;
import org.etlforge.xml.sax.EndOfElement;
import org.etlforge.xml.sax.KongaSaxParser;
import org.etlforge.xml.sax.RelativePathRoot;
import org.etlforge.xml.sax.StartElementHandler;
import org.xml.sax.Attributes;

final class FunctionLoader {
   private static final String XML_RESOURCE = "FormulaBuilder.Functions.xml";
   private final Map<String, ProjectItemArgDescriptor> projectItemArgTypes = Maps.newHashMap();
   private final FunctionFactory functionFactory = new FunctionFactory();

   public ImmutableList<FunctionCategory> load() {
      try {
         KongaSaxParser var1 = new KongaSaxParser();
         var1.registerHandler(new FunctionLoader.ProjectItemArgHandler());
         var1.registerHandler(new FunctionLoader.CategoryHandler());
         var1.registerHandler(new FunctionLoader.FunctionHandler());
         var1.parseResource("FormulaBuilder.Functions.xml");
         return this.functionFactory.getCategories();
      } catch (Exception var2) {
         this.handleLoadingException(var2);
         return null;
      }
   }

   private void handleLoadingException(Exception var1) {
      ErrorLog.attention(
         FunctionLoader.class, "Failed to load the available mapping functions. This is due to a configuration error. Please contact ETLForge Support.", var1
      );
   }

   public ImmutableMap<String, ProjectItemArgDescriptor> getProjectItemArgDescriptors() {
      return ImmutableMap.copyOf(this.projectItemArgTypes);
   }

   private static Version getVersion(Attributes var0) {
      String var1 = var0.getValue("Since");
      if (StringUtils.isEmpty(var1)) {
         return VersionFactory.newVersion(1, 0, 0, 0);
      }

      try {
         return VersionFactory.fromString(var1);
      } catch (IllegalVersionException var3) {
         throw new RuntimeException("Invalid XML: Illegal value of the \"Since\" attribute.", var3);
      }
   }

   @RelativePathRoot("Functions/Category")
   private final class CategoryHandler {
      private CategoryHandler() {
      }

      @StartElementHandler("/")
      public void start(Attributes var1) {
         String var2 = var1.getValue("Name");
         if (StringUtils.isEmpty(var2)) {
            throw new RuntimeException("Invalid XML: Missing name-attribute in a Category node.");
         }

         Version var3 = FunctionLoader.getVersion(var1);
         FunctionLoader.this.functionFactory.newCategory(var2, var3);
      }
   }

   @RelativePathRoot("Functions/Category/Function")
   private final class FunctionHandler {
      private String name;
      private String documentation;
      private Version since;
      private boolean hidden;
      private String display;
      private String deName;
      private String grammar;
      private String projectItemIdPrefixes;

      private FunctionHandler() {
      }

      @StartElementHandler("/")
      public void start(Attributes var1) {
         this.name = var1.getValue("Name");
         if (StringUtils.isEmpty(this.name)) {
            throw new RuntimeException("Invalid XML: Missing name-attribute in a Function node.");
         }

         this.since = FunctionLoader.getVersion(var1);
         this.hidden = KongaXmlUtils.isTrue(var1.getValue("Hidden"));
         this.display = "";
         this.deName = "";
         this.grammar = "";
         this.projectItemIdPrefixes = "";
      }

      @EndElementHandler("/")
      public void end() {
         this.loadDocumentation();
         this.insertFunction();
      }

      private void loadDocumentation() {
         this.documentation = "";
         URL var1 = this.getDocumentationResource();
         if (var1 != null) {
            try {
               this.documentation = Resources.toString(var1, Charsets.UTF_8);
               this.documentation = StringUtils.removeStartIgnoreCase(this.documentation, "<html>");
               this.documentation = StringUtils.removeEndIgnoreCase(this.documentation, "</html>");
            } catch (IOException var3) {
               var3.printStackTrace();
            }
         }
      }

      private URL getDocumentationResource() {
         String var1 = KongaStringUtils.removeStartAndEnd(this.name.toLowerCase(), "<", ">");
         String var2 = var1 + ".html";
         URL var3 = this.getClass().getResource("/functions/" + var2);
         if (var3 == null) {
            var3 = this.getClass().getResource("/" + var2);
         }

         return var3;
      }

      private void insertFunction() {
         String var1 = this.grammar;
         String var2 = "";
         int var3 = 0;
         if (this.grammar != null && (var3 = this.grammar.indexOf(59)) >= 0 && (var3 = this.grammar.indexOf(59, var3 + 1)) >= 0) {
            var1 = this.grammar.substring(0, var3);
            var2 = this.grammar.substring(var3 + 1);
         }

         Set var4 = this.getProjectItemArgDescriptors();
         FunctionLoader.this.functionFactory.newFunction(this.name, this.since, this.display, this.documentation, var1, var2, this.deName, this.hidden, var4);
      }

      private Set<ProjectItemArgDescriptor> getProjectItemArgDescriptors() {
         if (this.projectItemIdPrefixes.isEmpty()) {
            return Collections.emptySet();
         }

         HashSet var1 = Sets.newHashSet();

         for (String var3 : Splitter.on(',').trimResults().split(this.projectItemIdPrefixes)) {
            ProjectItemArgDescriptor var4 = FunctionLoader.this.projectItemArgTypes.get(var3);
            if (var4 == null) {
               throw new RuntimeException(String.format("Invalid project item prefix \"%s\" for function %s.", var3, this.name));
            }

            var1.add(var4);
         }

         return var1;
      }

      @EndElementHandler("Display")
      public void display(String var1) {
         this.display = var1;
      }

      @EndElementHandler("DEName")
      public void deName(String var1) {
         this.deName = var1;
      }

      @EndElementHandler("Grammar")
      public void grammar(String var1) {
         this.grammar = var1;
      }

      @EndElementHandler("ProjectItemIdPrefixes")
      public void projectItemIdPrefixes(String var1) {
         this.projectItemIdPrefixes = var1;
      }
   }

   @RelativePathRoot("Functions/ProjectItemArgs/Type")
   private final class ProjectItemArgHandler {
      private String prefix;
      private Class<? extends IntegrationEntityId> idClass;
      private Class<? extends IntegrationEntity> dataClass;

      private ProjectItemArgHandler() {
      }

      @StartElementHandler("/")
      public void start() {
         this.prefix = null;
         this.idClass = null;
         this.dataClass = null;
      }

      @EndElementHandler("Prefix")
      public void prefix(EndOfElement var1) {
         this.prefix = var1.value();
      }

      @EndElementHandler("IdClass")
      public void idClass(EndOfElement var1) {
         this.idClass = this.toClass(var1, IntegrationEntityId.class);
      }

      private <T> Class<? extends T> toClass(EndOfElement var1, Class<T> var2) {
         try {
            String var3 = var1.value();
            return Class.forName(var3).asSubclass(var2);
         } catch (ClassNotFoundException var4) {
            throw new RuntimeException(var4);
         }
      }

      @EndElementHandler("DataClass")
      public void dataClass(EndOfElement var1) {
         this.dataClass = this.toClass(var1, IntegrationEntity.class);
      }

      @EndElementHandler("/")
      public void end() {
         ProjectItemArgDescriptor var1 = new ProjectItemArgDescriptor(this.prefix, this.idClass, this.dataClass);
         FunctionLoader.this.projectItemArgTypes.put(this.prefix, var1);
      }
   }
}
