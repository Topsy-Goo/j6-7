
angular.module('market-front').controller('mainController', function ($scope, $http)
{
	const contextAuthoPath = 'http://localhost:8189/market/api/v1/auth';


	$scope.getStatistics = function ()
	{
		$http.get(contextAuthoPath + '/statistics')
		.then(
		function successCallback (response)
		{
			$scope.statistics = response.data;
			console.log (response.data);
			//alert (response.data);
		},
		function failureCallback (response)
		{
			alert (response.data);
		});
	}
//----------------------------------------------------------------- вызовы
	$scope.getStatistics();
});
