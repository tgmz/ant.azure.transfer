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

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.text.DecimalFormat;
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
    
	/** The connection string for authentication to azure */
	private String connectionString;
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
	
	public String getConnectionString() {
		return connectionString;
	}
	public void setConnectionString(String connectionString) {
		this.connectionString = connectionString;
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
}
