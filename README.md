# ant.azure.transfer
Ant tasks to transfer an azure blob storage from/to the local file system.

Use it e.g. to cache your Maven repository in your Azure DevOps Maven builds so you don't have to download the dependencies every time. 

*   Surround your Maven build with two ant steps
*   In the first step download your zipped Maven repository from your azure storage to a temp file and extract its contents to ${user.home}/.m2
*	Run your Maven build
*	Zip ${user.home}/.m2 to a temporary file and upload it to your Azure storage.
*	The second build should succeed without any dependency downloads.
*	As long as your pom(s) do not change you may of course skip the second step.

Have a look at the provided examples
*	build.xml: Run targets "download unzip delete" in the first step and "zip upload delete" in the second
*	azure-pipelines.yml: Same as YAML
