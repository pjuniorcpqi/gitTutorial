app.controller("SettingsController", ['$scope', '$http', function($scope, $http) {

	initScopes();

	function initScopes (){
		getSettings();
	}

	$scope.viewModeOptions = ['Day' ,'Week','Month'];

	function getSettings(){
		$http.get("settingsValues/")
		.then(function (response) {
			$scope.settings = response.data;

		}, function(response){
			toastr["error"]("Something went wrong. Please, contact an administrator.");
		});
	}
	
	$scope.submit = function(settings){
		var url = "settings/save";
		$http.post(url, settings).then(callBackSave).catch(callBackError);
	}
	
	function callBackSave(response) {
		toastr["success"]("Settings saved successfully.");
		initScopes();
	}
	
	function callBackError(response) {
		toastr["error"]("Something went wrong. Please, contact an administrator.");
		initScopes();
	};
	
	

}]);
