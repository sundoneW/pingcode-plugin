package io.jenkins.plugins.pingcode.service;

import io.jenkins.plugins.pingcode.client.TokenClient;
import io.jenkins.plugins.pingcode.model.WTRestException;
import io.jenkins.plugins.pingcode.model.WTTokenEntity;
import io.jenkins.plugins.pingcode.resolver.TokenResolver;

import java.io.IOException;

public class WTTokenService implements TokenClient {
  private final TokenResolver tokenResolver;

  public WTTokenService(String baseURL, String clientId, String clientSecret) {
    this.tokenResolver = new TokenResolver(baseURL, clientId, clientSecret);
  }

  @Override
  public WTTokenEntity getTokenFromApi() throws IOException, WTRestException {
    return this.tokenResolver.resolveToken();
  }
}
