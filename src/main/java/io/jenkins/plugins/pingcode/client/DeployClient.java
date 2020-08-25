package io.jenkins.plugins.pingcode.client;

import io.jenkins.plugins.pingcode.model.WTDeployEntity;
import io.jenkins.plugins.pingcode.model.WTRestException;

import java.io.IOException;

public interface DeployClient {
  Object createDeploy(WTDeployEntity entity) throws IOException, WTRestException;
}
