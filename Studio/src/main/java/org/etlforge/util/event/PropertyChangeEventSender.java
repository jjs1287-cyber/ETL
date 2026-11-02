package org.etlforge.util.event;

import java.beans.PropertyChangeListener;

public interface PropertyChangeEventSender {
   void addPropertyChangeListener(PropertyChangeListener var1);

   void addPropertyChangeListener(String var1, PropertyChangeListener var2);

   void removePropertyChangeListener(PropertyChangeListener var1);

   void removePropertyChangeListener(String var1, PropertyChangeListener var2);
}
