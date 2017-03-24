app.controller("TimeSheetController", function($scope, $uibModal, $localStorage, $http) {

	initScopes();

	function initScopes() {

		$scope.userName = $localStorage.principal.username;
		//Dates

		$scope.currentTime = new Date();
		if($scope.selectedTime == null){
			$scope.selectedTime = new Date();
		}

		if($scope.monthSelected){
			var aux = "01/" + $scope.monthSelected; //Isso resolve a incompatibilidade da timesheet com o firefox.
			$scope.selectedTime = new Date(aux);
		}

		$scope.thisDay = $scope.selectedTime.getDate();
		$scope.thisMonth = currentMonth();
		$scope.thisMonthName = monthName(currentMonth());
		$scope.thisYear = ($scope.selectedTime).getFullYear();
		$scope.daysOfMonth = getAllDatesFromMonthYear($scope.thisMonth, $scope.thisYear);

		//TimeSheet
		$scope.monthSelected;
		getTimeSheetFromMonth();
		getSettings();
		$scope.viewModeOptions = ['Day' ,'Week','Month'];
		$scope.currentPage = 1;
		$scope.erroMsg = [];
	}

	$scope.initScopes = function(){
		initScopes();
	};

	function getSettings(){
		$http.get("settingsValues/")
		.then(function (response) {
			$scope.settings = response.data;
			$scope.viewMode = $scope.settings.viewModeTimesheet;
		}, function(response){
			toastr["error"]("Something went wrong. Please, contact an administrator.");
		});
	}

	function getTimeSheetFromMonth(){
		$http.get("timelogs/getTimeSheet", {
			params: {
				idUser: $localStorage.principal.userId,
				date:$scope.thisYear + '-' +  ($scope.thisMonth+1) + '-' + '1'
			}
		}).then(function (response) {
			$scope.timeSheets = response.data;
			$scope.totalItems = $scope.timeSheets.length;
			$scope.itemsPerPage = $scope.timeSheets.length;
			$scope.setItemsPerPage($scope.viewMode);

		}, function(response){
			toastr["error"]("Something went wrong. Please, contact an administrator.");
		});
	}


	$scope.calculateTotal = function(day,in1,out1,in2,out2,in3,out3){

		// Nao tenta calcular o total para dias sem timelogs
		if (in1 === undefined) {
			return;
		}

		var totalAmount = 0;
		if(in1 != "" &&  out1 != ""){
			totalAmount = (in1 && out1 ) ? getDifference(day,in1,out1) : totalAmount;
			totalAmount = (in2 && out2) ? totalAmount.add(getDifference(day,in2,out2)) : totalAmount;
			totalAmount =  (in3 && out3 ) ? totalAmount.add(getDifference(day,in3,out3)) : totalAmount;
		}

		day.timeLog.total = durationToString(totalAmount);
	};

	function getDifference(day, timeInString, timeOutString){
		if(timeInString == null || timeInString == "" || timeOutString == null){
			return 0;
		}

		timeIn = createDateTimeFromHourMinute(day.date, timeInString);
		timeOut = createDateTimeFromHourMinute(day.date, timeOutString);

		var timeDifference = moment(timeOut).diff(moment(timeIn));

		var totalHours = moment.duration(timeDifference);
		return totalHours;
	}

	$scope.submit = function(day){

		//Formatting dates
		day.timeLog.date = new Date(day.date);
		day.date = new Date(day.date);
		day.userName = $scope.userName;
		day.userId = $localStorage.principal.userId;
		day.userRequesting = $localStorage.principal.username;
		$scope.erroMsg = [];

		if(validateTimeLog(day)){
			for (var y in day.workLogs) {
				day.workLogs[y].timeInserted = createNumberFromHourMinute(day.workLogs[y].timeInserted);
			}
			$http.post('timelogs/saveTimeSheet/', day)
			.then(function (response) {
				formatTimeInserted(day);
				toastr["success"]("Timesheet saved successfully.");
				callBackSave();
			}, function(response){
				formatTimeInserted(day);
				var msg = "";
				if (response.status == 412) {
					msg = "This timelog was defined as an absence.";
				} else {
					msg = "Something went wrong. Please, contact an administrator.";
				}
				toastr["error"](msg);
			});
		};
	}

	function currentMonth(){
		return ($scope.selectedTime).getMonth();
	}

	function monthName(month){
		var monthNames = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
		return monthNames[month];
	}

	function getAllDatesFromMonthYear(month, year){
		var firstDay = new Date(year, month, 1, 0, 0, 0, 0);
		var allDays = [firstDay];
		var sameMonth = true;

		for(var i = 2 ; sameMonth ; i++){
			sameMonth = month == (new Date(year, month, i, 0, 0, 0, 0)).getMonth();
			if(sameMonth){
				allDays.push(new Date(year, month, i, 0, 0, 0, 0));
			};
		}
		return allDays;
	}

	function createDateTimeFromHourMinute(day, time){
		if(day == null || time == null){
			return;
		}

		var hour = "00";
		var minute = "00";

		if (time != 0) {
			var hour = time.split(":")[0];
			var minute = time.split(":")[1];
		}
		var dayFromTimeLog = new Date(day);
		var dateTime = new Date(dayFromTimeLog.getFullYear(), dayFromTimeLog.getMonth(), dayFromTimeLog.getDate(),0, 0, 0, 0);

		dateTime.setMinutes(minute);
		dateTime.setHours(hour);

		return dateTime;
	}

	function createNumberFromHourMinute(time){
		var factor = 1/60;
		var value = time;
		if(isNaN(time)){
			var hour = time.split(":")[0];
			var minute = time.split(":")[1];
			minute = Number(minute*factor);
			value = Number(hour) + minute;
		}
		return 	value;
	}

	$scope.createTimepickerStringFromNumber = function createTimepickerStringFromNumber(w){

		if ((typeof w.timeInserted) == "string") {
			w.timeInserted = createNumberFromHourMinute(w.timeInserted);
		}

		//Sem essa checagem, todos os campos iniciam com valor 0:00. Com ela, os campos ficam vazios.
		if (w.timeInserted) {
			var time = w.timeInserted

			var hours = Math.floor(time);
			var minutes = (time % 1) * 0.6; //Converte para a quantidade em minutos
			minutes = minutes.toFixed(2).split(".")[1]; //Arredonda para dois decimais

			w.timeInserted = hours + ":" + minutes;
		}
	}

	function validateTimeLog(day){

		if (day.timeLog.inTime1 === "") {
			toastr["error"]("Incomplete timelogs");
			return false;
		}

		if (validateTimeInserted(day) == false) {
			toastr["error"]("The allocated time cannot be smaller or larger than the number of hours worked on that day");
			return false;
		}

		return true;
	}

	function callBackSave() {
		getTimeSheetFromMonth();
	}

	$scope.validateTimelogs = function(day) {
		var i1 = day.timeLog.inTime1;
		var i2 = day.timeLog.inTime2;
		var i3 = day.timeLog.inTime3;
		var o1 = day.timeLog.outTime1;
		var o2 = day.timeLog.outTime2;
		var o3 = day.timeLog.outTime3;

		var showToast = false;

		if (i1 === "") {
			showToast = true;
			day.timeLog.inTime1 = "";
		} else if (o1 && o1 != "" && getDifference(day, i1, o1) <= 0) {
			showToast = true;
			day.timeLog.outTime1 = "";
		} else if (i2 && i2 != "" && getDifference(day, o1, i2) <= 0) {
			showToast = true;
			day.timeLog.inTime2 = "";
		} else if (o2 && o2 != "" && getDifference(day, i2, o2) <= 0) {
			showToast = true;
			day.timeLog.outTime2 = "";
		} else if (i3 && i3 != "" && getDifference(day, o2, i3) <= 0) {
			showToast = true;
			day.timeLog.inTime3 = "";
		} else if (o3 && o3 != "" && getDifference(day, i3, o3) <= 0) {
			showToast = true;
			day.timeLog.outTime3 = "";
		}

		if(showToast) {
			toastr["warning"]("Timelogs must be inserted in chronological order.");
		}

		$scope.calculateTotal(day,day.timeLog.inTime1,day.timeLog.outTime1,day.timeLog.inTime2,day.timeLog.outTime2,day.timeLog.inTime3,day.timeLog.outTime3);
	}

	$scope.setItemsPerPage = function(viewMode) {
		$scope.currentPage = 1; //reset to first page

		if(viewMode == 'Day'){
			$scope.itemsPerPage = 1;
			$scope.currentPage = $scope.thisDay;
			if($scope.currentTime.getMonth() ===  $scope.selectedTime.getMonth()){
				$scope.currentPage = $scope.currentTime.getDate(); 
			}
		}
		if(viewMode == 'Week'){
			$scope.itemsPerPage = 7;
			if($scope.currentTime.getMonth() ===  $scope.selectedTime.getMonth()){
				var firstDayOfMonth = new Date($scope.thisYear, $scope.thisMonth, 1).getDay();
				$scope.currentPage = Math.ceil(($scope.selectedTime.getDate() + firstDayOfMonth)/7);
			}

		}
		if(viewMode == 'Month'){
			$scope.itemsPerPage = $scope.totalItems;
		}
	};

	$scope.printReport = function printReport() {

		var thisMonth = $scope.thisMonth + 1;
		if (thisMonth < 10) {
			thisMonth = "0" + thisMonth;
		}

		var input = {
				idUser: $localStorage.principal.userId,
				userName: $localStorage.principal.userName,
				yearMonth:$scope.thisYear + '-' + thisMonth
		}

		$http.post("/userReport", input, {responseType: 'arraybuffer'})
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

	function validateTimeInserted(day) {
		var sum = getSumOfTimeInsertions(day);
		var timelogTotal = createDateTimeFromHourMinute(day.date, day.timeLog.total);
		return moment(sum).isSame(moment(timelogTotal));
	}

	$scope.fillTimeInserted = function(day) {
		//Caso so tenha uma alocacao, atribui-se todo o tempo logado a ela
		if (day.workLogs.length == 1) {
			if(!day.workLogs[0].revenueLock){
				day.workLogs[0].timeInserted = day.timeLog.total;
			}
		}
	};

	$scope.fillLastTimeInserted = function(day) {
		var sum = 0;
		sum = getSumOfTimeInsertions(day);

		var lastWorkLog = day.workLogs[day.workLogs.length - 1];
		if(!lastWorkLog.revenueLock){
			if(lastWorkLog.timeInserted){
			var lastTimeInserted = lastWorkLog.timeInserted;
			var hour = lastTimeInserted.split(":")[0];
			var minute = lastTimeInserted.split(":")[1];
			sum = moment(sum).subtract(hour, 'h');
			sum = moment(sum).subtract(minute, 'm');
			}

			sum = moment(sum).format("HH:mm");
			var difference = getDifference(day, sum, day.timeLog.total);

			if (difference > 0) {
			lastWorkLog.timeInserted = durationToString(difference);
			} else if (difference == 0) {
			lastWorkLog.timeInserted = null;
			}else{
				return;
			}
		}
	};

	function getSumOfTimeInsertions(day) {
		var sum = moment(day.date).startOf('day');
		for (var y in day.workLogs) {
			if (day.workLogs[y].timeInserted) {
				var temp = day.workLogs[y].timeInserted;
				var hour = temp.split(":")[0];
				var minute = temp.split(":")[1];

				sum = moment(sum).add(hour, 'h');
				sum = moment(sum).add(minute, 'm');
			}
		}
		return sum;
	}

	function durationToString(time) {
		if(time != 0){
			var totalMinutes = time.minutes();

			//Isso impede que horarios como 1:04 aparecam como 1:4
			if (totalMinutes < 10) {
				totalMinutes = "0" + totalMinutes;
			}
		}
		return (time != 0) ? Math.floor(time.hours()) +":"+ totalMinutes : time;
	}

	//Exibindo o tempo alocado em formato de string ao inves de decimal
	function formatTimeInserted(day) {
		for (var y of day.workLogs) {
			$scope.createTimepickerStringFromNumber(y);
		}
	}

	$scope.getWeekDay = function(day){
		var weekDayNames = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
		return weekDayNames[moment(day).day()];
	}

	$scope.calculateTotalHoursFromMonth = function(timeSheets){
		$scope.hoursColor = 'grey';
		$scope.totalHours = '';
		var amount = 0;
		var required = 0;
		var remaining = 0;
		for(var x in timeSheets){
			
			if(timeSheets[x].timeLog != null && timeSheets[x].timeLog.total == null){
				$scope.calculateTotal(timeSheets[x], timeSheets[x].timeLog.inTime1,
					 timeSheets[x].timeLog.outTime1, timeSheets[x].timeLog.inTime2, 
					 timeSheets[x].timeLog.outTime2, timeSheets[x].timeLog.inTime3, 
					 timeSheets[x].timeLog.outTime3);
			}
			
			if(timeSheets[x].timeLog != null && timeSheets[x].timeLog.total != null && timeSheets[x].timeLog.total != 0){
				amount = amount + createNumberFromHourMinute(timeSheets[x].timeLog.total);
			};
			for(var y in timeSheets[x].allocations){
				if(timeSheets[x].holiday != null){
					continue;
				}else if(moment(timeSheets[x].date).isBefore($scope.currentTime) && (moment(timeSheets[x].date).day() != 0 && moment(timeSheets[x].date).day() != 6)){
					required = required + timeSheets[x].allocations[y].hoursPerDay;
				};
			};

		};

		if(amount){
			remaining = amount - required;
			var hours;
			if(remaining >= 0){
				$scope.hoursColor = '#00b303';//green
				hours = Math.floor(remaining);
			}else{
				$scope.hoursColor = '#ff0000';//red
				hours = Math.ceil(remaining);
			}

			var minutes = (remaining % 1) * 0.6; //Converte para a quantidade em minutos
			minutes = minutes.toFixed(2).split(".")[1]; //Arredonda para dois decimais
			$scope.totalHours =  hours + ":" + minutes;
		};
		return $scope.totalHours;
	};

	$scope.openModalDelete = function (day) {
		day.userRequesting = $localStorage.principal.username;
		day.userName = $scope.userName;
		day.userId = $localStorage.principal.userId;
		$scope.timeLogSelected = day;
		$scope.modalInstanceDelete  = $uibModal.open({
			templateUrl: 'views/timesheet/timeSheetDelete.html',
			controller: 'ModalTimeSheetController',
			scope: $scope,
			windowClass: 'large-Modal-delete'
		});

		$scope.modalInstanceDelete.result.then(function (isLoad) {
			if(isLoad){
				initScopes();
			}
		});
	};


});

app.controller('ModalTimeSheetController', ['$scope','$http','$timeout','$localStorage', function($scope, $http,$timeout,$localStorage) {

	var configHead = {header : {'Content-Type' : 'charset=UTF-8'}};
	$scope.timeSheet = {};
	$scope.erroMsg = [];
	$scope.successMsg = false;

	if($scope.timeLogSelected != undefined){
		$scope.timeSheet = $scope.timeLogSelected;
	}

	$scope.processDelete = function() {
		var timeSheet = $scope.timeSheet;
		
		if(timeSheet.timeLog == null){
			return(callBackDelete());
		}
		if(timeSheet.timeLog.id == null){
			timeSheet.timeLog.inTime1 = null;
			timeSheet.timeLog.outTime1 = null;
			timeSheet.timeLog.outTime2 = null;
			timeSheet.timeLog.inTime3 = null;
			timeSheet.timeLog.outTime3 = null;
			return(callBackDelete());
		}else{

			for (var y in timeSheet.workLogs) {
				timeSheet.workLogs[y].timeInserted = createNumberFromHourMinute(timeSheet.workLogs[y].timeInserted);
			}
			var url = 'timelogs/removeTimesheet/';
			$http.post(url, timeSheet, configHead)
			.then(callBackDelete)
			.catch(callBackErro);
		};
	};

	function callBackSaveUpdate(response) {
		$scope.successMsg = true;
		$timeout(callback, 2000);

	}

	function createNumberFromHourMinute(time){
		var factor = 1/60;
		var value = time;
		if(isNaN(time)){
			var hour = time.split(":")[0];
			var minute = time.split(":")[1];
			minute = Number(minute*factor);
			value = Number(hour) + minute;
		}
		return 	value;
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
