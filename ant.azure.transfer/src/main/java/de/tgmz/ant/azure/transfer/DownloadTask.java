/*******************************************************************************
  * Copyright (c) 16.12.2019 Thomas Zierer.
  * All rights reserved. This program and the accompanying materials
  * are made available under the terms of the Eclipse Public License v2.0
  * which accompanies this distribution, and is available at
  * http://www.eclipse.org/legal/epl-v20.html
  *
  * Contributors:
  *    Thomas Zierer - initial API and implementation and/or initial documentation
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
