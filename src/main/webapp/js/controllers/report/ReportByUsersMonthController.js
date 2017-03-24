app.controller("ReportByUsersMonthController", ['$scope', '$http', function($scope, $http){
	var configHead = {header : {'Content-Type' : 'charset=UTF-8'}};
	$scope.reportUserByDate = {};
	$scope.reportResult = [];
	$scope.reportFilterView = {};
	$scope.reportFilterView.users = [];
	$scope.reportFilterView.projects = [];
	$scope.reportFilterView.sites = [];

	$scope.reportFilterSend = {};
	$scope.reportFilterSend.users = [];
	$scope.reportFilterSend.projects = [];
	$scope.reportFilterSend.sites = [];


	$scope.erroMsgReportRevenue = [];
	$scope.Math = window.Math;
	$scope.teste = [];
	init();

	function init() {
		$http.get('report/loadFilter/', configHead)
		.then(function(response) {
			$scope.reportUserByDate  =  (JSON.parse(JSON.stringify(response.data)));
		});
	}

	$scope.print = function(){
		if (validateFilter()) {
			var dateSelected = moment($scope.reportFilter.monthYearSelected);
			var formattedDate = dateSelected.get('year') + "-" + dateSelected.format('M'); 
			
			hashMapReport = {
					"idUsers" : $scope.reportFilterSend.users,
					"idSites" : $scope.reportFilterSend.sites,
					"idProjects" : $scope.reportFilterSend.projects,
					"monthYearSelected" : formattedDate				
			};
					var url = 'report/monthReportsTimeSheet/';
					
					$http.post(url, hashMapReport, {responseType: 'arraybuffer'}).then(function(response){
						var file = new Blob([response.data], {type: 'application/pdf'});
						var fileURL = URL.createObjectURL(file);
						var temp = window.open(fileURL, '_blank');
						if (!temp) {
							toastr["info"]('You have a popup blocker enabled. Please allow popups for this domain.');
						};
					
					}).catch(function (response) {
						toastr["error"]("Something went wrong. Please, contact an administrator.");
					});
		}
	};
	
	$scope.printCreditAgricoleReport = function(){
		if (validateFilter()) {
			var dateSelected = moment($scope.reportFilter.monthYearSelected);
			var formattedDate = dateSelected.get('year') + "-" + dateSelected.format('M'); 
			
			hashMapReport = {
					"idUsers" : $scope.reportFilterSend.users,
					"idSites" : $scope.reportFilterSend.sites,
					"idProjects" : $scope.reportFilterSend.projects,
					"monthYearSelected" : formattedDate				
			};
					var url = 'creditAgricoleReport/';
					
					$http.post(url, hashMapReport, {responseType: 'arraybuffer'}).then(function(response){
						var file = new Blob([response.data], {type: 'application/pdf'});
						var fileURL = URL.createObjectURL(file);
						var temp = window.open(fileURL, '_blank');
						if (!temp) {
							toastr["info"]('You have a popup blocker enabled. Please allow popups for this domain.');
						};
					
					}).catch(function (response) {
						toastr["error"]("Something went wrong. Please, contact an administrator.");
					});
		}
	};

	$scope.removeUser = function (user) {
		for(var x in $scope.reportFilterView.users){
			if($scope.reportFilterView.users[x].id === user.id){
				$scope.reportFilterView.users.splice(x, 1);
				$scope.reportFilterSend.users.splice(x, 1);
				return;
			}
		}
	}

	$scope.removeProject = function (project) {
		for(var x in $scope.reportFilterView.projects){
			if($scope.reportFilterView.projects[x].id === project.id){
				$scope.reportFilterView.projects.splice(x, 1);
				$scope.reportFilterSend.projects.splice(x, 1);
				return;
			}
		}
	}

	$scope.removeSite = function (site) {
		for(var x in $scope.reportFilterView.sites){
			if($scope.reportFilterView.sites[x].id === site.id){
				$scope.reportFilterView.sites.splice(x, 1);
				$scope.reportFilterSend.sites.splice(x, 1);
				return;
			}
		}
	}

	$scope.onUserSelect = function ($item, $model) {
		if($model.id != undefined){
			if($scope.reportFilterView.users.length == 0){
				$scope.reportFilterView.users.push($model);
				$scope.reportFilterSend.users.push($model.id);
				$scope.reportFilter.userId = null;
			}else{
				for(var x in $scope.reportFilterView.users){
					var alreadyInserted = $scope.reportFilterView.users[x].id == $model.id;
				}
				if(alreadyInserted){
					toastr["error"]("User already selected");
					return;
				}else{

					$scope.reportFilterView.users.push($model);
					$scope.reportFilterSend.users.push($model.id);
					$scope.reportFilter.userId = null;
				}
			}
		}
	};
	$scope.onProjectSelect = function ($item, $model) {
		if($model.id != undefined){
			if($scope.reportFilterView.projects.length == 0){
				$scope.reportFilterView.projects.push($model);
				$scope.reportFilterSend.projects.push($model.id);
				$scope.reportFilter.projectId = null;
			}else{
				for(var x in $scope.reportFilterView.projects){
					var alreadyInserted = $scope.reportFilterView.projects[x].id == $model.id;
				}
				if(alreadyInserted){
					toastr["error"]("Project already selected");
					return;
				}else{

					$scope.reportFilterView.projects.push($model);
					$scope.reportFilterSend.projects.push($model.id);
					$scope.reportFilter.projectId = null;
				}
			}
		}
	};
	$scope.onSiteSelect = function ($item, $model) {
		if($model.id != undefined){
			if($scope.reportFilterView.sites.length == 0){
				$scope.reportFilterView.sites.push($model);
				$scope.reportFilterSend.sites.push($model.id);
				$scope.reportFilter.siteId = null;
			}else{
				for(var x in $scope.reportFilterView.sites){
					var alreadyInserted = $scope.reportFilterView.sites[x].id == $model.id;
				}
				if(alreadyInserted){
					toastr["error"]("Site already selected");
					return;
				}else{

					$scope.reportFilterView.sites.push($model);
					$scope.reportFilterSend.sites.push($model.id);
					$scope.reportFilter.siteId = null;
				}
			}
		}
	};

	$scope.resetbtn = function(){
		$scope.reportFilter = null;
		$scope.erroMsgReportRevenue = [];
		$scope.reportResult = [];

		if ($scope.reportFilterView != undefined) {
			$scope.reportFilterView.users = [];
			$scope.reportFilterView.projects = [];
			$scope.reportFilterView.sites = [];
		}
		if ($scope.reportFilterSend != undefined) {
			$scope.reportFilterSend.users = [];
			$scope.reportFilterSend.projects = [];
			$scope.reportFilterSend.sites = [];
		}
	};

	$scope.appendToFilter = function(user){

	}

	function validateFilter() {
		var erro = true;

		if ($scope.reportFilter != undefined) {
			if ($scope.reportFilter.monthYearSelected == undefined) {
				toastr["error"]("Please select a month.");
				erro = false;
			}
		} else {
			toastr["error"]("Filter invalid.");
			erro = false;
		}

		return erro;
	}
}]);
