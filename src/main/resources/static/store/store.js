
angular.module('market-front').controller('storeController', function ($scope, $http, $location)
{
/*	angular.module('market-front')	означает: используем приложение 'market-front'

	controller('storeController', … означает, что здесь мы создаём контроллер и даём ему указанное имя;

	$location - позволяет переходить на др.страницу

	$scope-переменные не видны за пределами того контроллера, в котором они объявлены.

	Каждый раз, когда мы будем переходить на эту страницу, будет производиться её инициализация.
*/
	const contextProductPath = 'http://localhost:8189/market/api/v1/products';	//< Для удобства составления адресов

	var productPageCurrent = 0;
	var productPageTotal = 0;	//< такая переменная не видна в HTML-файле
	$scope.cartItemsCount = 0;	//< такая переменная    видна в HTML-файле
//	let currentPageIndex = 1;

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

		/*	console.log (response.data);
			console.log ('productPageTotal: '+ productPageTotal);
			console.log ('productPageCurrent: '+ productPageCurrent);*/
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

/*	function CopyOfProductDto (p)
	{
		this.productId		= p.productId;
		this.productTitle	= p.productTitle;
		this.productCost	= p.productCost;
	}*/

	$scope.startEditProduct = function (pid)
	{
		$location.path ('/edit_product/'+ pid);
//		$scope.new_product = new CopyOfProductDto (p);
	}

	$scope.addToCart = function (pid)
	{
		console.log ('addToCart.pid: '+ pid);

		$http.get (contextProductPath + '/addtocart/' + pid)
		.then (
		function successCallback (response)
		{
			console.log ('addToCart: cartItemsCount: '+ response.data);
			$scope.cartItemsCount = response.data;
		},
		function failureCallback (response)
		{
			alert ('Не удалось добавить продукт в корзину.\r'+ response.data.messageText);
		});
	}

	$scope.removeFromCart = function (pid)
	{
	/*	console.log ('removeFromCart: pid: '+ pid);
		console.log ('removeFromCart: $scope.cartItemsCount: '+ $scope.cartItemsCount);*/

		if ($scope.cartItemsCount > 0)
		{
			$http.get (contextProductPath + '/removefromcart/' + pid)
			.then (
			function successCallback (response)
			{
				console.log ('removeFromCart: cartItemsCount: '+ response.data);
				$scope.cartItemsCount = response.data;
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