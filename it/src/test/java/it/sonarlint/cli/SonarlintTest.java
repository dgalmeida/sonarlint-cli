/*
 * SonarSource :: IT :: SonarLint CLI
 * Copyright (C) 2016 SonarSource
 * sonarqube@googlegroups.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package it.sonarlint.cli;

import it.sonarlint.cli.tools.SonarlintCli;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

public class SonarlintTest {
  @ClassRule
  public static SonarlintCli sonarlint = new SonarlintCli();

  @Before
  public void setUp() {
    sonarlint.install();
  }

  @Test
  public void testHelp() {
    int code = sonarlint.run("-h");
    assertThat(sonarlint.getOut()).contains("usage: sonarlint");
    assertThat(code).isEqualTo(0);
  }

  @Test
  public void testVersion() {
    int code = sonarlint.run("-v");
    String version = System.getProperty("sonarlint.version");
    assertThat(sonarlint.getOut()).contains(version);
    assertThat(code).isEqualTo(0);
  }

  @Test
  public void testInvalidArg() {
    int code = sonarlint.run("-q");
    assertThat(sonarlint.getErr()).contains("Error parsing arguments");
    assertThat(code).isEqualTo(1);
  }

  @Test
  public void testPluginsInstalled() throws IOException {
    Path installation = sonarlint.getSonarlintInstallation();
    Path plugins = installation.resolve("plugins");
    assertThat(Files.isDirectory(plugins)).isTrue();
    assertThat(Files.newDirectoryStream(plugins)).isNotEmpty();
  }
  
  @Test
  public void testNoFilesToAnalyse() throws IOException {
    int code = sonarlint.runProject("empty");
    assertThat(code).isEqualTo(0);
    assertThat(sonarlint.getOut()).contains("WARN: No files to analyze");
  }
}
