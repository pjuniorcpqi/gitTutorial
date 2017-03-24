app.controller('SitesController', function($scope, $http) {
	
	refresh();
	
	$scope.deleteRow = function(item) {
		var temp = item.name;
		if (confirm('Are you sure?')) {
			$http.post("sites/delete", item).then(function(response) {
				refresh();
				toastr["success"]("Site destroyed successfully.");
			}, function(response) {
				toastr["error"]("Something went wrong. Please, contact an administrator.");
			});
		}
	};
	
	$scope.editRow = function(site) {
		var newName = prompt("Please enter the new name:", site.name);
		var index;
		if (newName != undefined && isUnique(newName)) {
			site.name = newName;
			$http.post("sites/save", site).then(function(response) {
				refresh();
				toastr["success"]("Site edited successfully.");
			}, function(response) {
				toastr["error"]("Something went wrong. Please, contact an administrator.");
			});
		}
	};
	
	$scope.addRow = function() {
		if (isUnique($scope.name)) {
			var site = {name: $scope.name};
			$http.post("sites/save", site).then(function(response) {
				toastr["success"]("Site added successfully.");
				refresh();
				$scope.name = '';
			}, function(response) {
				toastr["error"]("Something went wrong. Please, contact an administrator.");
			});
		}
	};
	
	function refresh() {
		$http.get("sites/").then(function (response) {
			if (response.data.length == 0) {
				$scope.sites = [];
			} else {
				$scope.sites = response.data;
			}
		});
	}
	
	function findIndex(name) {
		return $scope.sites.map(function(e) {return e.name}).indexOf(name);
	}
	
	function isUnique(aux) {
		var uniqueness = true;
		for (site of $scope.sites) {
			if (site.name.toLowerCase().trim() == aux.toLowerCase().trim()) {
				toastr["error"]("This site already exists.");
				uniqueness = false;
				break;
			}
		}
		return uniqueness;
	}
});