app.controller("LoginController", ['vcRecaptchaService',  '$scope', '$location', '$localStorage','$http', 
        function(vcRecaptchaService, $scope, $location, $localStorage, $http) {

// captcha
	$scope.publickey = "6LfbLSATAAAAAC3Ls7Yjy6imZEA0loTHNyNkWzW4";
	$scope.response = null;
	$scope.widgetId = null;
	
	
	$scope.setResponse = function(response){
		$scope.response = response;
	};

	$scope.setWidgetId = function(widgetId){
		$scope.widgetId = widgetId;
	};
	
    $scope.cbExpiration = function() {
        $scope.response = null;
        vcRecaptchaService.reload($scope.widgetId);
    };
	
	$http.get("sites/").then(function(response) {
		$scope.sites = response.data;
	});

	var configHead = {
		header : {
			'Content-Type' : 'charset=UTF-8'
		}
	};

	var ok = $localStorage.logado;
	$scope.isLoading = ok;

	function  authenticate(username, password) {
		var data = 'username=' + username + '&password=' + password;
		var headers = new Headers();
		headers.append('Content-Type', 'application/x-www-form-urlencoded');
			
		var res = $http({
			method : 'POST',
			url : 'login',
			data : data ,
			headers : {
				'Content-Type' : 'application/x-www-form-urlencoded',
			}
		});
		res.success(function(dataObj, status, headers, config) {
			var data = (JSON.parse(JSON.stringify(dataObj)));
			if (data.msg != undefined) {
				toastr["error"](data.msg);
				$location.path('/login')
			} else {
				$localStorage.principal = data;
				$localStorage.logado = data.logged;
				$scope.isLoading = $localStorage.logado;
				$location.path('/home')
			}
	
		});
	};
	
	
	  $scope.submitLogin = function () {
          
  		if($scope.response === "" || $scope.response === null){
  		
  			toastr["error"]("Please resolve the captcha.");
  		
  		}else{
  			var valid = false;
  			
  			var data = "response=" + $scope.response; 
  			var res = $http({
  				method : 'POST',
  				url : '/recaptcha',
  				data : data,
  				headers : {
  					'Content-Type' : 'application/x-www-form-urlencoded'
  				}
  			}).then(function succes(response) {
  		        valid = response.data;
  		        if (valid) {
  		        	authenticate($scope.username, $scope.password);
  		        	vcRecaptchaService.reload($scope.widgetId);
  		        } else {
  		        	toastr["error"]("Cannot authenticate The captcha is not valid. Try again.");
  		        	vcRecaptchaService.reload($scope.widgetId);
  		        }
  		    }, function error(response) {
  		    	toastr["error"]("Cannot authenticate. Contact the system admin.");
  		    });
  		}
      };
      
      $scope.submitSignup = function () {
    		        	processUser();
      };
	
	$scope.isLoading = function() {
		var logado = $localStorage.logado;
		if (!logado) {
			$location.path('/login')
		}
		return logado;
	};

	$scope.resetForm = function(username) {
		$localStorage.logado = false;
		var headers = new Headers();
		headers.append('Content-Type', 'application/x-www-form-urlencoded');

		var res = $http({
			method : 'POST',
			url : 'logout',
			headers : {
				'Content-Type' : 'application/x-www-form-urlencoded',
				'username' : username
			}
		});
		res.success(function(dataObj, status, headers, config) {
			var data = (JSON.parse(JSON.stringify(dataObj)));
			toastr["success"](data.msg);
			$location.path('/login')
		});
	};

	$scope.getUser = function() {
		return $localStorage.principal;
	}

	$scope.isAdmin = function() {
		return $localStorage.principal.accessLevel == "ROLE_ADMIN";
	};
	
	$scope.isAuthReportByDate = function(){
		var authRoles = ["ROLE_ADMIN", "ROLE_HR", "ROLE_MANAGER"];
		
		return jQuery.inArray($localStorage.principal.accessLevel, authRoles) !== -1;
	};
	
	$scope.isAuthProjectsAllocationsPhases = function(){
		var authRoles = ["ROLE_ADMIN", "ROLE_MANAGER"];
		return jQuery.inArray($localStorage.principal.accessLevel, authRoles) !== -1;
	}
	
	$scope.isAuthReportRevenue = function(){
		var authRoles = ["ROLE_ADMIN", "ROLE_ACCOUNTING", "ROLE_MANAGER"];
		return jQuery.inArray($localStorage.principal.accessLevel, authRoles) !== -1;
	};

	function processUser() {
		var user = $scope.formUser;
			
			if (validateUser(user)) {
				var res = $http({
					method : 'POST',
					url : '/signup/',
					data: user
				});
				
				res.success(function(dataObj, status, headers, config) {
					callBackSaveUpdate(res);
				});
				
				res.error(function(dataObj, status, headers, config) {
					toastr["error"]("The User cannot be created. Please contact the system Admin.");
				});
			}
	};

	function validateUser(user) {
		var valid = true;
		if (user.name == undefined || user.name.trim() == "") {
			toastr["error"]("Name is required.");
			valid = false;
		}

		if (user.password == undefined || user.password.trim() == "") {
			toastr["error"]("Password is required.");
			valid = false;
		}

		if (user.email == undefined || !validateEmail(user.email)) {
			toastr["error"]("This e-mail is invalid. Please insert a CPQi corporate email.");
			valid = false;
		}

		if (!user.site) {
			toastr["error"]("Site is required.");
			valid = false;
		}

		return valid;
	}

	function callBackSaveUpdate(response) {
		toastr["success"]("User created successfully. Please check your email to activate your account.");
		$("#btnCloseModal").click();
	}
	
	function validateEmail(email) {
	    var re = /^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@cpqi.com$/;
	    return re.test(email);
	};

}]);