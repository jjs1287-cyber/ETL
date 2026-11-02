package org.etlforge.integration.data.entity;

import org.etlforge.integration.data.entity.id.ProjectVariableId;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.integration.data.validation.ValidationStatus;

public final class ProjectVariable extends IntegrationEntity {
    @IdProperty public static final String ID = "projectvariable_id";
    @NameProperty public static final String NAME = "projectvariable_name";
    public ProjectVariable(String name) { this(null, name); }
    public ProjectVariable(ProjectVariableId id, String name) {
        super(EntityType.ProjectVariable, id, name);
        setDefaultValue("");
    }
    @Override public ProjectVariableId getID() { return (ProjectVariableId)super.getID(); }
    @Override public String[] getPropertyKeys() { return new String[]{ID, NAME, "default_value", "is_password", "Description"}; }
    public String getDefaultValue() { return getProperty("default_value", ""); }
    public void setDefaultValue(String value) { value = value == null ? "" : value; validateValue(value); setStringProperty("default_value", value); }
    public boolean isPassword() { return "1".equals(getProperty("is_password", "0")); }
    public void setPassword(boolean value) { setStringProperty("is_password", value ? "1" : "0"); }
    public void setDescription(String value) { value = value == null ? "" : value; validateValue(value); super.setDescription(value); }
    @Override public boolean isDeepCopyable() { return true; }
    public static void validateName(String name) {
        if (name == null || !name.matches("[A-Za-z_][A-Za-z0-9_.]{0,255}")
            || name.startsWith("__") || name.toLowerCase(java.util.Locale.ROOT).startsWith("etlforge.")
            || name.toLowerCase(java.util.Locale.ROOT).startsWith("jitterbit."))
            throw new IllegalArgumentException("Use a variable name without $, spaces or reserved system prefixes; for example ApiBaseUrl.");
    }
    public static void validateValue(String value) {
        if (value.indexOf(0) >= 0) throw new IllegalArgumentException("Project variable values cannot contain NUL.");
        try {
            java.nio.ByteBuffer bytes = java.nio.charset.StandardCharsets.UTF_8.newEncoder()
                .onMalformedInput(java.nio.charset.CodingErrorAction.REPORT).encode(java.nio.CharBuffer.wrap(value));
            if (bytes.remaining() > 1048576) throw new IllegalArgumentException("Project variable values are limited to 1 MiB.");
        } catch (java.nio.charset.CharacterCodingException error) { throw new IllegalArgumentException("Invalid Unicode in project variable value.", error); }
    }
    @Override protected void validateOtherPropertiesThanTheName(ValidationMessageCollector messages) {
        try { validateName(getName()); validateValue(getDefaultValue()); }
        catch (IllegalArgumentException error) { messages.addMessage(error.getMessage(), ValidationStatus.INVALID); }
    }
}
