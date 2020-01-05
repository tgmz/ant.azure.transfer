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
import com.microsoft.azure.storage.blob.CloudBlockBlob;

/**
 * Task to upload a file to an azure blob storage.
 */
public class UploadTask extends TransferTask {
	private File source;
	
	@Override
	public void execute() {
		if (!source.exists() || !source.isFile()) {
			throw new BuildException("The source " + source + " does not exist or is not a regular file");
		}
		
		CloudBlockBlob b = getBlob();
		
		long start = System.currentTimeMillis();
		
		try {
			if (b.exists() && !isOverwrite()) {
				throw new BuildException("Blob " + b.getUri() + " exists");
			}
			
			log("Uploading " + computeSize(source.toPath()) + " Mbytes from " + source + " to " + getBlob().getUri());
			
			getBlob().uploadFromFile(source.toString());
		} catch (IOException | StorageException e) {
			throw new BuildException(e);
		}

		log("Uploading took " + (System.currentTimeMillis() - start) / 1000d + " secs");
	}
	public File getSource() {
		return source;
	}
	public void setSource(File source) {
		this.source = source;
	}
}
