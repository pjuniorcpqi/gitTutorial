app.controller("AllocationsController", ['$scope', '$http','$uibModal','$filter', function($scope, $http, $uibModal, $filter) {
	initPhase();
	loadAll();
	$scope.sortingOrder = "description";
    $scope.reverse = false;
    $scope.filteredItems = [];
    $scope.filteredItemsPhase = [];
    $scope.filteredItemsProject = [];
    $scope.groupedItems = [];
    $scope.pagedItems = [];
    $scope.pagedItemsPhase = [];
    $scope.pagedItemsproject = [];
    $scope.currentPage = 0;
    $scope.numberOfItemsPerPage = [5,10,15,20,100];
    loadAllocations();
	var configHead = {header : {'Content-Type' : 'charset=UTF-8'}};
  
  function loadAll() {
	  $http.get('allocations/populate', configHead)
		.then(function(response) {
			$scope.allocationsReq  =  (JSON.parse(JSON.stringify(response.data)));
			$scope.itemsPhase = $scope.allocationsReq.phases;
			$scope.itemsProject = $scope.allocationsReq.projects;
			$scope.changeClient();
			$scope.changeClientProject();
		});
  }
 

  // Initiate
  function initPhase() {
	  $scope.successPhaseMsg = false;
  }
  
  /*
   * Configurações da table
   */
  $scope.config = {
    itemsPerPage: 10
  }
  


  // Load list of Phase
  function loadAllocations() {
	$http.get('allocations/all', configHead)
    .then(function(response) {
    	$scope.items = (JSON.parse(JSON.stringify(response.data)));
    	$scope.search();
    });
  }
  
  $scope.openModal = function (allocationSelected) {
	  $scope.allocationSelected = allocationSelected;
	  $scope.modalInstance  = $uibModal.open({
          templateUrl: 'views/allocations/allocationsSaveAndUpdate.html',
          controller: 'ModalAllocationsController',
          scope: $scope,
          windowClass: 'large-Modal'
      });
	  
	  	$scope.modalInstance.result.then(function (isLoad) {
		  if(isLoad){
			  loadAllocations();
	      }
	  });
  };
  
  $scope.openModalDelete = function (allocationSelected) {
	  $scope.allocationSelected = allocationSelected;
	  $scope.modalInstanceDelete  = $uibModal.open({
          templateUrl: 'views/allocations/allocationsDelete.html',
          controller: 'ModalAllocationsController',
          scope: $scope,
          windowClass: 'large-Modal-delete'
      });
	  
	  	$scope.modalInstanceDelete.result.then(function (isLoad) {
		  if(isLoad){
			  loadAllocations();
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
  
  var searchMatch = function (allocation, needle) {
	  if (!needle) {
          return true;
      }
	  return (allocation.phase != null && allocation.phase.description.toLowerCase().indexOf(needle.toLowerCase()) > -1) ? true : false;
  };
  
  
  var searchMatchClient = function (allocation, needle) {
	  if (!needle) {
          return true;
      }
	  return (allocation.phase != null && allocation.phase.project.client.name.toLowerCase().indexOf(needle.toLowerCase()) > -1) ? true : false;
  };
  
  var searchMatchProject = function (allocation, needle) {
	  if (!needle) {
          return true;
      }
	  return (allocation.phase != null && allocation.phase.project.title.toLowerCase().indexOf(needle.toLowerCase()) > -1)  ? true : false;
  };
  
  
  var searchMatchUser = function (allocation, needle) {
	  if (!needle) {
          return true;
      }
	  return (allocation.user != null && allocation.user.name.toLowerCase().indexOf(needle.toLowerCase()) > -1) ? true : false;
  };
  
  
  var searchMatchCheckUser = function (allocation , noFilter) {
	  if(!noFilter){
		  return true;
	  }
	  return (allocation.user == null) ? true : false;
  };
  
  // Initialize the Search Filters
  $scope.searchClient = function () {
	  $scope.filteredItems = $filter('filter')($scope.items, function (item) {
          if (searchMatchClient(item, $scope.queryClient)){
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
  
// Initialize the Search Filters
  $scope.searchProject = function () {
	  $scope.filteredItems = $filter('filter')($scope.items, function (item) {
          if (searchMatchClient(item, $scope.queryClient) && searchMatchProject(item, $scope.queryProject)){
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
  
  // Initialize the Search Filters
  $scope.search = function () {
	  $scope.filteredItems = $filter('filter')($scope.items, function (item) {
          if (searchMatchClient(item, $scope.queryClient) && searchMatchProject(item, $scope.queryProject) && searchMatch(item, $scope.query)){
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
  
  $scope.searchUserCheck = function () {
	    $scope.filteredItems = $filter('filter')($scope.items, function (item) {
	    	if (searchMatchCheckUser(item,$scope.queryUserCheck)){
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
	}
  
  
 
  
  // Initialize the Search Filters
  $scope.searchUser = function () {
	  $scope.filteredItems = $filter('filter')($scope.items, function (item) {
          if (searchMatchClient(item, $scope.queryClient) && searchMatchProject(item, $scope.queryProject) && searchMatch(item, $scope.query) && searchMatchUser(item, $scope.queryUser)){
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
  
  
  $scope.onchangeClient = function (client) {
	  changeClient(client);
  };
  
  
  // Initialize the Search Filters
  $scope.changeClient = function (client) {
	  $scope.changeClientProject(client);
	  $scope.filteredItemsPhase = $filter('filter')($scope.itemsPhase, function (item) {
          if (filterMatchClient(item, client)){
        	  return true;
          }
          return false;
      });
	  
	  $scope.pagedItemsPhase = [];
      for (var i = 0; i < $scope.filteredItemsPhase.length; i++) {
          	$scope.pagedItemsPhase[i] = $scope.filteredItemsPhase[i];
      }
      
    };
    
    
    // Initialize the Search Filters
    $scope.changeProject = function (project, client) {
      $scope.filteredItemsPhase = $filter('filter')($scope.itemsPhase, function (item) {
            if (filterMatchClient(item, client) && filterMatchClientProject(item,project)){
          	  return true;
            }
            return false;
        });
  	  
  	  $scope.pagedItemsPhase = [];
        for (var i = 0; i < $scope.filteredItemsPhase.length; i++) {
            	$scope.pagedItemsPhase[i] = $scope.filteredItemsPhase[i];
        }
        
      };
    
    
    $scope.changeClientProject = function (client) {
  	  $scope.filteredItemsProject = $filter('filter')($scope.itemsProject, function (item) {
            if (filterMatchProject(item, client)){
          	  return true;
            }
            return false;
        });
  	  
  	  $scope.pagedItemsproject = [];
        for (var i = 0; i < $scope.filteredItemsProject.length; i++) {
            	$scope.pagedItemsproject[i] = $scope.filteredItemsProject[i];
        }
        
      };
  
  
	 var filterMatchClient = function (phase, client) {
		  if (!client) {
	          return true;
	      }
		  return (phase != null && phase.project.client.name.toLowerCase().indexOf(client.name.toLowerCase()) > -1) ? true : false;
	  };
	  
	  var filterMatchClientProject = function (phase, project) {
		  if (!project) {
	          return true;
	      }
		  return (phase != null && phase.project.title.toLowerCase().indexOf(project.title.toLowerCase()) > -1) ? true : false;
	  };
	  
	  var filterMatchProject = function (project, client) {
		  if (!client) {
	          return true;
	      }
		  return (project != null && project.client.name.toLowerCase().indexOf(client.name.toLowerCase()) > -1) ? true : false;
	  };
}]);




app.controller('ModalAllocationsController', ['$scope','$http','$timeout','$filter', function($scope, $http,$timeout,$filter) {
	
		var configHead = {header : {'Content-Type' : 'charset=UTF-8'}};
	 	$scope.allocation = {};
	    $scope.erroMsg = [];
	    $scope.successMsg = false;
	    
	    if($scope.allocationSelected != undefined){
	    	$scope.allocation = $scope.allocationSelected;
	    }
	    
	    $scope.changeClient();
		$scope.changeClientProject();
		
		$scope.checkIsExistingAllocation = function(){
			return $scope.allocation.id != undefined;
		};
		
		$scope.saveAsNew = function(){
			$scope.allocation.id = null;
			$scope.processAllocations();
		};
	
		$scope.processAllocations = function() {
			var allocation = $scope.allocation;
		    if (validateAllocation(allocation)) {
		    	var url = 'allocations/save/';
		    	if (allocation.id != undefined) {
		    		url += allocation.id;
		    	}
		    	
		    	$http.post(url, allocation, configHead)
		    		.then(callBackSaveUpdate)
		    		.catch(callBackErro);
		    }
	  };
	  
	  $scope.processDelete = function() {
		    var allocation = $scope.allocation;
		      var url = 'allocations/remove/';
		      if (allocation.id != undefined) {
		        url += allocation.id;
		      }
		      $http.post(url, allocation, configHead)
		      .then(callBackDelete)
		      .catch(callBackErro);
		    
	  };
	  
	  function validateAllocation(allocation) {
		    var erroMsg = [];
		    if (allocation.phase == undefined) {
		      erroMsg.push("Phase is required.");
		    }
		    if (allocation.profile == undefined) {
		        erroMsg.push("Profile is required.");
		     }
		    
		    if (allocation.startDate == undefined) {
		    	 erroMsg.push("Start Date is required.");
		    }
		    
		    if(erroMsg.length == 0){
		    	if(allocation.endDate != undefined){
		    		if(moment(allocation.endDate).isBefore(allocation.startDate)){
		    			erroMsg.push("End Date must be after Start Date!");
		    		}
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




