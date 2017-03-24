app.controller("CurrencyController", ['$scope', '$http', function($scope, $http) {

  var configHead = {header : {'Content-Type' : 'charset=UTF-8'}};

  initScope();
  loadCurrencies();

  $scope.processCurrency = function () {
    var currency = $scope.formCurrency;

    if (validateCurrency(currency)) {
      var url = '/currencies/';
      if (currency.id != undefined) {
        url += currency.id;
      }

      $http.post(url, currency, configHead)
      .then(callBackSaveUpdateCurrency);
    }
  };

  $scope.updateCurrency = function (currency) {
    $scope.formCurrency = (JSON.parse(JSON.stringify(currency)));
  };

  function initScope() {
    $scope.currencies = null;
    $scope.formCurrency = {};
  }

  function loadCurrencies() {
    $http.get('/currencies/', configHead)
    .then(function(response) {
      $scope.currencies = response.data;
    });
  }

  function callBackSaveUpdateCurrency() {
    initScope();
    loadCurrencies();
  }

  function validateCurrency(currency) {
    var erroMsg = [];
    if (currency.currency == undefined || currency.currency.trim() == "") {
      erroMsg.push("currency is required.");
    }

    if (currency.code == undefined || currency.code.trim() == "") {
      erroMsg.push("Code is required.");
    }

    $scope.erroMsg = erroMsg;

    return !(erroMsg.length > 0);
  }
}]);
