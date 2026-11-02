package org.etlforge.integration.client.ui.interchange.entity.transformation.tree;

public interface TreeAttribute extends TreeMapperNode {
   String getDataType();

   int getSqlDataType();

   String getDefaultValue();

   boolean isNullable();
}
