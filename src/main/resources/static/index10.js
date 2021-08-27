(function ()	//< Описание основной ф-ции
{
	angular
		.module('market-front', ['ngRoute'])
		.config(config)
		.run(run);

/*	anguler.module - создание (основного или дополнительного) модуля приложения.

	('market-front', […]) - название приложения и список модулей-зависимостей (разделённых запятыми;

	наличие []-скобок означает создание основного модуля, а в скобках можно указать список подключаемых модулей (возможно подключение сторонних модулей);

	отсутствие []-скобок означает создание доп.модуля. При его создании будет выполнен поиск пиложения с указанным именем (поиск осн.модуля указанного приложения).

	ngRoute - имя модуля, подключенного в html-файле при пом.тэга <script src="…/angular-route.min.js">.

	config(func_name) - указывает на ф-цию, которая будет конфигурировать приложение.

	run(func_name) - указывает на ф-цию, которая будет запускаться при старте приложения.
*/

	function config ($routeProvider)
	{
	/*	$routeProvider - модуль, который позволяет переходить между страницами
	*/
		$routeProvider
			.when('/store',	//< задаём адрес страницы с товарами
			{
				templateUrl: 'store/store.html',	//<	адрес страницы с товарами и…
				controller:	 'storeController'		//	…имя её контроллера
			})
			.when('/main',		//< задаём постфикс для перехода на главную страницу
			{
				templateUrl: 'main/main.html',		//< адрес главной страницы и…
				controller:	 'mainController'		//	…имя её контроллера
			})
			.when('/edit_product/:pid',	//< для возможности передавать параметр требуется указать $routeParams в объявлении edit_productController'а.
			{
				templateUrl: 'edit_product/edit_product.html',
				controller:	 'edit_productController'
			})
			.when('/edit_product',	//< пусть переход на страницу через главное меню означает намерение создать новый товар, а не редактировать существующий
			{
				templateUrl: 'edit_product/edit_product.html',
				controller:	 'edit_productController'
			})
			.when('/cart',
			{
				templateUrl: 'cart/cart.html',
				controller:	 'cartController'
			})
			.otherwise(
			{
				redirectTo:	'/main'
			});
	}

	function run ($rootScope, $http)	{	}

})();

angular.module('market-front').controller('indexController', function ($rootScope, $scope, $http)
{
/*	function ($scope, $http) - инжектим модули, которые входят в стандартную поставку ангуляра:

	$http - позволяет посылать из приложения http-запросы
	$scope - некий обменник между этим js-файлом и html-файлом.
	$rootScope -
*/
	const contextProductPath = 'http://localhost:8189/market/api/v1/products';
	$scope.appTitle = 'Marketplace';
	$scope.mainPageTitle = 'Главная страница';
	$scope.storePageTitle = 'Каталог продуктов';
	$scope.edit_productPageTitle = 'Создать продукт';
	$scope.cartPageTitle = 'Ваша корзина';
});

