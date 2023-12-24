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

import java.util.Arrays;
import java.util.Collection;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.BuildFileRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class TransferFailedTest {
	@Rule
	public final BuildFileRule buildRule = new BuildFileRule();
	private String project;
	private String[] targets;

	public TransferFailedTest(String project, String... targets) {
		super();
		this.project = project;
		this.targets = targets;
	}
	
	@Test(expected = BuildException.class)
	public void invalidFileTest() {
		buildRule.configureProject(project);
		
		for (String target : targets) {
			buildRule.executeTarget(target);
		}
	}
	
	@Parameters(name = "{index}: Check for project [{0}]")
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] {
				{ "build.xml", new String[] {"common.clean"
						, "common.delete"
						, "common.upload" }},	// Fails as the file does not exist
				{ "buildfail.xml", new String[] {"common.download" }},	// Fails as the container reference is set to "INVALID"
				{ "buildfail2.xml", new String[] {"common.clean"
						, "common.download"
						, "common.download" }},	// Fails as the file exists and overwrite is set to "false"
		};
		return Arrays.asList(data);
	}
	
}
