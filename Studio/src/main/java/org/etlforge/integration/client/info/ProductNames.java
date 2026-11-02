package org.etlforge.integration.client.info;

import org.etlforge.application.config.ApplicationConfiguration;

public final class ProductNames {
   public static final String COMPANY = ApplicationConfiguration.getCompanyName();
   public static final String CLIENT = ApplicationConfiguration.getProductName();
   public static final String SERVER = ApplicationConfiguration.getString("Product.Name.Server", "!Product.Name.Server!");

   private ProductNames() {
      throw new UnsupportedOperationException();
   }
}
