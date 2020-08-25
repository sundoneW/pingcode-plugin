package io.jenkins.plugins.pingcode.client;

import io.jenkins.plugins.pingcode.model.WTBuildEntity;
import io.jenkins.plugins.pingcode.model.WTRestException;

import java.io.IOException;

public interface BuildClient {
  Object createBuild(WTBuildEntity entity) throws IOException, WTRestException;
}
