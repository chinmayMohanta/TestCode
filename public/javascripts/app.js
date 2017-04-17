'use strict';


var lunaApp = angular.module('lunaaApp', []);


lunaApp.controller('CCController', function CCController($scope,$http) {

$scope.update = function(user) {

var isValid = false
var query = ""

if (($scope.cc != "" && $scope.cc != null) && ($scope.cn != "" && $scope.cn != null)) {isValid = false}
else if ($scope.cc != "" && $scope.cc != null)   {isValid = true; query = "/api/airDetailByCC/"+$scope.cc}
else if ($scope.cn != "" && $scope.cc != null)   {isValid = true; query = "/api/airDetailByCN/"+$scope.cn}

console.log("isvalid ......" +isValid + $scope.cc)
if (isValid){
 $scope.dataLoading = true
$http.get(query)
  .then(function(response) {

  console.log("hit ......" + query)
  $scope.messages = response.data[0];

	  console.log(response.data[0][0])
	  $scope.dataLoading = false

  });

  }


       };

});

lunaApp.controller('ReportController', function ReportController($scope,$http) {

$scope.update = function(user) {
$scope.dataLoading = true

$http.get("/api/hghestNumAirport/")
       .then(function(response) {
       console.log("hit ......")
       $scope.haReports = response.data[0];
       console.log(response.data[0])

       });
$http.get("/api/lowestNumAirport/")
       .then(function(response) {
       console.log("hit ......")
       $scope.laReports = response.data[0];
       console.log(response)

       });
$http.get("/api/topLeIdent/")
       .then(function(response) {
       console.log("hit ......")
       $scope.leIdentReports = response.data[0];
       console.log(response)

       });


$http.get("/api/surfacePerCountry/")
       .then(function(response) {
       console.log("hit ......")
       $scope.surfaceReports = response.data[0];
       console.log(response)
       $scope.dataLoading = false;

       });




       };

});

