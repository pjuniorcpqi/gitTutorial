app.controller("ReportUserByDateController", ['$scope', '$http', function($scope, $http){

	var configHead = {header : {'Content-Type' : 'charset=UTF-8'}};
	$scope.reportUserByDate = {};
	$scope.reportResult = [];
	$scope.erroMsgReport = [];
	init ();
	
	$scope.predicate = 'userName';
	$scope.reverse = false;
	  
	$scope.order = function(predicate) {
	  $scope.reverse = ($scope.predicate === predicate) ? !$scope.reverse : false;
	  $scope.predicate = predicate;
	};

	function init() {
		$http.get('report/loadFilter/', configHead)
		.then(function(response) {
			$scope.reportUserByDate  =  (JSON.parse(JSON.stringify(response.data)));
		});
	}

	$scope.pesquisar = function(){
		$scope.reportResult = [];
		if (validateFilter($scope.reportFilter)) {
			var reportFilter = (JSON.parse(JSON.stringify($scope.reportFilter)));

			reportFilter.endDate = reportFilter.endDate != undefined ?
																	moment(reportFilter.endDate, 'DD/MM/YYYY', true).format()
																	: null;
			reportFilter.startDate = moment(reportFilter.startDate, 'DD/MM/YYYY', true).format();
				var url = 'report/search/';
				$http.post(url, reportFilter, configHead).then(function(response){
					$scope.reportResult = response.data;
				}).catch(function (response) {
					$scope.erroMsgReport.push("Erro to call server.");
				});
		}
	};

	$scope.limpar = function(){
		$scope.reportFilter = null;
			$scope.erroMsgReport = [];
	};

	function validateFilter(reportFilter) {
		var erroMsg = [];

		if (reportFilter != undefined) {
			if (reportFilter.startDate == undefined) {
				erroMsg.push("start Date is necesseray");
			}
		} else {
			erroMsg.push("Filter invalid.");
		}

    $scope.erroMsgReport = erroMsg;

    return !(erroMsg.length > 0);
	}
	
	$scope.resetbtn = function() {
		
		if ($scope.reportFilter != undefined) {
			$scope.reportFilter.userId = undefined;
			$scope.reportFilter.projectId = undefined;
			$scope.reportFilter.phaseId = undefined;
			$scope.reportFilter.clientId = undefined;
		}
		
	};
	
	$scope.getTotalHours = function(line){
	    var total = 0;
	    var allocations = line.allocations;
	    for(var i = 0; i < allocations.length; i++){
	        total += allocations[i].totalHours;
	    }
	    return total;
	}
}]);

app.filter('setDecimal', function ($filter) {
    return function (input) {
    	return Math.round(input * 100) / 100;
    };
});
