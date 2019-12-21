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
 * Task to upload a file to an azure blob storage.
 */
public class UploadTask extends TransferTask {
	private static final Logger LOG = LoggerFactory.getLogger(UploadTask.class);
	private File source;
	
	@Override
	public void execute() {
		try {
			if (LOG.isInfoEnabled()) {
				LOG.info("Uploading {} Mbytes from {} to {}"
						, NF.get().format(computeSize(source.toPath()))
						, source
						, getBlob().getUri());
			}
			
			long start = System.currentTimeMillis();
			
			getBlob().uploadFromFile(source.toString());

			if (LOG.isInfoEnabled()) {
				LOG.info("Uploading took {} msecs"
						, NF.get().format((System.currentTimeMillis() - start) / 1000d));
			}
		} catch (IOException | StorageException e) {
			throw new BuildException(e);
		}
	}
	public File getSource() {
		return source;
	}
	public void setSource(File source) {
		this.source = source;
	}
}
