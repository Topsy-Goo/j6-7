
angular.module('market-front', []).controller('indexController', function ($scope, $http)
{
/*	anguler.module - создание приложения.

	('market-front', [ ]) - название приложения и список модулей-зависимостей (отсутствие скобок означает не создание пиложения, а поиск существующего).

	controller - создаём контроллер.

	function ($scope, $http) - инжектим модули, которые входят в стандартную поставку ангуляра:
	 - $http - позволяет посылать из приложения http-запросы
	 - $scope - некий обменник между этим js-файлом и html-файлом.
*/

	const contextPath = 'http://localhost:8189/market/';	//< Для удобства составления адресов

	//var variable1 = 0;		< такая переменная не видна в HTML-файле
	//$scope.variable2 = 0;		< такая переменная    видна в HTML-файле
	var productPageCurrent = 0;
	var productPageTotal = 0;


/*	Так выполняется GET-запрос в приложение. Если этот запрос нужно выполянть из HTML-файла, то его придётся поместить в функцию (см.след.пример).

	$http.get (contextPath + 'products')
		 .then (function (response)
		 {
		 	//...
		 });

	Описание функции (для функций $scope. и var используются также, как для переменных):	*/
	$scope.loadProductsPage = function ()
	{
/*		var path = contextPath + 'products/page?p='+ productPageCurrent;
		console.log (path);
		$http.get (path)*/	//< НЕблокирующая операция
		$http({
			url: contextPath + 'products/page',
			method: 'GET',
			params:	{
					p: productPageCurrent
					}
		})
		.then (function (response)			//< получили ответ (через success-колбэк)
		{
			$scope.productsPage = response.data;	//< переменную можно объявлять где угодно в коде
			productPageCurrent = $scope.productsPage.pageable.pageNumber;
			productPageTotal = $scope.productsPage.totalPages;

//			console.log (response);	  < напечатать в консоли всю полученную информацию, включая служебную
			console.log (response.data);	//< напечатать в консоли только запрошенные данные
			console.log ('productPageTotal: '+ productPageTotal);
			console.log ('productPageCurrent: '+ productPageCurrent);
		});
			 /*	Как только отработал колбэк, ангуляр подставляет изменённые данные в связанную HTML-страницу.	*/
	};

	$scope.loadProductsPage();	//< вызов описанной выше функции

	$scope.deleteProduct = function (id)
	{
		$http.get (contextPath + 'products/delete/' + id)
			 .then (function (response)
			 {
			 	$scope.loadProductsPage();
			 });
	};

	$scope.infoProduct = function (p)
	{
		alert('id: ' + p.productId + ', Title: '+ p.productTitle + ', Proce: '+ p.productCost);
	};

	$scope.prevProductsPage = function ()
	{
		productPageCurrent --;
		$scope.loadProductsPage();
	}

	$scope.nextProductsPage = function ()
	{
		productPageCurrent ++;
		$scope.loadProductsPage();
	}

});

