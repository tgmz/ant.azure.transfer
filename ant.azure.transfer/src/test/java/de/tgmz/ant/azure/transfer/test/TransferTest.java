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

import org.apache.tools.ant.BuildFileRule;
import org.junit.Rule;
import org.junit.Test;

public class TransferTest {
	@Rule
	public final BuildFileRule buildRule = new BuildFileRule();

	@Test
	public void happyDayTest() {
		buildRule.configureProject("build.xml");
		buildRule.executeTarget("clean");
		buildRule.executeTarget("download");
		buildRule.executeTarget("unzip");
		buildRule.executeTarget("zip");
		buildRule.executeTarget("upload");
		buildRule.executeTarget("delete");
	}
}
