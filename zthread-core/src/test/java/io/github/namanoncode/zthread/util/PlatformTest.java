package io.github.namanoncode.zthread.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.namanoncode.zthread.exception.ConfigurationException;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

class PlatformTest {

  @Test
  @EnabledOnOs(OS.LINUX)
  void detectsLinux() {
    assertThat(Platform.isLinux()).isTrue();
  }

  @Test
  @EnabledOnOs(OS.LINUX)
  void ensureLinuxDoesNotThrow() {
    Platform.ensureLinux();
  }

  @Test
  void architectureDetection() {
    assertThat(Platform.OS_ARCH).isNotBlank();
  }

  @Test
  void ensureSupportedArchitectureAcceptsX8664() {
    Platform.ensureSupportedArchitecture("x86_64");
    Platform.ensureSupportedArchitecture("amd64");
  }

  @Test
  void ensureSupportedArchitectureRejectsOthers() {
    assertThatThrownBy(() -> Platform.ensureSupportedArchitecture("aarch64"))
        .isInstanceOf(ConfigurationException.class)
        .hasMessageContaining("aarch64");
  }

  @Test
  void ensureSupportedArchitectureOnCurrentMachine() {
    // Exercises the real no-arg entry point that LinuxRuntimeFactory calls.
    // This project's CI currently only runs on x86_64, so skip rather than
    // fail on any other architecture instead of asserting a result either way.
    Assumptions.assumeTrue(Platform.isX8664(), "CI currently only runs on x86_64");
    Platform.ensureSupportedArchitecture();
  }
}
