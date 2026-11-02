package org.etlforge.integration.client.project.caseconverter;

import com.google.common.collect.Sets;
import java.io.File;
import java.util.Collection;
import java.util.Set;
import org.etlforge.integration.client.project.ProjectFile;
import org.etlforge.integration.client.project.ProjectFolders;
import org.etlforge.integration.data.entity.EntityDescriptor;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.io.Folder;
import org.etlforge.xml.Elements;
import org.etlforge.xml.dom.DomParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class ProjectProcessor {
   private final String projectName;
   private final File projectFolder;
   private final Folder dataRoot;
   private final Folder jtrRoot;
   private final DbSchemaTransformProvider schemaTransformProvider;
   private Document projectStructure;
   private Set<EntityDescriptor> affectedTransformations;
   private Set<EntityDescriptor> skippedTransformations;
   private boolean deleteJtrFiles;

   public ProjectProcessor(String var1, File var2, Conversion var3) {
      this.projectName = var1;
      this.projectFolder = var2;
      this.schemaTransformProvider = DbSchemaTransformProvider.newProvider(var3, new Folder(var2));
      this.dataRoot = ProjectFolders.getProjectDataFolder(var2);
      this.jtrRoot = ProjectFolders.getCacheFolder(var2).subFolder("TransformationStructures");
      this.deleteJtrFiles = true;
   }

   public void setDeleteJtrFiles(boolean var1) {
      this.deleteJtrFiles = var1;
   }

   public Collection<EntityDescriptor> getAffectedTransformations() {
      return Sets.newHashSet(this.affectedTransformations);
   }

   public Collection<EntityDescriptor> getSkippedTransformations() {
      return Sets.newHashSet(this.skippedTransformations);
   }

   public void process() throws Exception {
      this.affectedTransformations = Sets.newHashSet();
      this.skippedTransformations = Sets.newHashSet();
      this.projectStructure = this.loadProjectFile();
      this.processTransformations();
   }

   private Document loadProjectFile() throws Exception {
      ProjectFile var1 = ProjectFile.createForExistingProject(this.projectName, this.projectFolder, false);
      File var2 = new File(this.projectFolder, var1.getProperty("structure-file"));
      return DomParser.parseFile(var2);
   }

   private void processTransformations() throws Exception {
      TransformationProcessor var1 = new TransformationProcessor(this.schemaTransformProvider);
      Folder var2 = this.dataRoot.subFolder(EntityType.Transformation.name());

      for (Element var4 : this.getEntityElementsOfType(EntityType.Transformation)) {
         String var5 = var4.getAttribute("entityId");
         String var6 = var4.getAttribute("name");
         EntityDescriptor var7 = this.getTransformationDescriptor(var5, var6);
         File var8 = var2.getFile(var5 + ".xml");
         var1.process(var8);
         if (var1.wasLastTransformationAffected()) {
            this.affectedTransformations.add(var7);
            this.deleteJtrFiles(var5);
         } else {
            this.skippedTransformations.add(var7);
         }
      }
   }

   private EntityDescriptor getTransformationDescriptor(String var1, String var2) {
      EntityDescriptor var3 = new EntityDescriptor(EntityType.Transformation, var1);
      var3.setName(var2);
      return var3;
   }

   private void deleteJtrFiles(String var1) {
      this.deleteJtrFile(var1, "input");
      this.deleteJtrFile(var1, "output");
   }

   private void deleteJtrFile(String var1, String var2) {
      File var3 = this.jtrRoot.getFile(var1 + "_" + var2 + ".jtr");
      if (!var3.exists() || !this.deleteJtrFiles || !var3.delete()) {
         ;
      }
   }

   private Elements getEntityElementsOfType(EntityType var1) {
      return Elements.matching("Project/EntityType[@name='" + var1.name() + "']//Entity", this.projectStructure);
   }
}
