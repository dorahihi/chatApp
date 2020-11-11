package com.chatApp.sp.model;

import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "group")
public class DBGroup {
	
	@Id
	private String id;
	
	@Indexed
	private String groupId;
	private Map<String, String> members;
	private String manager;
	private String groupName;
	
	public DBGroup() {
		
	}
	
	public DBGroup( String manager, String groupName) {
		this.groupId = groupName.toLowerCase() + System.currentTimeMillis();
		this.manager = manager;
		this.groupName = groupName;
	}
	
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}



	public Map<String, String> getMembers() {
		return this.members;
	}

	public void setMembers(Map<String, String> members) {
		this.members = members;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	
}
