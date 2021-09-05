
angular.module('market-front').controller('registrationController',
						function ($rootScope, $scope, $http, $location, $localStorage)
{
	const contextProductPath = 'http://localhost:8189/market/api/v1/auth';
	var contextPrompt_Registered = "Вы успешно зарегистрированы.";
	var contextPrompt_Unathorized = "Введите логин, паоль и адрес электронной почты.";
	var contextPrompt_LogedIn = "Вы уже авторизованы.";
	var contextPrompt_Error = "Ошибка регистрации.";
	$scope.contextPrompt = "";


	$scope.prepareToRegistration = function()
	{
		$scope.clearNewUserFields();

		if ($rootScope.isUserLoggedIn())
		{
			$scope.contextPrompt = contextPrompt_LogedIn;
		}
		else
		{
			$scope.contextPrompt = contextPrompt_Unathorized;
		}
	}

	$scope.tryToRegister = function ()
	{
		if ($scope.new_user != null)
		{
			$http.post(contextProductPath + '/register', $scope.new_user)
			.then
			(function successCallback (response)
			{
				if (response.data.token)
				{
					$http.defaults.headers.common.Authorization = 'Bearer ' + response.data.token;
					$localStorage.webMarketUser = {login: $scope.new_user.login, token: response.data.token};
					$scope.clearNewUserFields();
					$scope.contextPrompt = contextPrompt_Registered;
				}
			},
			function failureCallback (response)
			{
				alert ('ОШИБКА: '+ response.data.messages);
				console.log ('$scope.tryToRegister failure callback. : '+ response.data.messages);
				$scope.contextPrompt = contextPrompt_Error;
			});
		}
	}

	$scope.cancelRegistration = function()
	{
		$scope.clearNewUserFields();
		$location.path('/main');
	}

	$scope.clearNewUserFields = function()	{	$scope.new_user = null;	}

	$scope.canShow = function()	{	return !$rootScope.isUserLoggedIn();	}

	$scope.prepareToRegistration();
});
