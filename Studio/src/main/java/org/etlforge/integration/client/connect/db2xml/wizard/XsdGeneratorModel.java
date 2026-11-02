package org.etlforge.integration.client.connect.db2xml.wizard;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.data.structure.database.DatabaseStructureTree;
import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.Property;
import org.etlforge.util.file.WindowsFileNameValidator;
import org.etlforge.xml.ElementNameSanitizer;

final class XsdGeneratorModel extends AbstractPropertyBasedModel {
   public static final String FILE_NAME = "filename";
   public static final String ROOT = "root";
   public static final String TARGET_NAMESPACE = "targetNamespace";
   public static final String INCLUDE_DEFAULTS = "includeDefaults";
   public static final String CONTENT = "content";
   public static final String VALID_SETTINGS = "validSettings";
   public static final String READY = "ready";
   private final Property<String> fileName = this.newProperty("filename", "");
   private final Property<String> root = this.newProperty("root", "_root_");
   private final Property<String> targetNamespace = this.newProperty("targetNamespace", "");
   private final Property<Boolean> includeDefaults = this.newProperty("includeDefaults", false);
   private final Property<String> content = this.newProperty("content", "");
   private final Property<Boolean> validSettings = this.newProperty("validSettings", false);
   private final Property<Boolean> ready = this.newProperty("ready", false);
   private volatile DatabaseStructure dbStructure;

   public XsdGeneratorModel() {
      this.hookUpInternalListening();
   }

   private void hookUpInternalListening() {
      PropertyChangeListener var1 = new PropertyChangeListener() {
         @Override
         public void propertyChange(PropertyChangeEvent var1) {
            XsdGeneratorModel.this.validateSettings();
         }
      };
      this.root.addListener(var1);
      this.fileName.addListener(var1);
      this.content.addListener(new PropertyChangeListener() {
         @Override
         public void propertyChange(PropertyChangeEvent var1) {
            XsdGeneratorModel.this.updateReadyState();
         }
      });
   }

   private void validateSettings() {
      boolean var1 = this.isRootNameValid() && this.isFileNameValid();
      this.validSettings.set(var1);
      this.updateReadyState();
   }

   private boolean isRootNameValid() {
      String var1 = this.root.get();
      return ElementNameSanitizer.isValid(var1);
   }

   private boolean isFileNameValid() {
      String var1 = this.fileName.get();
      WindowsFileNameValidator var2 = new WindowsFileNameValidator();
      return var2.isValid(var1);
   }

   public DatabaseStructure getDbStructure() {
      return this.dbStructure;
   }

   public void setDatabaseStructure(DatabaseStructure var1) {
      this.dbStructure = var1;
      String var2 = this.getSuggestedXsdName(var1);
      this.fileName.set(var2);
   }

   private String getSuggestedXsdName(DatabaseStructure var1) {
      String var2 = var1.getDbLocation().getDatabase();
      BeginEndQuote var3 = var1.getBeginEndQuote();
      DatabaseStructureTree var4 = new DatabaseStructureTree(var1);
      DatabaseStructureTree.Node var5 = var4.getRoot();
      DatabaseObject var6 = var5.getTable();
      XsdNameGenerator var7 = new XsdNameGenerator(var2, var3);
      return var7.getXsdName(var6);
   }

   public String getRootName() {
      return this.root.get();
   }

   public void setRootName(String var1) {
      this.root.set(var1);
   }

   public String getFileName() {
      return this.fileName.get();
   }

   public void setFileName(String var1) {
      this.fileName.set(var1);
   }

   public void setContent(String var1) {
      this.content.set(var1);
   }

   public String getContent() {
      return this.content.get();
   }

   public String getTargetNamespace() {
      return this.targetNamespace.get();
   }

   public void setTargetNamespace(String var1) {
      this.targetNamespace.set(var1);
   }

   public boolean isIncludeDefaultsSelected() {
      return this.includeDefaults.get();
   }

   public void setIncludeDefaultsSelected(boolean var1) {
      this.includeDefaults.set(var1);
   }

   public void setNotReady() {
      this.ready.set(false);
   }

   private void updateReadyState() {
      boolean var1 = this.validSettings.get() && !StringUtils.isEmpty(this.content.get());
      this.ready.set(var1);
   }
}
