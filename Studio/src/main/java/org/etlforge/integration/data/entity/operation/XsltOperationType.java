package org.etlforge.integration.data.entity.operation;

import com.google.common.collect.ImmutableSet;
import java.util.Arrays;
import org.etlforge.integration.data.entity.*;
import org.etlforge.integration.data.entity.operation.pipeline.*;
import org.etlforge.integration.data.entity.operation.pipeline.archive.*;
import org.etlforge.integration.data.entity.operation.pipeline.filter.EntityFilterProvider;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.validation.ValidationMessageCollector;

public final class XsltOperationType extends OperationType {
    public static final XsltOperationType TYPE = new XsltOperationType();
    private XsltOperationType() {
        super(23, "XsltOperation", "XSLT", "Transform XML using XSLT", ImmutableSet.of(PipelineActivityType.SOURCE, PipelineActivityType.XSLT_TRANSFORM, PipelineActivityType.TARGET));
    }
    @Override public RunnableStatus checkRunnable(OperationExecutionMode mode) { return RunnableStatus.RUNNABLE; }
    @Override public OperationPipeline createNewTemplate() {
        return new OperationPipeline(this, Arrays.asList(new SourceActivity("Source"), new XsltTransformActivity("XSLT"), new TargetActivity("Target")));
    }
    @Override public ActivityInsertionPolicy getInsertionPolicy(OperationPipeline pipeline) { return connection -> ImmutableSet.of(); }
    @Override public ActivityRemovingPolicy getRemovingPolicy(OperationPipeline pipeline) {
        return activity -> { throw new RemoveActivityVetoException("The Source, XSLT and Target are required for an XSLT operation."); };
    }
    @Override public PipelineAutoCompleter getPipelineAutoCompleter(IntegrationEntityLookup lookup) { return new ArchiveAutoCompleter(lookup); }
    @Override public OperationPipelineFactory getPipelineFactory() {
        return (operation, lookup) -> new OperationPipeline(this, Arrays.asList(
            SourceActivity.newInstance(lookup.getEntity(operation.getSourceId(), Source.class), "Source"),
            new XsltTransformActivity("XSLT"),
            TargetActivity.newInstance(lookup.getEntity(operation.getTargetId(), Target.class), "Target")));
    }
    @Override public OperationPipelineValidator getPipelineValidator(Operation operation, OperationPipeline pipeline, ValidationMessageCollector messages) {
        return new OperationPipelineValidator(operation, pipeline, messages) {
            @Override public void validate() {
                OperationPipelineBrowser browser = new OperationPipelineBrowser(pipeline);
                Source source = browser.getSource(); Target target = browser.getTarget();
                if (source == null || source.getLocation() == null || !source.getLocation().isFileBased()) fatal("An XSLT operation requires a file-based Source.");
                if (target == null || target.getLocation() == null || !target.getLocation().isFileBased()) fatal("An XSLT operation requires a file-based Target.");
                int transforms = 0;
                for (PipelineActivity activity : pipeline.getActivities()) {
                    if (activity instanceof XsltTransformActivity) {
                        transforms++;
                        XsltTransform content = ((XsltTransformActivity)activity).getContent();
                        if (content == null) fatal("Select an XSLT transform.");
                        else try { XsltTransform.validateStylesheet(content.getXslt()); } catch (Exception failure) { fatal(failure.getMessage()); }
                    }
                }
                if (transforms != 1) fatal("An XSLT operation requires exactly one XSLT transform.");
            }
        };
    }
    @Override public EntityFilterProvider<Source> getSourceActivityFilterProvider(OperationPipeline pipeline) { return new ArchiveSourceFilterProvider(); }
    @Override public EntityFilterProvider<Target> getTargetActivityFilterProvider(OperationPipeline pipeline) { return new ArchiveTargetFilterProvider(); }
    @Override public EntityFilter getSourceFilter() { return ArchiveOperationType.TYPE.getSourceFilter(); }
    @Override public EntityFilter getTargetFilter() { return ArchiveOperationType.TYPE.getTargetFilter(); }
    @Override public boolean hasSourceHistory() { return true; }
    @Override public boolean isChunkable() { return false; }
    @Override public boolean isEmailNotificationsSupported() { return true; }
}
