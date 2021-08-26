angular.module('market-front', []).controller('indexController', function ($scope, $http)
{
/*	anguler.module - создание приложения.

	('market-front', [ ]) - название приложения и список модулей-зависимостей (отсутствие скобок означает не создание пиложения, а поиск существующего).

	controller('indexController',…	- создаём контроллер и даём ему указанное имя.

	function ($scope, $http) - инжектим модули, которые входят в стандартную поставку ангуляра:
	 - $http - позволяет посылать из приложения http-запросы
	 - $scope - некий обменник между этим js-файлом и html-файлом.
*/

	const contextProductPath = 'http://localhost:8189/market/api/v1/products';	//< Для удобства составления адресов

	$scope.newProductId = 0;	//< такая переменная    видна в HTML-файле
	$scope.newProducTitle = "";
	$scope.newProductCost = 0;

	var productPageCurrent = 0;
	var productPageTotal = 0;	//< такая переменная не видна в HTML-файле

//----------------------------------------------------------------------------------------

/*	Так выполняется GET-запрос в приложение. Если этот запрос нужно выполянть из HTML-файла, то его придётся поместить в функцию (см.след.пример).

	$http.get (contextProductPath + 'products')
		 .then (function (response)
		 {
		 	//...
		 });	*/

/*	Типовое (для этого проекта) описание функции в js (для функций $scope. и var используются также, как для переменных):	*/
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
		//Как только отработал колбэк, ангуляр подставляет изменённые данные в связанную HTML-страницу.
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
		$http.get (contextProductPath + '/' + p.productId)
		.then (
		function successCallback (response)
		{
//			console.log (response.data);
			alert('id: ' + response.data.productId +
				  '\rНазвание: '+ response.data.productTitle +
				  '\rЦена: '+ response.data.productCost);
		},
		function failureCallback (response)
		{
			alert(response.data.messageText);
		});
	}

	function CopyOfProductDto (p)
	{
		this.productId		= p.productId;
		this.productTitle	= p.productTitle;
		this.productCost	= p.productCost;
	}

	$scope.startEditProduct = function (p)
	{
		$scope.new_product = new CopyOfProductDto (p);
	}

	$scope.createOrUpdateProduct = function ()
	{
		if ($scope.new_product.productId == null)
		{
			$scope.createNewProduct();
		}
		else
		{
			$scope.putProduct ($scope.new_product);
		}
	}

	$scope.createNewProduct = function ()
	{
		$http.post (contextProductPath, $scope.new_product)
		.then (
		function successCallback (response)
		{
			$scope.loadProductsPage();
			$scope.resetForm();
		},
		function failureCallback (response)
		{
			alert(response.data.messageText);	// Имя параметра должно совпадать с именем поля в передаваемом объекте, коим в данном случае выступает ru.gb.antonov.j67.beans.errorhandlers.ErrorMessage.
		});
	}

	$scope.putProduct = function (p)
	{
		$http.put (contextProductPath, p)
		.then(
		function successCallback (response)
		{
			$scope.loadProductsPage();
			$scope.resetForm();
		},
		function failureCallback (response)
		{
			alert(response.data.messageText);
		});
	}

	$scope.resetForm = function()
	{
		$scope.new_product = null;	//< сбросит содержимое формы
	}

//    $scope.generatePagesIndexes = function (startPage, endPage)
//    {
//        let arr = [];
//        for (let i = startPage; i < endPage + 1; i++)
//        {
//            arr.push(i);
//        }
//        return arr;
//    }

/*----------------------------------------------------------------------------------------*/

	$scope.loadProductsPage();	//< вызов описанной выше функции

}); //< НЕ ЗАБЫВАЕМ СЧИТАТЬ СКОБКИ!!

