var naytaLisaysModal = function() {
	$('#merkintamodal').addClass('is-active');
};

var poistaMerkinta = function(id) {
	$("#poistonappi").attr("href", "${url}/1/poista/" + id);
	$('#poistomodal').addClass('is-active');
};

var closeModal = function() {
	$('.modal').removeClass('is-active');
};

$('.modal-background').click(function() {
	console.log("lol");
});