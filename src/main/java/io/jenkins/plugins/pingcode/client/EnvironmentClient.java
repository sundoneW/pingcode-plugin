package io.jenkins.plugins.pingcode.client;

import io.jenkins.plugins.pingcode.model.WTEnvironmentEntity;
import io.jenkins.plugins.pingcode.model.WTEnvironmentSchema;
import io.jenkins.plugins.pingcode.model.WTPaginationResponse;
import io.jenkins.plugins.pingcode.model.WTRestException;

import java.io.IOException;

public interface EnvironmentClient {
  WTPaginationResponse<WTEnvironmentSchema> listEnvironments() throws IOException, WTRestException;

  WTEnvironmentSchema getEnvironmentByName(String name) throws IOException, WTRestException;

  WTEnvironmentSchema deleteEnvironment(String id) throws IOException, WTRestException;

  WTEnvironmentSchema createEnvironment(WTEnvironmentEntity entity)
      throws IOException, WTRestException;
}
