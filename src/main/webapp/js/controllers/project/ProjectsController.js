app.controller("ProjectsController", ['$scope', '$http', function($scope, $http){
	$scope.viewBy = 10;
	$scope.viewByOptions = [3,5,10,20,30];
	$scope.currentPage = 1;
	$scope.itemsPerPage = $scope.viewBy;
	$scope.maxSize = 5; //Number of pager buttons to show

	var configHead = {header : {'Content-Type' : 'charset=UTF-8'}};

	initScope();
	initProjects();
	
	//Initiate Projects
	function initProjects () {
		$scope.projects = [];
		getObjects('projects/', 'projects');
	}
	
	
	//Initiate
	function initScope () {
		$scope.formProject= {};
		$scope.emptyProject = angular.copy($scope.formProject);
		$scope.erroMsg = [];
		$scope.clients = [];
		$scope.projectTypes = [];
		$scope.incomeTypes = [];
		$scope.currencies = [];
		$scope.successMsg = false;
	}

	function getObjects(url, partialtype){
		$http.get(url, configHead)
		.then(function(response) {
			$scope[partialtype] = response.data;
			$scope.totalItems = $scope.projects.length;
		});
	}

	$scope.getProject = function (project) {
		getObjects('clients/', 'clients');
		getObjects('projectTypes/', 'projectTypes');
		getObjects('incomeTypes/', 'incomeTypes');
		getObjects('currencies/', 'currencies');
		$scope.formProject = (JSON.parse(JSON.stringify(project)));
		$scope.formProject.startDate = $scope.formProject.startDate/1000; //Formatting unix-timestamp to seconds.
		$scope.formProject.endDate = $scope.formProject.endDate/1000;

		$scope.formProject.startDate = moment.unix($scope.formProject.startDate).format("DD/MM/YYYY");
		$scope.formProject.endDate = moment.unix($scope.formProject.endDate).format("DD/MM/YYYY");
	};

	$scope.deleteProject = function() {
		var url = 'project/delete';
		var project = angular.copy($scope.formProject);
		project.endDate =  moment($scope.formProject.endDate, 'DD/MM/YYYY', true).format();
		project.startDate = moment($scope.formProject.startDate, 'DD/MM/YYYY', true).format();
		$http.post(url, project, configHead).then(callBackSave).catch(callBackErro);
	}

	$scope.processProject = function(){
		
		var project = angular.copy($scope.formProject);
		project.endDate =  moment(project.endDate, 'DD/MM/YYYY', true).format();
		project.startDate = moment(project.startDate, 'DD/MM/YYYY', true).format();
		if(validateProject(project)){
			var url = 'project/';
			if (project.id != undefined) {
				url += project.id;
			}
			$http.post(url, project, configHead).then(callBackSave).catch(callBackErro);
		};
	}

	function callBackSave(response) {
		$("#btnCloseModal").click();
		initScope();
		initProjects();
		$scope.successMsg = true;
	}
	
	// Reset errorMsg Array when user close update modal.
	$('#modalUpdateProject').on('hidden.bs.modal', function () {
		$scope.erroMsg = [];
	});


	function callBackErro(response) {
		if (response.data != undefined ) {
			$scope.erroMsg.push(response.data.msg);
		} else {
			$scope.erroMsg.push(response.statusText);
		}
	}

	function validateProject(project){
		var erroMsg = [];
		if (project.title == undefined || project.title.trim() == "") {
			erroMsg.push("Project title is required.");
		}
		
		if (project.currency == undefined || project.currency.code.trim() == "") {
			erroMsg.push("Project currency is required.");
		}

		if(moment(project.endDate).isBefore(project.startDate)){
			erroMsg.push("End Date must be after Start Date!");
		}

		$scope.erroMsg = erroMsg;

		return !(erroMsg.length > 0);
	}

	$scope.startNewProject = function (){
		getObjects('clients/', 'clients');
		getObjects('projectTypes/', 'projectTypes');
		getObjects('incomeTypes/', 'incomeTypes');
		getObjects('currencies/', 'currencies');
		resetProject();
	}

	function resetProject() {
		$scope.formProject = angular.copy($scope.emptyProject);
	}

	$scope.pageChanged = function() {
		console.log('Page changed to: ' + $scope.currentPage);
	}

	$scope.setPage = function (pageNo) {
		$scope.currentPage = pageNo;
	};

	$scope.setItemsPerPage = function(num) {
		$scope.itemsPerPage = num;
		$scope.currentPage = 1; //reset to first page
	};

}]);
