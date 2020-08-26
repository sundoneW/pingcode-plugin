package io.jenkins.plugins.pingcode.client;

import io.jenkins.plugins.pingcode.model.WTRestException;
import io.jenkins.plugins.pingcode.model.WTTokenEntity;

import java.io.IOException;

public interface TokenClient {
  WTTokenEntity getTokenFromApi() throws IOException, WTRestException;
}
