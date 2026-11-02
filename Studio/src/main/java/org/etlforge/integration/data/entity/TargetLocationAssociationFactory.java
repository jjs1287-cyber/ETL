package org.etlforge.integration.data.entity;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.location.LdapLocation;

final class TargetLocationAssociationFactory {
   static TargetLocationAssociation newAssociation(Target var0, DataLocation var1) {
      return new TargetLocationAssociationFactory.Creator(var0, var1).getAssociation();
   }

   private TargetLocationAssociationFactory() {
   }

   private static final class Creator {
      private final Target target;
      private final DataLocation location;

      public Creator(Target var1, DataLocation var2) {
         this.target = var1;
         this.location = var2;
      }

      public TargetLocationAssociation getAssociation() {
         return this.location instanceof LdapLocation
            ? new TargetLocationAssociationFactory.TargetLdapLocationAssociation(this.target, (LdapLocation)this.location)
            : new TargetLocationAssociationFactory.DefaultAssociation(this.target, this.location);
      }
   }

   private static class DefaultAssociation implements TargetLocationAssociation {
      private final Target target;
      private final DataLocation location;
      private final PropertyChangeListener locationListener;

      DefaultAssociation(Target var1, DataLocation var2) {
         this.target = var1;
         this.location = var2;
         this.locationListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent var1) {
               DefaultAssociation.this.target.onLocationPropertyChanged(var1);
            }
         };
         var2.addPropertyChangeListener(this.locationListener);
      }

      @Override
      public void dispose() {
         this.location.removePropertyChangeListener(this.locationListener);
      }

      protected final Target getTarget() {
         return this.target;
      }

      protected final DataLocation getLocation() {
         return this.location;
      }
   }

   private static final class TargetLdapLocationAssociation extends TargetLocationAssociationFactory.DefaultAssociation {
      private final PropertyChangeListener idListener = new PropertyChangeListener() {
         @Override
         public void propertyChange(PropertyChangeEvent var1) {
            TargetLdapLocationAssociation.this.updateTargetIdInLocation();
         }
      };

      public TargetLdapLocationAssociation(Target var1, LdapLocation var2) {
         super(var1, var2);
         var1.addPropertyChangeListener("ID", this.idListener);
         this.updateTargetIdInLocation();
      }

      @Override
      public void dispose() {
         super.dispose();
         this.getTarget().removePropertyChangeListener("ID", this.idListener);
      }

      private void updateTargetIdInLocation() {
         ((LdapLocation)this.getLocation()).setTargetId(this.getTarget().getID());
      }
   }
}
