//start product
$('.card .aFavs').click(function() {
	$(this).parents('.card').toggleClass('esFav');
});
//Producto al carrito
$('.card .alCarrito').click(function() {
	$(this).parents('.card').toggleClass('enCarrito');
});
/*End product*/