package io.jenkins.plugins.pingcode.pipeline;

import com.google.common.collect.ImmutableSet;
import hudson.AbortException;
import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.model.Run;
import hudson.model.TaskListener;
import io.jenkins.plugins.pingcode.WTLogger;
import io.jenkins.plugins.pingcode.model.WTDeployEntity;
import io.jenkins.plugins.pingcode.model.WTEnvironmentEntity;
import io.jenkins.plugins.pingcode.model.WTEnvironmentSchema;
import io.jenkins.plugins.pingcode.model.WTRestException;
import io.jenkins.plugins.pingcode.service.WTRestService;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;
import org.jenkinsci.plugins.workflow.steps.*;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import java.io.IOException;
import java.io.Serializable;
import java.util.Set;

public class PCSendDeployStep extends Step implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String releaseName;

    private final String environmentName;

    @DataBoundSetter
    private String releaseURL;

    @DataBoundSetter
    private boolean failOnError;

    @DataBoundSetter
    private String status;

    @DataBoundSetter
    private boolean isTagged;

    @DataBoundConstructor
    public PCSendDeployStep(String releaseName, String environmentName) {
        this.releaseName = releaseName;
        this.environmentName = environmentName;
    }

    @Override
    public StepExecution start(StepContext context) throws Exception {
        return new PCSendDeployStepExecution(context, this);
    }

    public static class PCSendDeployStepExecution extends SynchronousNonBlockingStepExecution<Boolean> {
        private static final long serialVersionUID = 1L;

        private final PCSendDeployStep step;

        public PCSendDeployStepExecution(StepContext context, PCSendDeployStep step) {
            super(context);
            this.step = step;
        }

        @Override
        public Boolean run() throws Exception {
            WorkflowRun run = getContext().get(WorkflowRun.class);
            FilePath workspace = getContext().get(FilePath.class);
            TaskListener listener = getContext().get(TaskListener.class);

            WTLogger wtLogger = new WTLogger(listener);

            WTRestService service = new WTRestService();
            String envId = null;
            try {
                envId = handleEnvName(this.step.environmentName, service);
            } catch (Exception exception) {
                wtLogger.error(exception.getMessage());
                if (exception instanceof WTRestException) {
                    if (!((WTRestException) exception).getCode().equals("100105") && this.step.failOnError) {
                        throw new AbortException(exception.getMessage());
                    }
                } else if (this.step.failOnError) {
                    throw new AbortException(exception.getMessage());
                }
            }

            WTDeployEntity entity = WTDeployEntity.from(run, workspace, listener, this.step.status,
                    this.step.releaseName, this.step.releaseURL, envId, this.step.isTagged);

            wtLogger.info("Will send data to pingcode: " + entity.toString());
            try {
                service.createDeploy(entity);
                wtLogger.info("Create pingcode deploy record successfully.");
            } catch (Exception exception) {
                wtLogger.error(exception.getMessage());
                if (this.step.failOnError) {
                    throw new AbortException(exception.getMessage());
                }
            }
            return true;
        }

        public String handleEnvName(String name, WTRestService service) throws IOException, WTRestException {
            WTEnvironmentSchema schema = service.getEnvironmentByName(name);
            if (schema == null) {
                schema = service.createEnvironment(new WTEnvironmentEntity(name));
            }
            return schema.id;
        }
    }

    @Extension
    public static class DescriptorImpl extends StepDescriptor {
        @Override
        public Set<Class<?>> getRequiredContext() {
            return ImmutableSet.of(Run.class, EnvVars.class, TaskListener.class, FilePath.class);
        }

        @Override
        public String getFunctionName() {
            return "pingcodeDeployRecord";
        }

        @org.jetbrains.annotations.NotNull
        @Override
        public String getDisplayName() {
            return "Send deploy result to pingcode";
        }
    }
}
