package com.chatApp.sp.utils;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.chatApp.sp.model.DBGroup;
import com.chatApp.sp.model.DBUser;
import com.chatApp.sp.repository.GroupRepository;
import com.chatApp.sp.repository.UserRepository;
import com.chatApp.sp.service.CookieServices;

@Component
public class UserUtils {

	@Autowired
	UserRepository userRepo;
	
	@Autowired
	GroupRepository groupRepo;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	CookieServices cookieServices;
	
	
	public String createUser(String email, String userName, String password, String age, String gender) throws Exception{
		
		if(userRepo.findByEmail(email) == null) {
			DBUser user = new DBUser(email, passwordEncoder.encode(password), age, gender, userName);
			
			System.out.println("new user password: "+user.getPassword());
			userRepo.insert(user);
			
			return "SUCCEED";
		}else {
			throw new Exception(email + " already exist!!");
		}
	}
	
	public DBUser viewUserProfile(String email) throws Exception {
		DBUser user = userRepo.findByEmail(email);
		System.out.println("UserUtils - - viewUserProfile--email: "+email);
		System.out.println("UserUtils - - viewUserProfile--user: "+user);
		if(user != null) {
			user.setPassword("");
			return user;
		}else throw new Exception("User does not exist!");
	}
	
	public String updateUserProfile(String password, String userName, String age, HttpServletRequest req) {
		
		String email = cookieServices.getEmail(req);
		
		System.out.println("email là: "+email);
		
		DBUser user = userRepo.findByEmail(email);
		
		if(user != null) {
			if(password != null && !password.equals("")) {
				System.out.println("password changed!");
				user.setPassword(passwordEncoder.encode(password));
			}
			if(userName != null && !userName.equals("")) {
				user.setUserName(userName);
				Map<String, String> friends = user.getFriend();
				
				for(Map.Entry<String, String> f: friends.entrySet()) {
					DBUser other =  userRepo.findByEmail(f.getKey());
					Map<String,String> friend = other.getFriend();
					friend.put(user.getEmail(), userName);
					other.setFriend(friend);
					userRepo.save(other);
				}
				
				Map<String, String> groups = user.getGroup();
				
				for(Map.Entry<String, String> g: groups.entrySet()) {
					DBGroup other =  groupRepo.findByGroupId(g.getKey());
					Map<String, String> members = other.getMembers();
					members.put(user.getEmail(), userName);
					other.setMembers(members);
					groupRepo.save(other);
				}
				
				System.out.println("userName changed");
			}
			if(age != null && !age.equals("")) {
				user.setAge(age);
				System.out.println("age changed");
			}
			userRepo.save(user);
			return "SUCCEED";
		}else {
			System.out.println("user không tồn tại");
		}
		return null;
	}
	
	public String updateUserProfile(String password, String userName, String age, String email) {
				
		System.out.println("email là: "+email);
		
		DBUser user = userRepo.findByEmail(email);
		
		if(user != null) {
			if(password != null && !password.equals("")) {
				System.out.println("password changed!");
				user.setPassword(passwordEncoder.encode(password));
			}
			if(userName != null && !userName.equals("")) {
				user.setUserName(userName);
				Map<String, String> friends = user.getFriend();
				
				for(Map.Entry<String, String> f: friends.entrySet()) {
					DBUser other =  userRepo.findByEmail(f.getKey());
					Map<String,String> friend = other.getFriend();
					friend.put(user.getEmail(), userName);
					other.setFriend(friend);
					userRepo.save(other);
				}
				
				Map<String, String> groups = user.getGroup();
				
				for(Map.Entry<String, String> g: groups.entrySet()) {
					DBGroup other =  groupRepo.findByGroupId(g.getKey());
					Map<String, String> members = other.getMembers();
					members.put(user.getEmail(), userName);
					other.setMembers(members);
					groupRepo.save(other);
				}
				System.out.println("userName changed");
			}
			if(age != null && !age.equals("")) {
				user.setAge(age);
				System.out.println("age changed");
			}
			userRepo.save(user);
			return "SUCCEED";
		}else {
			System.out.println("user không tồn tại");
		}
		return null;
	}
	
	public String deleteAccount(HttpServletRequest req) {
		String email = cookieServices.getEmail(req);
		
		DBUser user = userRepo.findByEmail(email);
		
		if(user != null) {
			userRepo.delete(user);
			return "SUCCEED";
		}
		
		return null;
	}
	
	public String deleteAccount(String email) {
		
		DBUser user = userRepo.findByEmail(email);
		
		if(user != null) {
			userRepo.delete(user);
			return "SUCCEED";
		}
		
		return null;
	}
	
}
