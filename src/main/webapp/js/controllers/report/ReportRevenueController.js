app.controller("ReportRevenueController", ['$scope', '$http', function($scope, $http){
  var configHead = {header : {'Content-Type' : 'charset=UTF-8'}};
	$scope.reportUserByDate = {};
	$scope.reportResult = [];
	$scope.erroMsgReportRevenue = [];
	$scope.Math = window.Math;
	init ();

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
				var url = 'report/revenues/';
				$http.post(url, reportFilter, configHead).then(function(response){
					$scope.reportResult = response.data;
				}, function (response) {
					$scope.erroMsgReportRevenue.push("Erro to call server.");
				});
		}
	};

	$scope.resetbtn = function(){
		$scope.reportFilter = null;
		$scope.erroMsgReportRevenue = [];
		$scope.reportResult = [];
	};

	function validateFilter() {
		var valid = true;

		if ($scope.reportFilter == undefined || $scope.reportFilter.startDate == undefined) {
			toastr["error"]("Please, select a month.");
			valid = false;
		}

		return valid;
	}
	
	$scope.printReport = function printReport() {
		if (validateFilter($scope.reportFilter)) {
			var reportFilter = (JSON.parse(JSON.stringify($scope.reportFilter)));
			
			var input = {
					userId: reportFilter.userId,
					projectId: reportFilter.projectId,
					clientId: reportFilter.clientId,
					phaseId: reportFilter.phaseId,
					startDate: reportFilter.startDate,
					endDate: reportFilter.endDate
			};
			
			var url = '/revenueReport';
			$http.post(url, input, {responseType: 'arraybuffer'})
			.then(function (response) {
				var file = new Blob([response.data], {type: 'application/pdf'});
				var fileURL = URL.createObjectURL(file);
				var temp = window.open(fileURL, '_blank');

				if (!temp) {
					toastr["info"]('You have a popup blocker enabled. Please allow popups for this domain.');
				}
			}, function(response){
				toastr["error"]("Something went wrong. Please, contact an administrator.");
			});
		}
	}
}]);
