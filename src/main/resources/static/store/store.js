
angular.module('market-front').controller('storeController', function ($rootScope, $scope, $http, $location)
{
/*	angular.module('market-front')	означает: используем приложение 'market-front'

	controller('storeController', … означает, что здесь мы создаём контроллер и даём ему указанное имя;

	$location - позволяет переходить на др.страницу
	$scope-переменные не видны за пределами того контроллера, в котором они объявлены.
	$rootScope - глобальный контекст (позволяет обращаться к ф-циям (и переменным?) откуда угодно)

	Каждый раз, когда мы будем переходить на эту страницу, будет производиться её инициализация.
*/
	const contextProductPath = 'http://localhost:8189/market/api/v1/products';
	const contextCartPath = 'http://localhost:8189/market/api/v1/cart';

	var productPageCurrent = 0;
	var productPageTotal = 0;	//< такая переменная не видна в HTML-файле
	$scope.cartItemsCount = 0;	//< такая переменная    видна в HTML-файле


	$scope.getCartItemsCount = function()
	{
		$http.get (contextCartPath + '/itemscount')
		.then (
		function successCallback (response)
		{
			$scope.cartItemsCount = response.data;
		},
		function failureCallback (response)
		{
			alert (response.data.messages);	//< название параметра взято из ErrorMessage
		});
	}

/*	Типовое (для этого проекта) описание функции в js (для функций $scope. и var используются также, как для переменных):	*/
	$scope.loadProductsPage = function ()
	{
		$scope.getCartItemsCount();
		$http
		({
			url: contextProductPath + '/page',
			method: 'GET',
			params:	{p: productPageCurrent}
		})
		.then (function (response)
		{
			$scope.productsPage = response.data;	//< переменную можно объявлять где угодно в коде
			productPageCurrent = $scope.productsPage.pageable.pageNumber;
			productPageTotal = $scope.productsPage.totalPages;

			$scope.paginationArray = $scope.generatePagesIndexes(1, productPageTotal);
		});
	}
//----------------------------------------------------------------- страницы
	$scope.generatePagesIndexes = function (startPage, endPage)
	{
		let arr = [];
		for (let i = startPage; i < endPage + 1; i++)
		{
			arr.push(i);
		}
		return arr;
	}

	$scope.loadProducts = function (pageIndex = 1)
	{
		productPageCurrent = pageIndex -1;
		$scope.loadProductsPage();
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
//----------------------------------------------------------------- редактирование
	$scope.deleteProduct = function (pid)
	{
		$http.get (contextProductPath + '/delete/' + pid)
		.then (function (response)
		{
			$scope.loadProductsPage();
		});
	}

	$scope.infoProduct = function (p)
	{
		alert('id: ' + p.productId + ', Title: '+ p.productTitle + ', Price: '+ p.productCost);
	}

/*	function CopyOfProductDto (p)
	{
		this.productId		= p.productId;
		this.productTitle	= p.productTitle;
		this.productCost	= p.productCost;
	}*/

	$scope.startEditProduct = function (pid)
	{
		$location.path ('/edit_product/'+ pid);
	}
//----------------------------------------------------------------- плюс/минус
	$scope.addToCart = function (pid)
	{
		$http.get (contextCartPath + '/add/' + pid)
		.then (
		function successCallback (response)
		{
			console.log ('addToCart: cartItemsCount: '+ response.data);
			$scope.cartItemsCount = response.data;
		},
		function failureCallback (response)
		{
			alert ('Не удалось добавить продукт в корзину.\r'+ response.data.messages);	//< название параметра взято из ErrorMessage
		});
	}

	$scope.removeFromCart = function (pid)
	{
		if ($scope.cartItemsCount > 0)
		{
			$http.get (contextCartPath + '/remove/' + pid)
			.then (
			function successCallback (response)
			{
				console.log ('removeFromCart: cartItemsCount: '+ response.data);
				$scope.cartItemsCount = response.data;
			},
			function failureCallback (response)
			{
				alert (response.data.messages);	//< название параметра взято из ErrorMessage
			});
		}
	}
//----------------------------------------------------------------- условия
	$scope.canShow = function()
	{
		return $rootScope.isUserLoggedIn();
	}
//----------------------------------------------------------------- вызовы
	$scope.loadProductsPage();
});