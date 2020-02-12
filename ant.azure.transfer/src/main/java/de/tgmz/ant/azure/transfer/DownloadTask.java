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

import com.microsoft.azure.storage.StorageException;

/**
 * Task to download a file from an azure blob storage.
 */
public final class DownloadTask extends TransferTask {
	/** The destination file. */
	private File destination;

	@Override
	public void execute() {
		try {
			if (destination.exists() && !isOverwrite()) {
				throw new BuildException("File " + destination + " exists");
			}

			if (destination.isDirectory()) {
				throw new BuildException("The destination " + destination + " is a directory");
			}
		} catch (SecurityException e) {
			throw new BuildException("A security exception occurred during access of " + destination, e);
		}

		log("Downloading from " + getBlob().getUri() + " to " + destination);

		long start = System.currentTimeMillis();

		try {
			getBlob().downloadToFile(destination.toString());

			log("Downloading " + computeSize(destination.toPath()) + " Mbytes took " + ((System.currentTimeMillis() - start) / THOUSAND) + " secs");
		} catch (StorageException | IOException e) {
			throw new BuildException(e);
		}
	}

	/**
	 * @param aDestination the destination to set
	 */
	public void setDestination(final File aDestination) {
		this.destination = aDestination;
	}
}
