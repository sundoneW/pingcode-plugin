package io.jenkins.plugins.worktile;

import hudson.model.TaskListener;

import java.io.Serializable;

public class WTLogger implements Serializable {
  private static final long serialVersionUID = 1L;

  private final TaskListener listener;

  public WTLogger(TaskListener listener) {
    this.listener = listener;
  }

  public void info(String message) {
    this.listener.getLogger().println("PINGCODE - [INFO] " + message);
  }

  public void error(String message) {
    this.listener.getLogger()
        .println("PINGCODE - [ERROR] This is probably a problem with pingcode plugin, verbose information as blow");
    this.listener.getLogger().println("WT - [ERROR] " + message);
    this.listener.getLogger().println(
        "PINGCODE - [ERROR] please issue this problem at " + "https://github.com/jenkinsci/pingcode-plugin/issues");
  }
}
