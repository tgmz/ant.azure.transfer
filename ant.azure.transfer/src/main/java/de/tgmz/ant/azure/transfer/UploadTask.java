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
 * Task to upload a file to an azure blob storage.
 */
public class UploadTask extends TransferTask {
	private File source;
	
	@Override
	public void execute() {
		try {
			System.out.printf("Uploading %f Mbytes from %s to %s%n"
					, computeSize(source.toPath())
					, source
					, getBlob().getUri());
			
			long start = System.currentTimeMillis();
			
			getBlob().uploadFromFile(source.toString());

			System.out.printf("Uploading took %f secs%n", (System.currentTimeMillis() - start) / 1000d);
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
