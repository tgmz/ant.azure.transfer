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
import java.text.MessageFormat;

import org.apache.tools.ant.BuildException;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

/**
 * Common functionalities.
 */
public abstract class TransferTask extends org.apache.tools.ant.Task {
	/** 1000. */
	protected static final double THOUSAND = 1000d;
	/** Mebibyte. */
	private static final double MB = 1024d * 1024d;
	/** Default endpoint protocol. */
	private String defaultEndpointsProtocol = "https";
	/** Azure account name. */
	private String accountName;
	/** Base64 encoded Azure account key. */
	private String accountKey;
	/** Default endpoint suffix. */
	private String endpointSuffix = "core.windows.net";
	/** The container on Azure. */
	private String containerReference;
	/** The name of the file on Azure. */
	private String fileName;
	/** The blob. */
	private CloudBlockBlob blob;
	/** Overwrite file (download) or blob (upload). */
	private boolean overwrite = true;

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

	/**
	 * Computes the size of a path in MB.
	 * @param p the path
	 * @return the size of the path in MB
	 * @throws IOException if the path does not denote a file or for other reasons.
	 */
	protected final double computeSize(final Path p) throws IOException {
		try (FileChannel fc = FileChannel.open(p)) {
			return fc.size() / MB;
		}
	}

	/**
	 * Computes the connection string required by the azure-storage libraries.
	 * @return the Azure connection string
	 */
	private String getConnectionString() {
		//CHECKSTYLE DISABLE NoWhitespaceBefore
		return MessageFormat.format("DefaultEndpointsProtocol={0};AccountName={1};AccountKey={2};EndpointSuffix={3}"
				, getDefaultEndpointsProtocol()
				, getAccountName()
				, getAccountKey()
				, getEndpointSuffix());
		//CHECKSTYLE ENABLE NoWhitespaceBefore
	}

	/**
	 * @return the defaultEndpointsProtocol
	 */
	public final String getDefaultEndpointsProtocol() {
		return defaultEndpointsProtocol;
	}

	/**
	 * @param aDefaultEndpointsProtocol the defaultEndpointsProtocol to set
	 */
	public final void setDefaultEndpointsProtocol(final String aDefaultEndpointsProtocol) {
		this.defaultEndpointsProtocol = aDefaultEndpointsProtocol;
	}

	/**
	 * @return the accountName
	 */
	public final String getAccountName() {
		return accountName;
	}

	/**
	 * @param aAccountName the accountName to set
	 */
	public final void setAccountName(final String aAccountName) {
		this.accountName = aAccountName;
	}

	/**
	 * @return the accountKey
	 */
	public final String getAccountKey() {
		return accountKey;
	}

	/**
	 * @param aAccountKey the accountKey to set
	 */
	public final void setAccountKey(final String aAccountKey) {
		this.accountKey = aAccountKey;
	}

	/**
	 * @return the endpointSuffix
	 */
	public final String getEndpointSuffix() {
		return endpointSuffix;
	}

	/**
	 * @param aEndpointSuffix the endpointSuffix to set
	 */
	public final void setEndpointSuffix(final String aEndpointSuffix) {
		this.endpointSuffix = aEndpointSuffix;
	}

	/**
	 * @return the containerReference
	 */
	public final String getContainerReference() {
		return containerReference;
	}

	/**
	 * @param aContainerReference the containerReference to set
	 */
	public final void setContainerReference(final String aContainerReference) {
		this.containerReference = aContainerReference;
	}

	/**
	 * @return the fileName
	 */
	public final String getFileName() {
		return fileName;
	}

	/**
	 * @param aFileName the fileName to set
	 */
	public final void setFileName(final String aFileName) {
		this.fileName = aFileName;
	}

	/**
	 * @return the overwrite
	 */
	public final boolean isOverwrite() {
		return overwrite;
	}

	/**
	 * @param aOverwrite the overwrite to set
	 */
	public final void setOverwrite(final boolean aOverwrite) {
		this.overwrite = aOverwrite;
	}
}
