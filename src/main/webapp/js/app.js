var app = angular.module('andes', ['ngRoute','ngStorage','ui.bootstrap', 'angular-table', 'vcRecaptcha']);

toastr.options = {
		"progressBar": true,
		"timeOut": "3000",
		"positionClass": "toast-top-full-width"
}

/**
 * Configure the Routes
 */
app.config([ '$routeProvider', function($routeProvider) {

	$routeProvider
	// Home
	.when("/home", {
		templateUrl : "views/welcome.html",
		controller : "LoginController"
	}).when('/', {
		redirectTo : "/home"
	}).when('/login', {
		templateUrl : "views/login.html",
		controller : "LoginController"
	}).when('/profile', {
		templateUrl : "views/profile.html",
		controller : "ProfileController"
	})

	// Pages
	.when("/timesheet", {
		templateUrl : "views/timesheet/user_timesheet.html",
		controller : "TimeSheetController"
	}).when("/timesheetAdmin", {
		templateUrl : "views/timesheet/admin_timesheet.html",
		controller : "TimeSheetAdminController"
	}).when("/currencyPage", {
		templateUrl : "views/currency.html",
		controller : "CurrencyController"
	}).when("/users", {
		templateUrl : "views/users.html",
		controller : "UserController"
	}).when("/sitePage", {
		templateUrl : "views/sites/site.html",
		controller : "SitesController"
	}).when("/clientPage", {
		templateUrl : "views/clients/client.html",
		controller : "ClientsController"
	}).when("/timesheet", {
		templateUrl : "views/timesheet/user_timesheet.html",
		controller : "TimeSheetController"
	}).when("/projects", {
		templateUrl : "views/project/list.html",
		controller : "ProjectsController"
	}).when("/pricing", {
		templateUrl : "partials/pricing.html",
		controller : "PageCtrl"
	}).when("/services", {
		templateUrl : "partials/services.html",
		controller : "PageCtrl"
	}).when("/phases", {
		templateUrl : "views/phases/phaseList.html",
		controller : "PhasesController"
	}).when("/allocations", {
		templateUrl : "views/allocations/allocationsList.html",
		controller : "AllocationsController"
	}).when("/holidays", {
		templateUrl: "views/holidays/holidays.html", 
		controller: "HolidayController"
	}).when("/settings", {
		templateUrl : "views/settings.html",
		controller : "SettingsController"
	})

	// Access permission
	.when("/403", {
		templateUrl : "views/403.html",
		controller : "TableController"
	})
	// Reports
	.when("/reportbydate", {
		templateUrl : "views/report/reportbydate.html",
		controller : "ReportUserByDateController"
	}).when("/reportrevenue", {
		templateUrl : "views/report/reportRevenue.html",
		controller : "ReportRevenueController"
	}).when("/reportbyusersmonth", {
		templateUrl : "views/report/reportbyusersmonth.html",
		controller : "ReportByUsersMonthController"
	})

	// else 404
	.otherwise("/404", {templateUrl : "partials/404.html",controller : "PageCtrl"})
	.otherwise({ templateUrl : "views/error.html", controller : "TableController"});
} ]);


app.factory('myHttpResponseInterceptor', [ '$q', '$location', '$localStorage',
                                           function($q, $location, $localStorage) {
	return {
		// optional method
		'request' : function(config) {
			if($localStorage.principal != null){
				if(config.params === undefined){
					config.params = {};
				 }
				 if(config.url.indexOf("html") == -1){
						config.params.userId=  $localStorage.principal.userId;
				 }
				
				 
			}
			return config;
		},

		// optional method
		'requestError' : function(rejection) {
			return $q.reject(rejection);
		},

		// optional method
		'response' : function(response) {
			if(response.status === 401){
				$location.path('/403')
			}
			return response;
		},

		// optional method
		'responseError' : function(rejection) {
			if (rejection.status === 511) {
				$localStorage.logado = false;
				$localStorage.principal = [];
				$location.path('/login')
			}else if(rejection.status === 401){
				$location.path('/403')
			}else if(rejection.status === 404){
				$location.path('/404')
			}
			return $q.reject(rejection);
		}
	};

} ]);
//Http Intercpetor to check auth failures for xhr requests
app.config([ '$httpProvider', function($httpProvider) {
	$httpProvider.interceptors.push('myHttpResponseInterceptor');
} ]);
