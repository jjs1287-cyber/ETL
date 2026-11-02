package org.etlforge.integration.client.ui.structure.editor.ldap;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.client.ldap.ui.SelectedObjectClasses;
import org.etlforge.integration.data.location.LdapLocation;
import org.etlforge.integration.ldap.structure.ObjectClassDefinition;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.Property;

final class ObjectClassSelectionModel extends AbstractPropertyBasedModel {
   static final String LOCATION = "location";
   static final String OBJECT_CLASSES = "classes";
   static final String SELECTED_CLASSES = "selected_classes";
   static final String AUXILIARY_CLASSES = "auxiliary_classes";
   private final Property<LdapLocation> ldapLocation = this.newProperty("location");
   private final Property<ObjectClassDefinition[]> objectClasses = this.newProperty("classes");
   private final Property<List<ObjectClassDefinition>> selectedClasses = this.newProperty("selected_classes");

   public ObjectClassSelectionModel() {
      this.selectedClasses.set(new ArrayList<>());
   }

   public SelectedObjectClasses getSelection() {
      if (this.ldapLocation.isNull()) {
         return null;
      }

      List var1 = this.getSelectedClasses();
      return new SelectedObjectClasses(this.ldapLocation.get(), (ObjectClassDefinition[]) var1.toArray(new ObjectClassDefinition[var1.size()]));
   }

   public void setLocation(LdapLocation var1) {
      this.ldapLocation.set(var1);
   }

   public LdapLocation getLocation() {
      return this.ldapLocation.get();
   }

   public void setObjectClasses(ObjectClassDefinition[] var1) {
      this.objectClasses.set(var1);
   }

   public ObjectClassDefinition[] getObjectClasses() {
      if (this.objectClasses.isNull()) {
         return new ObjectClassDefinition[0];
      }

      ObjectClassDefinition[] var1 = this.objectClasses.get();
      if (var1.length == 0) {
         return var1;
      }

      ObjectClassDefinition[] var2 = new ObjectClassDefinition[var1.length];
      System.arraycopy(var1, 0, var2, 0, var1.length);
      return var2;
   }

   public List<ObjectClassDefinition> getSelectedClasses() {
      return this.selectedClasses.isNull() ? Lists.newArrayList() : Lists.newArrayList(this.selectedClasses.get());
   }

   public void setSelectedClasses(SelectedObjectClasses var1) {
      this.selectedClasses.set(Lists.newArrayList(var1.getClassDefinitions()));
   }

   public void setSelectedClasses(List<ObjectClassDefinition> var1) {
      this.selectedClasses.set(Lists.newArrayList(var1));
   }
}
