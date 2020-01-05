/*******************************************************************************
Copyright 2019 Thomas Zierer

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*******************************************************************************/
package de.tgmz.ant.azure.transfer.test;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.BuildFileRule;
import org.junit.Rule;
import org.junit.Test;

public class TransferTest {
	@Rule
	public final BuildFileRule buildRule = new BuildFileRule();

	@Test
	public void happyDayTest() {
		buildRule.configureProject("build.xml");
		buildRule.executeTarget("common.clean");
		buildRule.executeTarget("common.download");
		buildRule.executeTarget("common.unzip");
		buildRule.executeTarget("common.zip");
		buildRule.executeTarget("common.upload");
		buildRule.executeTarget("common.delete");
	}

	@Test(expected = BuildException.class)
	public void invalidFileTest() {
		buildRule.configureProject("build.xml");
		buildRule.executeTarget("common.clean");
		buildRule.executeTarget("common.delete");
		buildRule.executeTarget("common.upload");	// Fails as the file does not exist
	}

	@Test(expected = BuildException.class)
	public void invalidContainerTest() {
		buildRule.configureProject("buildfail.xml");
		buildRule.executeTarget("common.download");	// Fails as the container reference is set to "INVALID"
	}

	@Test(expected = BuildException.class)
	public void noLocalOverwriteTest() {
		buildRule.configureProject("buildfail2.xml");
		buildRule.executeTarget("common.clean");
		buildRule.executeTarget("common.download");
		buildRule.executeTarget("common.download");	// Fails as the file exists and overwrite is set to "false"
	}

	@Test(expected = BuildException.class)
	public void noRemoteOverwriteTest() {
		buildRule.configureProject("buildfail2.xml");
		buildRule.executeTarget("common.clean");
		buildRule.executeTarget("common.download");
		buildRule.executeTarget("common.upload");	// Fails as the blob exists and overwrite is set to "false"
	}
}
