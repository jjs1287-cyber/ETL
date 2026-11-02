package org.etlforge.ui.property;

import com.google.common.base.Preconditions;
import org.etlforge.ui.wait.AbstractWaitService;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.Property;

public final class BooleanPropertyWaitService extends AbstractWaitService {
   private final Property<Boolean> property;

   public BooleanPropertyWaitService(AbstractPropertyBasedModel var1, String var2) {
      this(var1.getProperty(var2));
   }

   public BooleanPropertyWaitService(Property<Boolean> var1) {
      this.property = Preconditions.checkNotNull(var1);
   }

   @Override
   protected void goToWaitingState() {
      this.property.set(true);
   }

   @Override
   protected void stopWaiting() {
      this.property.set(false);
   }
}
