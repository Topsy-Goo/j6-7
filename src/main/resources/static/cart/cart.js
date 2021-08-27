angular.module('market-front').controller('cartController', function ($scope, $http)
{
	const contextProductPath = 'http://localhost:8189/market/api/v1/products';
	var cartPageCurrent = 0;
	var cartPageTotal = 0;
	$scope.cartItemsCount = 0;

	$scope.getCartItemsCount = function()
	{
		$http.get (contextProductPath + '/cartitemscount')

		.then (
		function successCallback (response)
		{
			$scope.cartItemsCount = response.data;
		},
		function failureCallback (response)
		{
			alert (response.data.messageText);
		});
	}

	$scope.loadProductsPage = function ()
	{
		$scope.getCartItemsCount();

		$http
		({
			url: contextProductPath + '/cartpage',
			method: 'GET',
			params:	{p: cartPageCurrent}
		})
		.then (function (response)
		{
			$scope.productsPage = response.data;
			cartPageCurrent = $scope.productsPage.pageable.pageNumber;
			cartPageTotal = $scope.productsPage.totalPages;

			$scope.paginationArray = $scope.generatePagesIndexes(1, cartPageTotal);
		/*	console.log (response.data);
			console.log ('cartPageTotal: '+ cartPageTotal);
			console.log ('cartPageCurrent: '+ cartPageCurrent);*/
		});
	};

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
		cartPageCurrent = pageIndex -1;
		$scope.loadProductsPage();
	}

	$scope.prevProductsPage = function ()	//< загрузка левой соседней страницы
	{
		cartPageCurrent --;
		$scope.loadProductsPage();
	}

	$scope.nextProductsPage = function ()	//< загрузка правой соседней страницы
	{
		cartPageCurrent ++;
		$scope.loadProductsPage();
	}

	$scope.infoProduct = function (p)
	{
		alert('id: ' + p.productId + ', Title: '+ p.productTitle + ', Price: '+ p.productCost);
	}

	$scope.addToCart = function (pid)
	{
		console.log ('addToCart.pid: '+ pid);

		$http.get (contextProductPath + '/addtocart/' + pid)
		.then (
		function successCallback (response)
		{
			$scope.loadProductsPage();
		},
		function failureCallback (response)
		{
			alert ('Не удалось добавить продукт в корзину.\r'+ response.data.messageText);
		});
	}

	$scope.removeFromCart = function (pid)
	{
		if ($scope.cartItemsCount > 0)
		{
			$http.get (contextProductPath + '/removefromcart/' + pid)
			.then (
			function successCallback (response)
			{
				$scope.loadProductsPage();
			},
			function failureCallback (response)
			{
				alert (response.data.messageText);
			});
		}
	}

//----------------------------------------------------------------------------------------

	$scope.loadProductsPage();

});