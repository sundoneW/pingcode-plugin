package io.jenkins.plugins.pingcode.model;

import io.jenkins.plugins.pingcode.WTHelper;

public class WTTokenEntity {
  public String accessToken;
  public String tokenType;
  public long expiresIn;

  public boolean isExpired() {
    return expiresIn < WTHelper.toSafeTs(System.currentTimeMillis());
  }
}
