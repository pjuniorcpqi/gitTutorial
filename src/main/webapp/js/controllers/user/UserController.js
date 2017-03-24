app.controller("UserController", ['$scope', '$http', function($scope, $http) {

  initScope();
  loadUsers();
  
  $http.get("accessLevels/").then(function (response) {
	  $scope.accessLevels = response.data;
  });
  
  $http.get("sites/").then(function (response) {
	  $scope.sites = response.data;
  });

  $scope.predicate = 'name';
  $scope.reverse = false;
  $scope.search = {};
  $scope.search.active = true;
  
  $scope.order = function(predicate) {
    $scope.reverse = ($scope.predicate === predicate) ? !$scope.reverse : false;
    $scope.predicate = predicate;
  };

  var configHead = {header : {'Content-Type' : 'charset=UTF-8'}};

  // Cria e atualiza User
  $scope.processUser = function() {
    var user = angular.copy($scope.formUser);
    user.admissionDate = moment(user.admissionDate, 'DD/MM/YYYY', true).format();
    if (validateUser(user)) {
      var url = '/user/';

      if (user.id != undefined) {
        url += user.id;
      }
      
      $http.post(url, user, configHead)
      .then(callBackSaveUpdate)
      .catch(callBackErro);
    }
  };

  // Chama o Modal do Form
  $scope.createUser = initScope;

  // Chama o Modal do Form para atualizar
  $scope.updateUser = function (user) {
    $scope.formUser = (JSON.parse(JSON.stringify(user)));
    $scope.formUser.admissionDate = moment($scope.formUser.admissionDate).format("DD/MM/YYYY");
  }

  // Chama o Modal do Form para alocar e desalocar o user
  $scope.allocateUser = function (user) {
	$scope.erroMsg = [];
	$scope.formUserAllocations = {};
	$scope.toDeleteAllocations = [];
	$scope.newAllocations = [];
	$scope.user = user;
	
	$http.post('allocations/', user.id, configHead)
	.then(function(response) {
		$scope.allocations = response.data;		
	})
    .catch(callBackErro);
	
	$http.get('allocations/noUser', configHead)
	.then(function(response) {
		$scope.noUserAllocations = response.data;
	})
    .catch(callBackErro);
  }
  
  $scope.toggleUserAllocation = function(allocation, list)
  {
	  var idx = list.indexOf(allocation);

	    // is currently selected
	    if (idx > -1) {
	    	list.splice(idx, 1);
	    }

	    // is newly selected
	    else {
	    	list.push(allocation);
	    }
  }

  // Aloca e desaloca o User
  $scope.processUserAllocation = function() {
	var url = 'allocations/allocate/';
	if ($scope.user != undefined) {
		url += $scope.user.id;
	}
	
	if($scope.newAllocations.length > 0)
	{
		$http.post(url, $scope.newAllocations, configHead)
		    .then(callBackSaveAllocation)
		    .catch(callBackErro);
	}

	if($scope.toDeleteAllocations.length > 0)
	{
		$http.post('allocations/deallocate/', $scope.toDeleteAllocations, configHead)
		    .then(callBackSaveAllocation)
		    .catch(callBackErro);
	}
  };

  function getObjects(url, partialtype){
	$http.get(url, configHead)
	.then(function(response) {
		$scope[partialtype] = response.data;
	});
  }

  // Initiate
  function initScope () {
    $scope.formUser = {"active": true};
    $scope.erroMsg = [];
    $scope.successMsg = false;
  }

  // Load list of User
  function loadUsers() {
    $http.get('/users/', configHead)
    .then(function(response) {
      $scope.users = response.data;
    });
  }

  function callBackSaveUpdate(response) {
    $("#btnCloseModal").click();
    initScope();
    loadUsers();
    $scope.successMsg = true;
  }

  function callBackSaveAllocation(response) {
    $("#btnCloseAllocateModal").click();
    initScope();
    loadUsers();
  }

  function callBackErro(response) {
    if (response.data != undefined ) {
    	$scope.erroMsg.push(response.data.msg);
    } else {
    	$scope.erroMsg.push(response.statusText);
    }
  }

  function callBackDeallocationError(response) {
    if (response.data != undefined ) {
      $scope.erroMsg.push(response.data.msg);
    } else {
    	if(response.statusText != undefined)
    	{
    		$scope.erroMsg.push(response.statusText);
    	}
    	else
    	{
    		$scope.erroMsg.push('Allocation may have linked worklogs');
    	}
    }
  }

  function validateUser(user) {
    var erroMsg = [];
    if (user.name == undefined || user.name.trim() == "") {
      erroMsg.push("Name is required.");
    }

    // Novo usuario com senha vazia
    if ((!user.password || !user.password.trim()) && !user.id) {
        erroMsg.push("Password is required.");
    }
    
    if (user.email == undefined || !validateEmail(user.email)) {
		toastr["error"]("This e-mail is invalid.");
		valid = false;
	}
    
    if (!user.accessLevel) {
        erroMsg.push("Access level is required.");
    }
    
    if (!user.site) {
        erroMsg.push("Site is required.");
    }
    
    
    $scope.erroMsg = erroMsg;

    return !(erroMsg.length > 0);
  }
  
  function validateEmail(email) {
	    var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	    return re.test(email);
	}
  
}]);
