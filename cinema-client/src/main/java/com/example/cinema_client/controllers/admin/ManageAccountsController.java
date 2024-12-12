package com.example.cinema_client.controllers.admin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.cinema_client.constants.Api;
import com.example.cinema_client.models.BranchDTO;
import com.example.cinema_client.models.JwtResponseDTO;
import com.example.cinema_client.models.MovieDTO;
import com.example.cinema_client.models.Role;
import com.example.cinema_client.models.RoomDTO;
import com.example.cinema_client.models.ScheduleDTO;
import com.example.cinema_client.models.TicketDTO;
import com.example.cinema_client.models.User;

@Controller
@RequestMapping("/admin/accounts")
public class ManageAccountsController {
    @Autowired
    private RestTemplate restTemplate;
    private static final String API_ACCOUNTS = Api.baseURL+"/api/admin/accounts";
    private static final String API_COUNT_PAGE = Api.baseURL+"/api/admin/accounts/count-page";
    private static final String API_PAGING_ACCOUNTS = Api.baseURL+"/api/admin/accounts/paging";
    private static final String API_GET_ROLES = Api.baseURL+"/api/admin/roles";
    private static final String API_GET_USER = Api.baseURL+"/api/account";
    @GetMapping
    public String displayManageAccountPage(HttpSession session,Model model,			
			@RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
		    @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
		    @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort){
        //Gắn access token jwt vào header để gửi kèm request
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        JwtResponseDTO jwtResponseDTO = (JwtResponseDTO)session.getAttribute("jwtResponse");
        headers.set(HttpHeaders.AUTHORIZATION,"Bearer "+jwtResponseDTO.getAccessToken());
        HttpEntity<?> entity = new HttpEntity<>(headers);
        // Gọi api phân trang
        String urlTemplate = UriComponentsBuilder.fromHttpUrl(API_PAGING_ACCOUNTS)
                .queryParam("page", "{page}")
                .queryParam("size", "{size}")
                .queryParam("sort", "{sort}")
                .encode()
                .toUriString();
        Map<String,String> listRequestParam = new HashMap<>();
        listRequestParam.put("page", page+"");
        listRequestParam.put("size", size+"");
        listRequestParam.put("sort", sort+"");
    	ResponseEntity<User[]> response = restTemplate.exchange(urlTemplate,HttpMethod.GET,entity,User[].class,listRequestParam);
    	User[] users = response.getBody();
    	//gọi api lấy ra role trong table
	    ResponseEntity<Role[]> responseRole = restTemplate.exchange(API_GET_ROLES,HttpMethod.GET,entity,Role[].class);
	    Role[] roles = responseRole.getBody();
	    urlTemplate = UriComponentsBuilder.fromHttpUrl(API_COUNT_PAGE)
                .queryParam("size", "{size}")
                .encode()
                .toUriString();
	    ResponseEntity<Long> responseCountPage = restTemplate.exchange(urlTemplate,HttpMethod.GET,entity,Long.class,listRequestParam);
	    long countPage = responseCountPage.getBody();
	    model.addAttribute("users",users);
	    model.addAttribute("roles",roles);
	    model.addAttribute("modelUser",new User());
	    model.addAttribute("countPage",countPage);
	    model.addAttribute("page",page);
	    model.addAttribute("size",size);
        return "admin/manage-account";
    }
    @GetMapping("/update")
    public String displayUpdateAccountPage(@RequestParam Integer userId,HttpSession session,Model model){
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        JwtResponseDTO jwtResponseDTO = (JwtResponseDTO)session.getAttribute("jwtResponse");
        headers.set(HttpHeaders.AUTHORIZATION,"Bearer "+jwtResponseDTO.getAccessToken());
        HttpEntity<?> entity = new HttpEntity<>(headers);

        // Gọi api lấy ra user by id
        String urlTemplate = UriComponentsBuilder.fromHttpUrl(API_GET_USER)
                .queryParam("userId", "{userId}")
                .encode()
                .toUriString();
        Map<String,String> listRequestParam = new HashMap<>();
        listRequestParam.put("userId", userId+"");
        
        ResponseEntity<User> response = restTemplate.exchange(urlTemplate, HttpMethod.GET,entity,User.class
        ,listRequestParam);
        User user=response.getBody();
	    model.addAttribute("user",user);
	    //Gọi API lấy ra list role
	    ResponseEntity<Role[]> responseRole = restTemplate.exchange(API_GET_ROLES,HttpMethod.GET,entity,Role[].class);
	    Role[] roles = responseRole.getBody();
	    model.addAttribute("roles",roles);
        return "admin/update-account";
    }
    @PostMapping("/update")
    public String updateAccountPage(@ModelAttribute("user") User user,@RequestParam(name = "role", required = false) List<String> role,
    		@RequestParam String newPassword ,HttpSession session,Model model){
        //Gắn access token jwt vào header để gửi kèm request
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        JwtResponseDTO jwtResponseDTO = (JwtResponseDTO)session.getAttribute("jwtResponse");
        headers.set(HttpHeaders.AUTHORIZATION,"Bearer "+jwtResponseDTO.getAccessToken());
        try {
        	Set<Role> roles =new HashSet<>();
        	if(role!=null) {
	        	for (String r: role) {
	        		Role ro=new Role();
	        		ro.setName(r);
	        		roles.add(ro);
	        	}
        	}
        	user.setRoles(roles);
        	HttpEntity<User> entity = new HttpEntity<>(user,headers);
            String urlTemplate = UriComponentsBuilder.fromHttpUrl(API_ACCOUNTS)
                    .queryParam("newPassword", "{newPassword}")
                    .encode()
                    .toUriString();
            Map<String,String> listRequestParam = new HashMap<>();
            listRequestParam.put("newPassword", newPassword);
        	ResponseEntity<String> response = restTemplate.exchange(urlTemplate,HttpMethod.PUT, entity, String.class,listRequestParam);
		} catch (Exception e) {
			HttpEntity<?> entity = new HttpEntity<>(headers);
		    ResponseEntity<Role[]> responseRole = restTemplate.exchange(API_GET_ROLES,HttpMethod.GET,entity,Role[].class);
		    Role[] roles = responseRole.getBody();
		    model.addAttribute("roles",roles);
			model.addAttribute("error",e.getMessage());
			return "admin/update-account";
		}
        return "redirect:/admin/accounts";
    }
    @GetMapping("/add")
    public String displayAddAccountPage(HttpSession session,Model model){
        //Gắn access token jwt vào header để gửi kèm request
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        JwtResponseDTO jwtResponseDTO = (JwtResponseDTO)session.getAttribute("jwtResponse");
        headers.set(HttpHeaders.AUTHORIZATION,"Bearer "+jwtResponseDTO.getAccessToken());
        HttpEntity<?> entity = new HttpEntity<>(headers);
	    //Gọi API lấy ra list role
	    ResponseEntity<Role[]> responseRole = restTemplate.exchange(API_GET_ROLES,HttpMethod.GET,entity,Role[].class);
	    Role[] roles = responseRole.getBody();
	    model.addAttribute("roles",roles);
	    model.addAttribute("user",new User());
        return "admin/add-account";
    }
    @PostMapping("/add")
    public String addAccountPage(@ModelAttribute("user") User user,@RequestParam(name = "role", required = false) List<String> role ,HttpSession session,Model model){
        //Gắn access token jwt vào header để gửi kèm request
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        JwtResponseDTO jwtResponseDTO = (JwtResponseDTO)session.getAttribute("jwtResponse");
        headers.set(HttpHeaders.AUTHORIZATION,"Bearer "+jwtResponseDTO.getAccessToken());
        try {
        	Set<Role> roles =new HashSet<>();
        	if(role!=null) {
	        	for (String r: role) {
	        		Role ro=new Role();
	        		ro.setName(r);
	        		roles.add(ro);
	        	}
        	}
        	user.setRoles(roles);
        	HttpEntity<User> entity = new HttpEntity<>(user,headers);
        	ResponseEntity<String> response = restTemplate.exchange(API_ACCOUNTS,HttpMethod.POST, entity, String.class);
		} catch (Exception e) {
			HttpEntity<?> entity = new HttpEntity<>(headers);
		    ResponseEntity<Role[]> responseRole = restTemplate.exchange(API_GET_ROLES,HttpMethod.GET,entity,Role[].class);
		    Role[] roles = responseRole.getBody();
		    model.addAttribute("roles",roles);
			model.addAttribute("error",e.getMessage());
			return "admin/add-account";
		}
	    
        return "redirect:/admin/accounts";
    }
}
