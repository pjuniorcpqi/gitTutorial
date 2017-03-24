app.controller("PhasesController", ['$scope', '$http','$uibModal','$filter', function($scope, $http, $uibModal,$filter) {
	initPhase();
	loadProjects();
	$scope.sortingOrder = "description";
    $scope.reverse = false;
    $scope.filteredItems = [];
    $scope.groupedItems = [];
    $scope.itemsPerPage = 8;
    $scope.pagedItems = [];
    $scope.currentPage = 0;
    loadPhases();
	
	var configHead = {header : {'Content-Type' : 'charset=UTF-8'}};
  
  function loadProjects() {
	  $http.get('phases/allProjects', configHead)
		.then(function(response) {
			$scope.projects  =  (JSON.parse(JSON.stringify(response.data)));
		});
  }
 

  // Initiate
  function initPhase() {
	  $scope.successPhaseMsg = false;
	  
  }

  // Load list of Phase
  function loadPhases() {
	$http.get('phases/all', configHead)
    .then(function(response) {
    	$scope.items = (JSON.parse(JSON.stringify(response.data)));
    	$scope.search();
    	
    	
    });
  }
  
  $scope.openModal = function (phaseSelected) {
	  $scope.phaseSelected = phaseSelected;
	  $scope.modalInstance  = $uibModal.open({
          templateUrl: 'views/phases/phaseSaveAndUpdate.html',
          controller: 'ModalController',
          scope: $scope,
          windowClass: 'large-Modal'
      });
	  
	  	$scope.modalInstance.result.then(function (isLoad) {
		  if(isLoad){
			  loadPhases();
	      }
	  });
  };
  
  $scope.openModalDelete = function (phaseDelete) {
	  $scope.phaseDelete = phaseDelete;
	  $scope.modalInstanceDelete  = $uibModal.open({
          templateUrl: 'views/phases/phaseDelete.html',
          controller: 'ModalController',
          scope: $scope,
          windowClass: 'large-Modal-delete'
      });
	  
	  	$scope.modalInstanceDelete.result.then(function (isLoad) {
		  if(isLoad){
			  loadPhases();
	      }
	  });
  };
  
// Calculate Total Number of Pages based on Records Queried
  $scope.groupToPages = function () {
      $scope.pagedItems = [];
      for (var i = 0; i < $scope.filteredItems.length; i++) {
          if (i % $scope.itemsPerPage === 0) {
              $scope.pagedItems[Math.floor(i / $scope.itemsPerPage)] = [$scope.filteredItems[i]];
          } else {
              $scope.pagedItems[Math.floor(i / $scope.itemsPerPage)].push($scope.filteredItems[i]);
          }
      }
  };
  
  var searchMatch = function (phase, needle) {
	  if (!needle) {
          return true;
      }
      return (phase.description.toLowerCase().indexOf(needle.toLowerCase()) > -1 || phase.project.title.toLowerCase().indexOf(needle.toLowerCase()) > -1 ) ? true : false;
  };
  
  //Initialize the Search Filters 
  $scope.search = function () {
	  $scope.filteredItems = $filter('filter')($scope.items, function (item) {
          if (searchMatch(item, $scope.query)){
        	  return true;
          }
          return false;
      });
      // Define Sorting Order
      if ($scope.sortingOrder !== '') {
          $scope.filteredItems = $filter('orderBy')($scope.filteredItems, $scope.sortingOrder, $scope.reverse);
      }
      $scope.currentPage = 0;
      
      // Group by pages
      $scope.groupToPages();
  };
  
  $scope.range = function (start, end) {
      var ret = [];
      if (!end) {
          end = start;
          start = 0;
      }
      for (var i = start; i < end; i++) {
          ret.push(i);
      }
      return ret;
  };
  
  $scope.prevPage = function () {
      if ($scope.currentPage > 0) {
          $scope.currentPage--;
      }
  };
  
  $scope.nextPage = function () {
      if ($scope.currentPage < $scope.pagedItems.length - 1) {
          $scope.currentPage++;
      }
  };
  $scope.setPage = function () {
      $scope.currentPage = this.n;
  };
  
  
}]);



app.controller('ModalController', ['$scope','$uibModal','$http','$timeout', function($scope, $uibModalInstance, $http,$timeout) {
	
		var configHead = {header : {'Content-Type' : 'charset=UTF-8'}};
	 	$scope.phase = {};
	    $scope.erroMsg = [];
	    $scope.successMsg = false;
	    
	    if($scope.phaseSelected != undefined){
	    	$scope.phase = $scope.phaseSelected;
	    }else if($scope.phaseDelete != undefined){
	    	$scope.phase = $scope.phaseDelete;
	    }
	
		$scope.processPhase = function() {
	    var phase = $scope.phase;
	    if (validatePhase(phase)) {
	    var url = 'phase/';

	      if (phase.id != undefined) {
	        url += phase.id;
	      }
	      
	      $http.post(url, phase, configHead)
	      .then(callBackSaveUpdate)
	      .catch(callBackErro);
	    }
	  };
	  
	  $scope.processDelete = function() {
		    var phase = $scope.phase;
		      var url = 'phase/delete/';
		      if (phase.id != undefined) {
		        url += phase.id;
		      }
		      $http.post(url, phase, configHead)
		      .then(callBackDelete)
		      .catch(callBackErro);
		    
	  };
	  
	  function validatePhase(phase) {
		    var erroMsg = [];
		    if (phase.project == undefined) {
		      erroMsg.push("Project is required.");
		    }

		    if (phase.startDate == undefined) {
		        erroMsg.push("Start Date is required.");
		     }
		    
		    if (phase.endDate == undefined) {
		    	erroMsg.push("End Date is required.");
		    }
		    
		    if (phase.description == undefined) {
		    	erroMsg.push("Description is required.");
		    }
		    
		    if(erroMsg.length == 0){
		    	if(moment(phase.endDate).isBefore(phase.startDate)){
		    		erroMsg.push("End Date must be after Start Date!");
		    	}
		    }
		    

		    $scope.erroMsg = erroMsg;
		    return !(erroMsg.length > 0);
		  }
	  
	  function callBackSaveUpdate(response) {
		  $scope.successMsg = true;
		  $timeout(callback, 2000);
		 
	  }
	  
	  function callBackDelete(response) {
		  $scope.successMsg = true;
		  $timeout(callbackDelete, 1000);
		 
	  }
	  
	  function callback () {
		  $scope.modalInstance.close(true);
	  }
	  
	  function callbackDelete () {
		  $scope.modalInstanceDelete.close(true);
	  }

		
	  
	  $scope.cancelDelete = function() {
		  $scope.modalInstanceDelete.dismiss("cancel");
		  
	  };

		
	  
	  $scope.cancel = function() {
		  $scope.modalInstance.dismiss("cancel");
		  
	  };
	  
	  function callBackErro(response) {
		  if (response.data != undefined ) {
		      $scope.erroMsg.push(response.data.msg);
		  } else {
		      $scope.erroMsg.push(response.statusText);
		  }
	 }
    
}]);




