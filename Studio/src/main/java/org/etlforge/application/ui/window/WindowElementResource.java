package org.etlforge.application.ui.window;

public interface WindowElementResource {
   WindowElementStateSpan getLifeSpan();

   void create(WindowElement var1);

   void dispose(WindowElement var1);
}
