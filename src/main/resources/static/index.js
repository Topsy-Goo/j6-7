
angular.module('market-front', []).controller('indexController', function ($scope, $http)
{
/*	anguler.module - создание приложения.

	('market-front', [ ]) - название приложения и список модулей-зависимостей (отсутствие скобок означает не создание пиложения, а поиск существующего).

	controller - создаём контроллер.

	function ($scope, $http) - инжектим модули, которые входят в стандартную поставку ангуляра:
	 - $http - позволяет посылать из приложения http-запросы
	 - $scope - некий обменник между этим js-файлом и html-файлом.
*/

	const contextProductPath = 'http://localhost:8189/market/api/v1/products';	//< Для удобства составления адресов

	var productPageCurrent = 0;
	var productPageTotal = 0;	//< такая переменная не видна в HTML-файле
	$scope.newProductId = 0;	//< такая переменная    видна в HTML-файле

/*----------------------------------------------------------------------------------------*/

/*	Так выполняется GET-запрос в приложение. Если этот запрос нужно выполянть из HTML-файла, то его придётся поместить в функцию (см.след.пример).

	$http.get (contextProductPath + 'products')
		 .then (function (response)
		 {
		 	//...
		 });

	Описание функции (для функций $scope. и var используются также, как для переменных):	*/
	$scope.loadProductsPage = function ()
	{
		$http
		({	// НЕблокирующая операция
			url: contextProductPath + '/page',
			method: 'GET',
			params:	{p: productPageCurrent}
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

	$scope.createNewProduct = function ()
	{
		$http.post (contextProductPath, $scope.new_product)
		.then (
		function successCallback (response)
		{
			$scope.loadProductsPage();
			$scope.new_product = null;	//< сбросит содержимое формы
		},
		function failureCallback (response)
		{
			alert(response.data.messageText);	// Имя параметра должно совпадать с именем поля в передаваемом объекте, коим в данном случае выступает ru.gb.antonov.j67.beans.errorhandlers.ErrorMessage.
		});
	}

	$scope.deleteProduct = function (id)
	{
		$http.get (contextProductPath + '/delete/' + id)
			 .then (function (response)
			 {
			 	$scope.loadProductsPage();
			 });
	}

	$scope.infoProduct = function (p)
	{
		alert('id: ' + p.productId + ', Title: '+ p.productTitle + ', Price: '+ p.productCost);
	}

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

	$scope.updateProductMinus = function (p)	{	$scope.putProduct(p, -1);	}
	$scope.updateProductPlus = function (p)		{	$scope.putProduct(p, 1);	}

	$scope.putProduct = function (p, step)
	{
		let oldCost = p.productCost;

		p.productCost += step;
		$http.put (contextProductPath, p)
		.then(
		function successCallback (response){$scope.loadProductsPage();},
		function failureCallback (response)
		{
			alert(response.data.messageText);
			p.productCost = oldCost;
		});
	}
/*----------------------------------------------------------------------------------------*/

	$scope.loadProductsPage();	//< вызов описанной выше функции


});

