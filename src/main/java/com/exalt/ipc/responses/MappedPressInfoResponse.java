package com.exalt.ipc.responses;

import java.time.LocalDateTime;

public class MappedPressInfoResponse {
	private int id;

	private String name;

	private String address;

	private String description;

	private LocalDateTime creationDate;

	private String sizeLimit;

	private String residualSize;

	private int heldJobs;

	private int itemsLimit;

	private int jobsInProgress;

	private String expectedPrintingCompletionTime;

	private int finishedJobs;

	public MappedPressInfoResponse() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public String getSizeLimit() {
		return sizeLimit;
	}

	public void setSizeLimit(String sizeLimit) {
		this.sizeLimit = sizeLimit;
	}

	public String getResidualSize() {
		return residualSize;
	}

	public void setResidualSize(String residualSize) {
		this.residualSize = residualSize;
	}

	public int getHeldJobs() {
		return heldJobs;
	}

	public void setHeldJobs(int heldJobs) {
		this.heldJobs = heldJobs;
	}

	public int getItemsLimit() {
		return itemsLimit;
	}

	public void setItemsLimit(int itemsLimit) {
		this.itemsLimit = itemsLimit;
	}

	public int getJobsInProgress() {
		return jobsInProgress;
	}

	public void setJobsInProgress(int jobsInProgress) {
		this.jobsInProgress = jobsInProgress;
	}

	public String getExpectedPrintingCompletionTime() {
		return expectedPrintingCompletionTime;
	}

	public void setExpectedPrintingCompletionTime(String expectedPrintingCompletionTime) {
		this.expectedPrintingCompletionTime = expectedPrintingCompletionTime;
	}

	public int getFinishedJobs() {
		return finishedJobs;
	}

	public void setFinishedJobs(int finishedJobs) {
		this.finishedJobs = finishedJobs;
	}
}

