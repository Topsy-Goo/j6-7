(function ()	//< Описание основной ф-ции
{
	angular
		.module('market-front', ['ngRoute','ngStorage'])
		.config(config)
		.run(run);

	function config ($routeProvider)
	{
		$routeProvider
			.when('/store',
			{
				templateUrl: 'store/store.html',
				controller:	 'storeController'
			})
			.when('/main',
			{
				templateUrl: 'main/main.html',
				controller:	 'mainController'
			})
			.when('/edit_product/:pid',
			{
				templateUrl: 'edit_product/edit_product.html',
				controller:	 'edit_productController'
			})
			.when('/edit_product',
			{
				templateUrl: 'edit_product/edit_product.html',
				controller:	 'edit_productController'
			})
			.when('/cart',
			{
				templateUrl: 'cart/cart.html',
				controller:	 'cartController'
			})
			.when('/registration',
			{
				templateUrl: 'registration/registration.html',
				controller:	 'registrationController'
			})
			.otherwise(
			{
				redirectTo:	'/main'
			});
	}

	function run ($rootScope, $http, $localStorage)
	{
        if ($localStorage.webMarketUser != null)
        {
            $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.webMarketUser.token;
        }
	}
})();

angular.module('market-front').controller('indexController',
								function ($rootScope, $scope, $http, $localStorage, $location)
{
	const contextProductPath = 'http://localhost:8189/market/api/v1/auth';
	$scope.appTitle = 'Marketplace';
	$scope.mainPageTitle = 'Главная страница';
	$scope.storePageTitle = 'Каталог продуктов';
	$scope.edit_productPageTitle = 'Создать продукт';
	$scope.cartPageTitle = 'Ваша корзина';


	$rootScope.isUserLoggedIn = function ()	{	return $localStorage.webMarketUser != null;	};

	$scope.tryToRegister = function ()
	{
		console.log ('$scope.tryToRegister call.');
		$scope.clearUserFields();
		$location.path('/registration');
	}

	$scope.clearUserFields = function ()
	{
		$scope.user = null;
	}

	$scope.tryToLogin = function ()
	{
		if ($scope.user != null)
		{
			$http.post(contextProductPath + '/login', $scope.user)
			.then
			(function successCallback (response)
			{
				if (response.data.token)
				{
					$http.defaults.headers.common.Authorization = 'Bearer ' + response.data.token;
					$localStorage.webMarketUser = {login: $scope.user.login, token: response.data.token};
					$scope.clearUserFields();
				}
			},
			function failureCallback (response)
			{
				console.log ('$scope.tryToLogin failure callback.');
				alert ('ОШИБКА: '+ response.data.messages);
			});
		}
	};

	$scope.tryToLogout = function ()
	{
		$scope.removeUserFromLocalStorage();
		$scope.clearUserFields();
		$location.path('/main');
	};

	$scope.removeUserFromLocalStorage = function ()
	{
		delete $localStorage.webMarketUser;
		$localStorage.webMarketUser == null;
		$http.defaults.headers.common.Authorization = '';
	};
});

