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
package de.tgmz.ant.azure.transfer;

import java.io.File;
import java.io.IOException;

import org.apache.tools.ant.BuildException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.azure.storage.StorageException;

/**
 * Task to download a file from an azure blob storage.
 */
public class DownloadTask extends TransferTask {
	private static final Logger LOG = LoggerFactory.getLogger(DownloadTask.class);
	private File destination;
	
	@Override
	public void execute() {
		try {
			if (LOG.isInfoEnabled()) {
				LOG.info("Downloading from {} to {}"
						, getBlob().getUri()
						, destination);
			}
					
			long start = System.currentTimeMillis();
					
			getBlob().downloadToFile(destination.toString());

			if (LOG.isInfoEnabled()) {
				LOG.info("Downloading {} Mbytes took {} secs"
						, NF.get().format(computeSize(destination.toPath()))
						, NF.get().format((System.currentTimeMillis() - start) / 1000d));
			}
		} catch (StorageException | IOException e) {
			throw new BuildException(e);
		}
	}
	public File getDestination() {
		return destination;
	}
	public void setDestination(File destination) {
		this.destination = destination;
	}
}
