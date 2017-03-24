app.controller("ProfileController", function($scope, $location, $localStorage, $http) {
	
	initScopes();
	
	function initScopes() {
		$http.get('/getuser/', {
			params: {
				idUser: $localStorage.principal.userId,
			}
		}).then(function(response) {
	      $scope.formUser = (JSON.parse(JSON.stringify(response.data)));
	      $scope.formUser.admissionDate = moment($scope.formUser.admissionDate).format("DD/MM/YYYY");
		}, function(response){
			toastr["error"]("Something went wrong. Please, contact an administrator.");
		});
	}
	
	$http.get("sites/").then(function(response) {
		$scope.sites = response.data;
	});

	var configHead = {
		header : {
			'Content-Type' : 'charset=UTF-8'
		}
	};

	$scope.processUser = function() {
		var user = angular.copy($scope.formUser);
	    user.admissionDate = moment(user.admissionDate, 'DD/MM/YYYY', true).format();
		if (validateUser(user)) {
			var url = '/setuser/' + user.id;

			$http.post(url, user, configHead)
			.then(function(response) {
				toastr["success"]("User updated successfully.");
			}, function(response) {
				toastr["error"]("Invalid data.");
			});
		}
	}
	
	function validateUser(user) {
		var valid = true;
		if (user.name == undefined || user.name.trim() == "") {
			toastr["error"]("Name is required.");
			valid = false;
		}

		if (user.email == undefined || !validateEmail(user.email)) {
			toastr["error"]("This e-mail is invalid.");
			valid = false;
		}

		if (!user.site) {
			toastr["error"]("Site is required.");
			valid = false;
		}

		return valid;
	}
	
	function validateEmail(email) {
	    var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	    return re.test(email);
	}

});