package org.etlforge.integration.activity.ui;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.beans.PropertyChangeListener;
import java.util.List;
import org.etlforge.integration.activity.ActivitySubject;
import org.etlforge.integration.activity.ActivitySubjectType;
import org.etlforge.integration.activity.serverapi.ActivitySubjectId;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;

public final class IntegrationEntityActivitySubject implements ActivitySubject {
   private final ActivitySubjectId id;
   private final IntegrationEntity entity;
   private final List<PropertyChangeListener> nameListeners;

   public static IntegrationEntityActivitySubject create(IntegrationEntity var0) {
      Preconditions.checkArgument(ActivityUtils.isValidType(var0), "Invalid entity type: " + var0.getEntityType());
      return new IntegrationEntityActivitySubject(var0);
   }

   private IntegrationEntityActivitySubject(IntegrationEntity var1) {
      this.entity = var1;
      ActivitySubjectType var2;
      IntegrationEntityId var3;
      if (var1 instanceof RootFolder) {
         var3 = var1.getParent().getID();
         var2 = ActivitySubjectType.PROJECT;
      } else if (var1 instanceof SalesforceWizardEntity) {
         var3 = ((SalesforceWizardEntity)var1).getImplementingOperationId();
         var2 = ActivitySubjectType.OPERATION;
      } else {
         var3 = var1.getID();
         var2 = var1 instanceof Folder ? ActivitySubjectType.FOLDER : ActivitySubjectType.OPERATION;
      }

      this.id = new ActivitySubjectId(var2, var3);
      this.nameListeners = Lists.newArrayList();
   }

   @Override
   public ActivitySubjectId getId() {
      return this.id;
   }

   @Override
   public boolean isReady() {
      return this.entity instanceof Folder ? this.entity.getNumberOfChildren() > 0 : true;
   }

   @Override
   public String getName() {
      return this.entity.getName();
   }

   @Override
   public String getJobName() {
      return this.getName();
   }

   @Override
   public void addNameChangeListener(PropertyChangeListener var1) {
      this.nameListeners.add(var1);
      this.entity.addPropertyChangeListener("Name", var1);
   }

   private void removeNameChangeListeners() {
      for (PropertyChangeListener var2 : this.nameListeners) {
         this.entity.removePropertyChangeListener("Name", var2);
      }
   }

   @Override
   public void hasBeenUpdated() {
   }

   @Override
   public void dispose() {
      this.removeNameChangeListeners();
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 instanceof IntegrationEntityActivitySubject ? this.id.equals(((IntegrationEntityActivitySubject)var1).id) : false;
      }
   }

   @Override
   public int hashCode() {
      return this.id.hashCode();
   }
}
