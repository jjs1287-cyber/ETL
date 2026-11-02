package org.etlforge.integration.data.entity;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.location.LdapLocation;

final class SourceLocationAssociationFactory {
   static SourceLocationAssociation newAssociation(Source var0, DataLocation var1) {
      return new SourceLocationAssociationFactory.Creator(var0, var1).getAssociation();
   }

   private SourceLocationAssociationFactory() {
   }

   private static final class Creator {
      private final Source source;
      private final DataLocation location;

      public Creator(Source var1, DataLocation var2) {
         this.source = var1;
         this.location = var2;
      }

      public SourceLocationAssociation getAssociation() {
         return this.location instanceof LdapLocation
            ? new SourceLocationAssociationFactory.SourceLdapLocationAssociation(this.source, (LdapLocation)this.location)
            : new SourceLocationAssociationFactory.DefaultAssociation(this.source, this.location);
      }
   }

   private static class DefaultAssociation implements SourceLocationAssociation {
      private final Source source;
      private final DataLocation location;
      private final PropertyChangeListener locationListener;

      DefaultAssociation(Source var1, DataLocation var2) {
         this.source = var1;
         this.location = var2;
         this.locationListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent var1) {
               DefaultAssociation.this.source.onLocationPropertyChanged(var1);
            }
         };
         var2.addPropertyChangeListener(this.locationListener);
      }

      @Override
      public void dispose() {
         this.location.removePropertyChangeListener(this.locationListener);
      }

      protected final Source getSource() {
         return this.source;
      }

      protected final DataLocation getLocation() {
         return this.location;
      }
   }

   private static final class SourceLdapLocationAssociation extends SourceLocationAssociationFactory.DefaultAssociation {
      private final PropertyChangeListener idListener = new PropertyChangeListener() {
         @Override
         public void propertyChange(PropertyChangeEvent var1) {
            SourceLdapLocationAssociation.this.updateSourceIdInLocation();
         }
      };

      public SourceLdapLocationAssociation(Source var1, LdapLocation var2) {
         super(var1, var2);
         var1.addPropertyChangeListener("ID", this.idListener);
         this.updateSourceIdInLocation();
      }

      @Override
      public void dispose() {
         super.dispose();
         this.getSource().removePropertyChangeListener("ID", this.idListener);
      }

      private void updateSourceIdInLocation() {
         ((LdapLocation)this.getLocation()).setSourceId(this.getSource().getID());
      }
   }
}
