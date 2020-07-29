package com.exalt.ipc.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.exalt.ipc.utilities.Constants.IPC_QUEUE_LIMIT;

@Entity
public class IPC {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@DateTimeFormat
	private LocalDateTime creationDate;

	@Min(5)
	@Max(100)
	private int queueLimit;

	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY,
						cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "user_id")
	private User user;

	@NotBlank
	@NotNull
	private String address;

	@JsonIgnore
	//	@OneToOne(fetch = FetchType.LAZY, mappedBy = "ipc",
	//						cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "press_id")
	private Press press;


	public IPC() {
		this.creationDate = LocalDateTime.now();
		this.queueLimit = IPC_QUEUE_LIMIT;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@JsonIgnore
	public Map<String, Object> getIPCMap() {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("id", getId());
		map.put("creationDate", getCreationDate());
		map.put("queueLimit", getQueueLimit());
		return map;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public int getQueueLimit() {
		return queueLimit;
	}

	public void setQueueLimit(int queueLimit) {
		this.queueLimit = queueLimit;
	}

	public Press getPress() {
		return press;
	}

	public void setPress(Press press) {
		this.press = press;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
