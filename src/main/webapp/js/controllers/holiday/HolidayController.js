app
		.controller(
				"HolidayController",
				function($scope, $http) {
					var configHead = {
						header : {
							'Content-Type' : 'charset=UTF-8'
						}
					};

					$scope.sites = [];
					$scope.holidays = [];
					$scope.toSaveHolidayList = [];
					$scope.monthSelected;

					initScope();

					function initScope() {
						getHolidays();
						getSites();
					}

					$scope.clearSelectedMonth = function() {
						$scope.monthSelected = "";
					}

					$scope.refreshByMonth = function() {
						$http.get("holidays/month", {
							params : {
								month : $scope.monthSelected
							}
						}).then(function(response) {
							$scope.holidays = response.data;
						})
					};

					function getHolidays() {
						$http.get("holidays").then(function(response) {
							if (response.data.length == 0) {
								$scope.holidays = [];
							} else {
								$scope.holidays = response.data;
							}
						});
					}

					function getSites() {
						$http.get("sites/").then(function(response) {
							if (response.data.length == 0) {
								$scope.sites = [];
							} else {
								$scope.sites = response.data;
							}
						});
					}

					$scope.reloadScope = function() {
						getHolidays();
						getSites();
					};

					$scope.addRow = function() {
						var holiday = {
							holidayDate : moment($scope.date, 'DD/MM/YYYY',
									true).format(),
							description : $scope.desc,
							sites : $scope.selectedSites
						}
						$scope.toSaveHolidayList.push(holiday);
					}

					$scope.saveHolidays = function() {
						if ($scope.toSaveHolidayList.length >= 1) {
							$http
									.post("holidays/saveAll",
											$scope.toSaveHolidayList)
									.then(
											function(response) {
												toastr["success"]("Holiday(s) saved with success!");
												initScope();
												
											},
											function(response) {
												toastr["error"]("Cannot save your data. Please contact your boss.");
											});
						}

					}

					$scope.updateHolidayDate = function() {
						$http
								.post("holidays/update", $scope.selectedHoliday)
								.then(
										function(response) {
											$scope.selectedHoliday = {};
											initScope();
											toastr["success"]("Holiday saved with success!");
										},
										function(response) {
											toastr["error"]("Cannot save this holiday. Please contact your boss.");
										}									
								);
					}

					$scope.removeHolidayDate = function() {
						var result = confirm("Do you really want to remove these item?");
						if (result) {
							$http
									.post("holidays/deleteAll",
											$scope.selectedHolidayList)
									.then(
											function(response) {
												initScope();
												$scope.selectedHolidayList = {};
												toastr["success"]("Holiday(s) removed with success!");
											},
											function(response) {
												toastr["error"]("Cannot remove this/these holiday(s). Please contact your boss.");
											});
						}
					}

					$scope.clearSelectedHolidayList = function() {
						$scope.selectedHolidayList = {};
					};

					$scope.clearSelectedHoliday = function() {
						$scope.selectedHoliday = {};
					};

					$scope.removeHoliday = function(index) {
						$scope.toSaveHolidayList.splice(index, 1);
					};

					$scope.removeSite = function(index) {
						$scope.selectedHoliday.sites.splice(index, 1);
					};

					$scope.addNewSite = function() {
						for (var i = 0; i < $scope.selectedSites.length; i++) {
							$scope.selectedHoliday.sites
									.push($scope.selectedSites[i]);
						}
					};

					$scope.clearHolidayList = function() {
						$scope.toSaveHolidayList = [];
					};

					$scope.clearHolidayForm = function() {
						$scope.selectedSites = [];
						$scope.date = "";
						$scope.desc = "";
					};

					$scope.clearSelectedSites = function() {
						$scope.selectedSites = [];
					};
					
				
					$("#holidayDate").datepicker({
						format : "dd/mm/yyyy",
						autoclose : true,
						todayHighlight : true
					});

					$("#monthDate").datepicker({
						format : "M-yy",
						viewMode : "months",
						minViewMode : "months",
						autoclose : true
					});

				});