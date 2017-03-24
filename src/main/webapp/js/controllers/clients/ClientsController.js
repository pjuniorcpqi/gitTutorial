app.controller('ClientsController', function($scope, $http) {
	
	refresh();
	
	$scope.deleteRow = function(item) {
		var temp = item.name;
		if (confirm('Are you sure?')) {
			$http.post("clients/delete", item).then(function(response) {
				refresh();
				toastr["success"]("Client deleted successfully.");
			}, function(response) {
				toastr["error"]("Something went wrong. Please, contact an administrator.");
			});
		}
	};
	
	$scope.editRow = function(client) {
		var newName = prompt("Please enter the new name:", client.name);
		var index;
		if (newName != undefined && isUnique(newName)) {
			client.name = newName;
			$http.post("clients/save", client).then(function(response) {
				refresh();
				toastr["success"]("Client edited successfully.");
			}, function(response) {
				toastr["error"]("Something went wrong. Please, contact an administrator.");
			});
		}
	};
	
	$scope.addRow = function() {
		if (isUnique($scope.name)) {
			var client = {name: $scope.name};
			$http.post("clients/save", client).then(function(response) {
				toastr["success"]("Client added successfully.");
				refresh();
				$scope.name = '';
			}, function(response) {
				toastr["error"]("Something went wrong. Please, contact an administrator.");
			});
		}
	};
	
	function refresh() {
		$http.get("clients/").then(function (response) {
			if (response.data.length == 0) {
				$scope.clients = [];
			} else {
				$scope.clients = response.data;
			}
		});
	}
	
	function findIndex(name) {
		return $scope.clients.map(function(e) {return e.name}).indexOf(name);
	}
	
	function isUnique(aux) {
		var uniqueness = true;
		for (client of $scope.clients) {
			if (client.name.toLowerCase().trim() == aux.toLowerCase().trim()) {
				toastr["error"]("This client already exists.");
				uniqueness = false;
				break;
			}
		}
		return uniqueness;
	}
});