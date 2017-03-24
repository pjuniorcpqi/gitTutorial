package com.cpqi.andes.controller;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cpqi.andes.mailer.AndesMailer;
import com.cpqi.andes.model.AccessLevel;
import com.cpqi.andes.model.User;
import com.cpqi.andes.model.actionenum.AuditAction;
import com.cpqi.andes.service.AccessLevelService;
import com.cpqi.andes.service.UserService;
import com.cpqi.andes.viewmodel.ResponseViewModel;
import com.google.common.collect.Lists;

// TODO: Auto-generated Javadoc
/**
 * <p>
 * UserConroller
 * </p>
 * .
 *
 * @author Yury Silva <ysilva@cpqi.com>
 * @version 0.1
 * @since 0.1
 */
@RestController
public class UserController extends AbstractController {
    
    private static final Logger	 LOGGER	       = Logger.getLogger(UserController.class);
    
    /** USER - Access Level. */
    private static final String	 USER	       = "User";
    
    /** The Constant EMAIL_PATTERN. */
    private static final String	 EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@cpqi.com$";
    
    /** The Constant pattern. */
    private static final Pattern pattern       = Pattern.compile(EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);
    
    /** The Constant matcher. */
    private static Matcher	 matcher       = null;
    
    /** The service. */
    @Autowired
    private UserService		 service;
    
    /** The access level. */
    @Autowired
    private AccessLevelService	 accessLevel;
    
    public UserController() {
	
    }
    
    /**
     * Retrieve All Users.
     *
     * @return a response
     */
    @RequestMapping(value = "/users/", method = RequestMethod.GET, produces = "application/json")
    @PreAuthorize("isAdmin()")
    public ResponseEntity<List<User>> getAll() {
	long mark = new Date().getTime();
	logInfoMethodEntry(mark, LOGGER, "getAll", AuditAction.READ, SecurityContextHolder.getContext().getAuthentication());
	
	List<User> users = Lists.newArrayList(service.findAll());
	
	for (User u : users) {
	    u.setPassword("");
	}
	
	if (users.isEmpty()) {
	    
	    logInfoMethodExit(mark, LOGGER, "getAll", AuditAction.READ);
	    return new ResponseEntity<List<User>>(HttpStatus.NO_CONTENT);
	} else {
	    
	    logInfoMethodExit(mark, LOGGER, "getAll", AuditAction.READ);
	    return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}
    }
    
    /**
     * Retrieve a user.
     *
     * @param idUser
     *            the id user
     * @return a response
     */
    @RequestMapping(value = "/getuser/", method = RequestMethod.GET, produces = "application/json")
    @PreAuthorize("isAdmin() or isOwner(#idUser)")
    public ResponseEntity<User> getByEmail(@RequestParam long idUser) {
	long mark = new Date().getTime();
	logInfoMethodEntry(mark, LOGGER, "getByEmail", AuditAction.READ, SecurityContextHolder.getContext().getAuthentication(), idUser);
	User user = service.findById(idUser);
	
	if (user == null) {
	    
	    logInfoMethodExit(mark, LOGGER, "getByEmail", AuditAction.READ);
	    return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
	    
	} else {
	    user.setPassword("");
	    
	    logInfoMethodExit(mark, LOGGER, "getByEmail", AuditAction.READ);
	    return new ResponseEntity<User>(user, HttpStatus.OK);
	}
    }
    
    /**
     * Create a User.
     *
     * @param user
     *            the user
     * @return the response entity
     */
    @RequestMapping(value = "/user/", method = RequestMethod.POST, produces = "application/json")
    @PreAuthorize("isAdmin() or isOwner(#user)")
    public ResponseEntity<ResponseViewModel> create(@RequestBody User user) {
	long mark = new Date().getTime();
	logInfoMethodEntry(mark, LOGGER, "create", AuditAction.CREATE, SecurityContextHolder.getContext().getAuthentication(), user);
	
	ResponseViewModel ret = new ResponseViewModel();
	boolean isValid = false;
	
	if (user != null && !isNullOrEmpty(user.getEmail()) && !isNullOrEmpty(user.getPassword()) && !isNullOrEmpty(user.getName()) && user.getSite() != null) {
	    matcher = pattern.matcher(user.getEmail());
	    
	    if (matcher.find()) {
		isValid = true;
	    }
	}
	
	if (isValid) {
	    try {
		user.setEmail(user.getEmail().toLowerCase());
		User existUser = service.findByEmail(user.getEmail());
		if (existUser == null) {
		    user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
		    service.save(user);
		} else {
		    throw new Exception("Email already used.");
		}
	    }
	    catch (Exception ex) {
		ret.setMsg(ex.getMessage());
		logError(mark, LOGGER, ex);
		logInfoMethodExit(mark, LOGGER, "create", AuditAction.CREATE);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ret);
	    }
	    
	    logInfoMethodExit(mark, LOGGER, "create", AuditAction.CREATE);
	    return ResponseEntity.status(HttpStatus.CREATED).body(ret);
	} else {
	    ret.setMsg("Invalid data");
	    
	    logError(mark, LOGGER, new Exception("Invalid data"));
	    logInfoMethodExit(mark, LOGGER, "create", AuditAction.CREATE);
	    
	    return ResponseEntity.badRequest().body(ret);
	}
    }
    
    /**
     * Signup.
     *
     * @param user
     *            the user
     * @return the response entity
     */
    @RequestMapping(value = "/signup/", method = RequestMethod.POST, produces = "application/json")
    @PreAuthorize("isAdmin() or isOwner(#user)")
    public ResponseEntity<ResponseViewModel> signup(@RequestBody User user, HttpServletRequest req) {
	long mark = new Date().getTime();
	logInfoMethodEntry(mark, LOGGER, "signup", AuditAction.READ, SecurityContextHolder.getContext().getAuthentication(), user);
	
	ResponseViewModel ret = new ResponseViewModel();
	
	boolean isValid = false;
	
	if (user != null && !isNullOrEmpty(user.getEmail()) && !isNullOrEmpty(user.getPassword()) && !isNullOrEmpty(user.getName()) && user.getSite() != null) {
	    matcher = pattern.matcher(user.getEmail());
	    
	    if (matcher.find()) {
		isValid = true;
	    }
	}
	
	if (isValid) {
	    AccessLevel al = accessLevel.findByDescription(USER);
	    user.setAccessLevel(al);
	    user.setActive(false);
	    
	    try {
		user.setEmail(user.getEmail().toLowerCase());
		User existUser = service.findByEmail(user.getEmail());
		if (existUser == null) {
		    user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
		    
		    String activationCode = UUID.randomUUID().toString();
		    user.setActivationToken(activationCode);

		    sendActivactionMessage(user);
		    service.save(user);
		} else {
		    throw new Exception("Email already used.");
		}
	    }
	    catch (Exception ex) {
		ret.setMsg(ex.getMessage());
		
		logError(mark, LOGGER, ex);
		logInfoMethodExit(mark, LOGGER, "signup", AuditAction.READ);
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ret);
	    }
	    
	    logInfoMethodExit(mark, LOGGER, "signup", AuditAction.READ);
	    return ResponseEntity.status(HttpStatus.CREATED).body(ret);
	} else {
	    
	    ret.setMsg("Invalid data");
	    
	    logError(mark, LOGGER, new Exception("Invalid data"));
	    logInfoMethodExit(mark, LOGGER, "signup", AuditAction.READ);
	    
	    return ResponseEntity.badRequest().body(ret);
	}
    }
    
    /**
     * Update a User.
     *
     * @param requestUser
     *            the request user
     * @param id
     *            the id
     * @return the response entity
     */
    @RequestMapping(value = "/user/{id}", method = RequestMethod.POST, produces = "application/json")
    @PreAuthorize("isAdmin() or isOwner(#requestUser)")
    public ResponseEntity<ResponseViewModel> update(@RequestBody User requestUser, @PathVariable("id") long id) {
	long mark = new Date().getTime();
	logInfoMethodEntry(mark, LOGGER, "update", AuditAction.UPDATE, SecurityContextHolder.getContext().getAuthentication(), requestUser, id);
	
	ResponseViewModel ret = new ResponseViewModel();
	
	if (requestUser != null) {
	    try {
		User user = service.findById(id);
		if (user != null) {
		    
		    logParamInfo(mark, LOGGER, "User (old)", user);
		    
		    requestUser.setEmail(requestUser.getEmail().toLowerCase());
		    if (!requestUser.getEmail().equalsIgnoreCase(user.getEmail())) {
			User existUser = service.findByEmail(requestUser.getEmail());
			if (existUser != null) {
			    throw new Exception("This email is used by another user.");
			}
		    }
		    
		    if (requestUser.getPassword().trim().isEmpty()) {
			requestUser.setPassword(user.getPassword());
		    } else {
			requestUser.setPassword(BCrypt.hashpw(requestUser.getPassword(), BCrypt.gensalt()));
		    }
		    service.save(requestUser);
		} else {
		    throw new Exception("User does not exists.");
		}
	    }
	    catch (Exception ex) {
		
		ret.setMsg(ex.getMessage());
		
		logError(mark, LOGGER, ex);
		logInfoMethodExit(mark, LOGGER, "update", AuditAction.UPDATE);
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ret);
	    }
	    
	    logInfoMethodExit(mark, LOGGER, "update", AuditAction.UPDATE);
	    return ResponseEntity.status(HttpStatus.CREATED).body(ret);
	} else {
	    
	    ret.setMsg("User not exists");
	    
	    logError(mark, LOGGER, new Exception("User not exists"));
	    logInfoMethodExit(mark, LOGGER, "update", AuditAction.UPDATE);
	    
	    return ResponseEntity.badRequest().body(ret);
	}
    }
    
    /**
     * A normal user updating his profile.
     *
     * @param requestUser
     *            the request user
     * @param id
     *            the id
     * @return the response entity
     */
    @RequestMapping(value = "/setuser/{id}", method = RequestMethod.POST, produces = "application/json")
    @PreAuthorize("isAdmin() or isOwner(#requestUser)")
    public ResponseEntity<ResponseViewModel> updateProfile(@RequestBody User requestUser, @PathVariable("id") long id) {
	long mark = new Date().getTime();
	logInfoMethodEntry(mark, LOGGER, "updateProfile", AuditAction.UPDATE, SecurityContextHolder.getContext().getAuthentication(), requestUser, id);
	
	ResponseViewModel ret = new ResponseViewModel();
	boolean isValid = false;
	
	if (requestUser != null && !isNullOrEmpty(requestUser.getEmail()) && !isNullOrEmpty(requestUser.getName()) && requestUser.getSite() != null) {
	    matcher = pattern.matcher(requestUser.getEmail());
	    
	    if (matcher.find()) {
		isValid = true;
	    }
	}
	
	if (isValid) {
	    try {
		User user = service.findById(id);
		if (user != null) {
		    
		    logParamInfo(mark, LOGGER, "User (old)", user);
		    
		    requestUser.setEmail(requestUser.getEmail().toLowerCase());
		    if (!requestUser.getEmail().equalsIgnoreCase(user.getEmail())) {
			User existUser = service.findByEmail(requestUser.getEmail());
			if (existUser != null) {
			    throw new Exception("This email is used by another user.");
			}
		    }
		    
		    /*
		     * Não é possivel alterar os campos accessLevel e active
		     * atraves da tela de Profile.
		     */
		    requestUser.setAccessLevel(user.getAccessLevel());
		    requestUser.setActive(user.isActive());
		    
		    if (requestUser.getPassword().trim().isEmpty()) {
			requestUser.setPassword(user.getPassword());
		    } else {
			requestUser.setPassword(BCrypt.hashpw(requestUser.getPassword(), BCrypt.gensalt()));
		    }
		    service.save(requestUser);
		} else {
		    throw new Exception("User does not exists.");
		}
	    }
	    catch (Exception ex) {
		ret.setMsg(ex.getMessage());
		
		logError(mark, LOGGER, ex);
		logInfoMethodExit(mark, LOGGER, "updateProfile", AuditAction.UPDATE);
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ret);
	    }
	    
	    logInfoMethodExit(mark, LOGGER, "updateProfile", AuditAction.UPDATE);
	    return ResponseEntity.status(HttpStatus.CREATED).body(ret);
	} else {
	    ret.setMsg("User does not exist");
	    
	    logError(mark, LOGGER, new Exception("User does not exist"));
	    logInfoMethodExit(mark, LOGGER, "updateProfile", AuditAction.UPDATE);
	    
	    return ResponseEntity.badRequest().body(ret);
	}
    }
    
    /**
     * Checks if is null or empty.
     *
     * @param str
     *            the str
     * @return true, if is null or empty
     */
    private boolean isNullOrEmpty(String str) {
	return str == null || str.trim().isEmpty();
    }

    private void sendActivactionMessage(User user) throws MessagingException {
	StringBuilder sb = new StringBuilder();
	String subject = "Account Activation";
	String mimeType = "text/html";
	String baseUrl = System.getenv("ADS_HOST_URL");
	String mailpass = System.getenv("ADS_MAIL_PW");

	if (isNullOrEmpty(baseUrl)) {
	    logError(new Date().getTime(), LOGGER, new Exception("A variável de ambiente ADS_HOST_URL não foi configurada corretamente."));
	}
	if (isNullOrEmpty(mailpass)) {
	    logError(new Date().getTime(), LOGGER, new Exception("A variável de ambiente ADS_MAIL_PW não foi configurada corretamente."));
	}
	
	sb.append("<h2>Andes</h2>");
	sb.append("\n");
	sb.append("<p>Hi " + user.getName() + ",</p>");
	sb.append("\n");
	sb.append("<p>Welcome to Andes! Click on the link below to activate your account:</p>");
	sb.append("<a href=\"" + baseUrl + "/activate/?activationToken=" + user.getActivationToken() + "\">Activate</a>");
	sb.append("\n");
	
	Runnable r = new AndesMailer(new String[] { user.getEmail() }, subject, sb.toString(), mimeType);
	new Thread(r).start();
    }

}
