package org.etlforge.integration.data.project;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.etlforge.integration.data.IntegrationData;
import org.etlforge.integration.data.ValidationListener;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.EntityUtils;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.validation.ValidationResult;
import org.etlforge.system.Disposable;

public class ProjectPropertyChangeSupport implements Disposable {
   private final PropertyChangeSupport propChangeSupport;
   private final ProjectItemPreparer registrator;
   private final PropertyChangeListener propertyChangeDispatcher;
   private final ProjectPropertyChangeSupport.ValidationDispatcher validationDispatcher;

   public ProjectPropertyChangeSupport(IntegrationProject var1) {
      this(var1, true);
   }

   public ProjectPropertyChangeSupport(IntegrationProject var1, EntityType var2) {
      this(var1, var2, true);
   }

   public ProjectPropertyChangeSupport(IntegrationProject var1, boolean var2) {
      this(var1, null, var2);
   }

   public ProjectPropertyChangeSupport(IntegrationProject var1, EntityType var2, boolean var3) {
      this.propChangeSupport = new PropertyChangeSupport(var1);
      this.propertyChangeDispatcher = new PropertyChangeListener() {
         @Override
         public void propertyChange(PropertyChangeEvent var1) {
            ProjectPropertyChangeSupport.this.propChangeSupport.firePropertyChange(var1);
         }
      };
      this.validationDispatcher = new ProjectPropertyChangeSupport.ValidationDispatcher();
      this.registrator = new ProjectPropertyChangeSupport.ListenerRegistrator(var1, var2, var3);
      this.registrator.insertAll();
   }

   @Override
   public void dispose() {
      this.registrator.removeAll();
      this.registrator.dispose();
   }

   public void addPropertyChangeListener(PropertyChangeListener var1) {
      this.propChangeSupport.addPropertyChangeListener(var1);
   }

   public void removePropertyChangeListener(PropertyChangeListener var1) {
      this.propChangeSupport.removePropertyChangeListener(var1);
   }

   public void addPropertyChangeListener(String var1, PropertyChangeListener var2) {
      this.propChangeSupport.addPropertyChangeListener(var1, var2);
   }

   public void removePropertyChangeListener(String var1, PropertyChangeListener var2) {
      this.propChangeSupport.removePropertyChangeListener(var1, var2);
   }

   public void addValidationListener(ValidationListener var1) {
      this.validationDispatcher.addExternal(var1);
   }

   public void removeValidationListener(ValidationListener var1) {
      this.validationDispatcher.removeExternal(var1);
   }

   private class ListenerRegistrator extends ProjectItemPreparer {
      private final boolean listenToValidation;
      private final EntityType typeFilter;

      public ListenerRegistrator(IntegrationProject var2, EntityType var3, boolean var4) {
         super(var2);
         this.typeFilter = var3;
         this.listenToValidation = var4;
      }

      @Override
      protected void inserted(IntegrationEntity var1) {
         if (this.isOfInterest(var1)) {
            var1.addPropertyChangeListener(ProjectPropertyChangeSupport.this.propertyChangeDispatcher);
            if (this.listenToValidation) {
               var1.addValidationListener(ProjectPropertyChangeSupport.this.validationDispatcher);
            }
         }
      }

      private boolean isOfInterest(IntegrationEntity var1) {
         return this.typeFilter == null || EntityUtils.getItemType(var1) == this.typeFilter;
      }

      @Override
      protected void removed(IntegrationEntity var1) {
         if (this.isOfInterest(var1)) {
            var1.removePropertyChangeListener(ProjectPropertyChangeSupport.this.propertyChangeDispatcher);
            if (this.listenToValidation) {
               var1.removeValidationListener(ProjectPropertyChangeSupport.this.validationDispatcher);
            }
         }
      }
   }

   private static class ValidationDispatcher implements ValidationListener {
      private final List<ValidationListener> external = new CopyOnWriteArrayList<>();

      private ValidationDispatcher() {
      }

      @Override
      public void validationComplete(IntegrationData var1, ValidationResult var2) {
         for (ValidationListener var4 : this.external) {
            var4.validationComplete(var1, var2);
         }
      }

      public void addExternal(ValidationListener var1) {
         this.external.add(var1);
      }

      public void removeExternal(ValidationListener var1) {
         this.external.remove(var1);
      }
   }
}
