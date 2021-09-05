
angular.module('market-front').controller('storeController', function ($scope, $http, $location)
{
	const contextProductPath = 'http://localhost:8189/market/api/v1/products';
	const contextCartPath = 'http://localhost:8189/market/api/v1/cart';

	var productPageCurrent = 0;
	var productPageTotal = 0;
	$scope.cartItemsCount = 0;

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
			alert (response.data.messages);
		});
	}

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
			$scope.productsPage = response.data;
			productPageCurrent = $scope.productsPage.pageable.pageNumber;
			productPageTotal = $scope.productsPage.totalPages;

			$scope.paginationArray = $scope.generatePagesIndexes(1, productPageTotal);
		});
	}

	$scope.generatePagesIndexes = function (startPage, endPage)
	{
		let arr = [];
		for (let i = startPage; i < endPage + 1; i++)
		{
			arr.push(i);
		}
		return arr;
	}

	$scope.loadProducts = function (pageIndex = 1)	//< загрузка страницы по индексу
	{
		productPageCurrent = pageIndex -1;
		$scope.loadProductsPage();
	}

	$scope.prevProductsPage = function ()	//< загрузка левой соседней страницы
	{
		productPageCurrent --;
		$scope.loadProductsPage();
	}

	$scope.nextProductsPage = function ()	//< загрузка правой соседней страницы
	{
		productPageCurrent ++;
		$scope.loadProductsPage();
	}

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

	$scope.startEditProduct = function (pid)
	{
		$location.path ('/edit_product/'+ pid);
	}

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
			alert ('Не удалось добавить продукт в корзину.\r'+ response.data.messages);
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
				alert (response.data.messages);
			});
		}
	}

	$scope.canShow = function()
	{
		return $rootScope.isUserLoggedIn();
	}

	$scope.loadProductsPage();
});