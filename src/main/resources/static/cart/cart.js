angular.module('market-front').controller('cartController', function ($scope, $http)
{
	const contextCartPath = 'http://localhost:8189/market/api/v1/cart';
	var cartPageCurrent = 0;
	var cartPageTotal = 0;
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
			alert (response.data.messages);	//< название параметра взято из ErrorMessage
		});
	}

	$scope.loadProductsPage = function ()
	{
		$scope.getCartItemsCount();

		$http
		({
			url: contextCartPath + '/page',
			method: 'GET',
			params:	{p: cartPageCurrent}
		})
		.then (function (response)
		{
			$scope.productsPage = response.data;
			cartPageCurrent = $scope.productsPage.pageable.pageNumber;
			cartPageTotal = $scope.productsPage.totalPages;

			$scope.paginationArray = $scope.generatePagesIndexes(1, cartPageTotal);

		});
	};

	$scope.infoProduct = function (p)
	{
		alert('id: ' + p.productId + ',\rназвание: '+ p.productTitle + ',\rцена: '+ p.productCost);
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
		cartPageCurrent = pageIndex -1;
		$scope.loadProductsPage();
	}

	$scope.prevProductsPage = function ()
	{
		cartPageCurrent --;
		$scope.loadProductsPage();
	}

	$scope.nextProductsPage = function ()
	{
		cartPageCurrent ++;
		$scope.loadProductsPage();
	}
//----------------------------------------------------------------- плюс/минус
	$scope.addToCart = function (pid)
	{
		$http.get (contextCartPath + '/add/' + pid)
		.then (
		function successCallback (response)
		{
			$scope.loadProductsPage();
		},
		function failureCallback (response)
		{
			alert ('Не удалось добавить продукт в корзину:\r'+ response.data.messages);	//< название параметра взято из ErrorMessage
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
				$scope.loadProductsPage();
			},
			function failureCallback (response)
			{
				alert ('Не удалось удалить продукт из корзины:\r'+ response.data.messages);	//< название параметра взято из ErrorMessage
			});
		}
	}
//----------------------------------------------------------------- вызовы
	$scope.loadProductsPage();
});