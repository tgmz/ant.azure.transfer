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

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;

import org.apache.tools.ant.BuildException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

/**
 * Common functionalities.
 */
public abstract class TransferTask extends org.apache.tools.ant.Task {
	private static final Logger LOG = LoggerFactory.getLogger(TransferTask.class);
	protected static final ThreadLocal<NumberFormat> NF = ThreadLocal.withInitial(DecimalFormat::new);

	private String defaultEndpointsProtocol="https";
	private String accountName;
	private String accountKey;
	private String endpointSuffix="core.windows.net";
	/** The container on azure */
	private String containerReference;
	/** The file */
	private String fileName;
	/** The blob */
	private CloudBlockBlob blob; 
	
	/**
	 * @return the blob
	 */
	protected CloudBlockBlob getBlob() {
		if (blob == null) {
			try {
				CloudStorageAccount storageAccount = CloudStorageAccount.parse(getConnectionString());
				CloudBlobClient	blobClient = storageAccount.createCloudBlobClient();
				CloudBlobContainer container = blobClient.getContainerReference(getContainerReference());
				blob = container.getBlockBlobReference(getFileName());
			} catch (InvalidKeyException | URISyntaxException | StorageException e) {
				throw new BuildException(e);
			}
		}
		
		return blob;
	}
	
	protected double computeSize(Path p) throws IOException {
		if (!p.toFile().exists()) {
			LOG.warn("The file {} does not exist", p);
			
			return 0d;
		}
			
		if (!p.toFile().isFile()) {
			LOG.warn("{} is not a regular file", p);
				
			return 0d;
		}
		
		try (FileChannel fc = FileChannel.open(p)) {
			return (double) fc.size() / (1024 * 1024);
		}
	}
	
	private String getConnectionString() {
		return MessageFormat.format("DefaultEndpointsProtocol={0};AccountName={1};AccountKey={2};EndpointSuffix={3}"
				, getDefaultEndpointsProtocol()
				, getAccountName()
				, getAccountKey()
				, getEndpointSuffix());
	}
	public String getContainerReference() {
		return containerReference;
	}
	public void setContainerReference(String containerReference) {
		this.containerReference = containerReference;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDefaultEndpointsProtocol() {
		return defaultEndpointsProtocol;
	}

	public void setDefaultEndpointsProtocol(String defaultEndpointsProtocol) {
		this.defaultEndpointsProtocol = defaultEndpointsProtocol;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountKey() {
		return accountKey;
	}

	public void setAccountKey(String accountKey) {
		this.accountKey = accountKey;
	}

	public String getEndpointSuffix() {
		return endpointSuffix;
	}

	public void setEndpointSuffix(String endpointSuffix) {
		this.endpointSuffix = endpointSuffix;
	}
}
