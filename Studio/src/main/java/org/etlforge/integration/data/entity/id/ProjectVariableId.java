package org.etlforge.integration.data.entity.id;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityId;
public final class ProjectVariableId extends IntegrationEntityId {
    public ProjectVariableId() {}
    public ProjectVariableId(String value) { super(value); }
    @Override public EntityType getEntityType() { return EntityType.ProjectVariable; }
}
