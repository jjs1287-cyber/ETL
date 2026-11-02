package org.etlforge.util;

import java.beans.PropertyChangeListener;
import java.io.Serializable;

public interface Property<T> extends Serializable {
   String name();

   T get();

   boolean isNull();

   void set(T var1);

   void setSilently(T var1);

   void setAllowsNull(boolean var1);

   void addListener(PropertyChangeListener var1);

   void removeListener(PropertyChangeListener var1);
}
